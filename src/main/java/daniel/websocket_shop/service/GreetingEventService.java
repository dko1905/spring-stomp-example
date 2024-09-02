package daniel.websocket_shop.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	List<GreetingEvent> allEvents = Collections.synchronizedList(new ArrayList<GreetingEvent>());
	@Autowired
	SimpMessagingTemplate messagingTemplate;

	public List<GreetingEvent> getAllEvents() {
		return allEvents;
	}

	public void clearAll() {
		allEvents.clear();
	}

	@Subscribe
	public void onSoldEvent(GreetingEvent ev) {
		allEvents.add(ev);
		messagingTemplate.convertAndSend("/topic/greetings", ev);
	}
}
