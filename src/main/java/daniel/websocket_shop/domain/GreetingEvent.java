package daniel.websocket_shop.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GreetingEvent {
	private int id;
	private String channel;
	private String nameFrom;
	private String nameTo;
}
