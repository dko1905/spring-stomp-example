package daniel.websocket_shop.controllers;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import daniel.websocket_shop.service.GreetingEventService;

@Controller
public class HomeController {
	@Autowired
	GreetingEventService s;

	@ModelAttribute("date")
	public String modelDate() {
		return DateTimeFormatter.RFC_1123_DATE_TIME.format(Instant.now().atZone(ZoneId.systemDefault()));
	}

	@RequestMapping("/")
	public String showHome() {
		return "index";
	}
}
