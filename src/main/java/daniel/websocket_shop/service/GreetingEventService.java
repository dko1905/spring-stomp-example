package daniel.websocket_shop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.collections4.QueueUtils;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.yeliheng.eventbus.annotations.Subscribe;

import daniel.websocket_shop.domain.GreetingEvent;

@Service
public class GreetingEventService {
	Logger logger = LoggerFactory.getLogger(GreetingEventService.class);
	static final int EVENT_HISTORY = 15; // Only keep last 15 events in cache
	Queue<GreetingEvent> eventHistory = QueueUtils.synchronizedQueue(new CircularFifoQueue<>(EVENT_HISTORY));
	@Autowired
	SimpMessagingTemplate messagingTemplate;

	public List<GreetingEvent> getEventHistory() {
		return new ArrayList<>(eventHistory);
	}

	public void clearAll() {
		eventHistory.clear();
	}

	@Subscribe
	public void onGreetingEvent(GreetingEvent ev) {
		eventHistory.add(ev);
		messagingTemplate.convertAndSend("/topic/greetings", ev);
	}
}

