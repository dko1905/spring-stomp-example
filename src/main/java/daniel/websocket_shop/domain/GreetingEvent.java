package daniel.websocket_shop.domain;

import com.yeliheng.eventbus.interfaces.IEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GreetingEvent implements IEvent {
	private int id;
	private String nameFrom;
	private String nameTo;
}
