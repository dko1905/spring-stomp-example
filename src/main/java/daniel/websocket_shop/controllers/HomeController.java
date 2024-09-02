package daniel.websocket_shop.controllers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
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

	// @ModelAttribute("allEvents")
	// public List<SoldEvent> allEvents() {
	// 	return s.getAllEvents();
	// }

	@RequestMapping("/")
	public ModelAndView showHome() {
		var m = new ModelAndView();

		m.addObject("allEvents", s.getAllEvents());
		m.addObject("date", Instant.now().toString());
		m.setViewName("index");

		return m;
	} 

	@RequestMapping(path = "/clear", method = RequestMethod.POST)
	public ResponseEntity clearEvents() {
		s.clearAll();

		return ResponseEntity.ok()
			.header("HX-Refresh", "true")
			.build();
	}
}
