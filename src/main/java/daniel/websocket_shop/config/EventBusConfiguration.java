package daniel.websocket_shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yeliheng.eventbus.spring.EventBusAutoRegister;
import com.yeliheng.eventbus.spring.context.EventBusContext;

@Configuration
public class EventBusConfiguration {

	@Bean(name = "eventBusContext")
	public EventBusContext eventBusContext() {
		return new EventBusContext();
	}

	@Bean(name = "eventBusAutoRegister")
	public EventBusAutoRegister eventBusAutoRegister() {
		return new EventBusAutoRegister();
	}
}
