package daniel.websocket_shop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.collections4.QueueUtils;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import com.yeliheng.eventbus.annotations.Subscribe;

import daniel.websocket_shop.domain.GreetingEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GreetingEventService {
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

	// Handle subscribe, unsubscribe and disconnect Spring events
	@EventListener
	public void onSubscribeEvent(SessionSubscribeEvent ev) {
		StompHeaderAccessor headers = StompHeaderAccessor.wrap(ev.getMessage());
		log.info("SUBSCRIBE - ", headers.getId(), headers.getSubscriptionId());
	}

	@EventListener
	public void onUnsubscribeEvent(SessionUnsubscribeEvent ev) {
		StompHeaderAccessor headers = StompHeaderAccessor.wrap(ev.getMessage());
		log.info("UNSUBSCRIBE - ", headers.getId(), headers.getSubscriptionId());
	}

	@EventListener
	public void onDisconnectEvent(SessionDisconnectEvent ev) {
		StompHeaderAccessor headers = StompHeaderAccessor.wrap(ev.getMessage());
		log.info("DISCONNECT - ", headers.getId(), headers.getSubscriptionId());
	}
}
