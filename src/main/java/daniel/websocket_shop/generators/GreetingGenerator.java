package daniel.websocket_shop.generators;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import com.yeliheng.eventbus.EventBus;

import daniel.websocket_shop.domain.GreetingEvent;

@Component
public class GreetingGenerator implements DisposableBean, Runnable {
	Logger logger = LoggerFactory.getLogger(GreetingGenerator.class); 
	Thread thread;
	volatile boolean isRunning = true;
	static final String[] NAMES = {"James", "Michael", "Robert", "John", "David", "William"};
	Random random;

	GreetingGenerator() {
		this.thread = new Thread(this);
		this.thread.setName("greeting-generator"); // Greeting generator
		this.thread.start();
		this.random = new Random();
	}

	@Override
	public void run() {
		logger.info("RUnning generator");

		int counter = 0;

		while (isRunning) {
			String nameFrom = NAMES[random.nextInt(NAMES.length)];
			String nameTo = NAMES[random.nextInt(NAMES.length)];
			GreetingEvent ev = new GreetingEvent(counter, nameFrom, nameTo);
			EventBus.post(ev);
			
			counter += 1;
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.error("Event generator interrupted", e);
			}
		}
	}

	@Override
	public void destroy() {
		isRunning = false;
	}
}
