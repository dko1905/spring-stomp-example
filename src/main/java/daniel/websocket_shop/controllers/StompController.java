package daniel.websocket_shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import daniel.websocket_shop.service.GreetingEventService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class StompController {
	@Autowired
	GreetingEventService greetingEventService;

	// Handle subscribe, unsubscribe and disconnect Spring events
	@EventListener
	public void onSubscribeEvent(SessionSubscribeEvent ev) {
		StompHeaderAccessor headers = StompHeaderAccessor.wrap(ev.getMessage());
		var channel = headers.getDestination().replace("/topic/", "");

		log.info("SUBSCRIBE - {} - {}", channel, headers.getSubscriptionId());
		greetingEventService.subscribeEventStream(channel);
	}

	@EventListener
	public void onUnsubscribeEvent(SessionUnsubscribeEvent ev) {
		StompHeaderAccessor headers = StompHeaderAccessor.wrap(ev.getMessage());
		var channel = headers.getSubscriptionId().replace("/topic/", "");

		log.info("UNSUBSCRIBE - {} - {}", channel, headers.getSubscriptionId());
		greetingEventService.unsubscribeEventStream(channel);
	}

	@EventListener
	public void onDisconnectEvent(SessionDisconnectEvent ev) {
		StompHeaderAccessor headers = StompHeaderAccessor.wrap(ev.getMessage());
		log.info("DISCONNECT - {} - {}", headers.getDestination(), headers.getSubscriptionId());
	}
}
