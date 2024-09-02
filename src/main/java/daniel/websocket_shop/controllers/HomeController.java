package daniel.websocket_shop.controllers;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

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
	public ModelAndView showHome() {
		var m = new ModelAndView();

		m.addObject("allEvents", s.getEventHistory());
		m.setViewName("index");

		return m;
	} 

	@RequestMapping(path = "/clear", method = RequestMethod.POST)
	public ResponseEntity<String> clearEvents() {
		s.clearAll();

		return ResponseEntity.ok()
			.header("HX-Refresh", "true")
			.build();
	}
}
