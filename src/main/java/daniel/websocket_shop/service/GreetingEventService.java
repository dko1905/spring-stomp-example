package daniel.websocket_shop.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.collections4.QueueUtils;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import daniel.websocket_shop.domain.GreetingEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GreetingEventService {
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired
	private TaskScheduler taskScheduler;

	// Map to keep tracking of which subscriptions should generate fake messages.
	// The subscription topic, called "channel", can have multiple listeners.
	// When all
	private Map<String, MutablePair<Integer, ScheduledFuture<?>>> subscriptions = Collections
			.synchronizedMap(new HashMap<>());

	public void subscribeEventStream(String channel) {
		log.info("Subscribing to {}", channel);

		subscriptions.computeIfPresent(channel, (String _channel, MutablePair<Integer, ScheduledFuture<?>> val) -> {
			log.info("Incrementing counter to {}", val.left + 1);
			val.left += 1;
			return val;
		});

		subscriptions.computeIfAbsent(channel, (String _channel) -> {
			log.info("Creating {}", channel);

			// If subscription does not exist, create new generator and save.
			var counter = 1; // One subscription at creation
			var gg = new GreetingGenerator(channel, messagingTemplate);
			var task = taskScheduler.scheduleAtFixedRate(gg, Duration.ofSeconds(3));
			return MutablePair.of(counter, task);
		});

	}

	public void unsubscribeEventStream(String channel) {
		log.info("Unsubscribing to {}, counter is {}", channel,
				subscriptions.get(channel) != null ? subscriptions.get(channel).left : -1);

		subscriptions.computeIfPresent(channel, (String _channel, MutablePair<Integer, ScheduledFuture<?>> val) -> {
			if (val.left > 1) {
				// Decrement if more than 1 listener
				val.left -= 1;
				log.info("Decrementing task counter: {}", val.left);
				return val;
			} else {
				var ret = val.right.cancel(false);
				log.info("Cancelling task, ret: {}", ret);
				return null;
			}
		});
	}
}

@RequiredArgsConstructor
class GreetingGenerator implements Runnable {
	private final String channel;
	private final SimpMessagingTemplate messagingTemplate;
	private int eventCounter = 0;

	@Getter
	@Setter
	private int subscriptionCounter = 1; // Used to stop when no-one is listening

	public void incrementSubscriptionCounter() {
		subscriptionCounter += 1;
	}

	public void decrementSubscriptionCounter() {
		subscriptionCounter -= 1;
	}

	@Override
	public void run() {
		var ev = new GreetingEvent(eventCounter, channel, "Alice", "Bob");
		eventCounter += 1;

		messagingTemplate.convertAndSend(String.format("/topic/%s", channel), ev);
	}
}
