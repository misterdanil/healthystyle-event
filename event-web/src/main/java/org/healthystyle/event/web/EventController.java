package org.healthystyle.event.web;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.healthystyle.event.model.Event;
import org.healthystyle.event.service.EventService;
import org.healthystyle.event.service.dto.DistanceEvent;
import org.healthystyle.event.service.dto.EventSaveRequest;
import org.healthystyle.event.service.dto.mapper.EventMapper;
import org.healthystyle.event.service.error.event.EventNotFoundException;
import org.healthystyle.event.service.error.event.RoleUnacceptableException;
import org.healthystyle.event.service.error.event.UserEventExistException;
import org.healthystyle.event.service.error.event.UserNotFoundException;
import org.healthystyle.event.service.oauth2.TokenService;
import org.healthystyle.util.error.ErrorResponse;
import org.healthystyle.util.error.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class EventController {
	@Autowired
	private EventService service;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private EventMapper mapper;
	private RequestCache requestCache = new HttpSessionRequestCache();

	@PostMapping("/event")
	public ResponseEntity<?> addEvent(@RequestBody EventSaveRequest saveRequest) throws URISyntaxException {
		Event event;
		try {
			event = service.save(saveRequest);
		} catch (ValidationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse("1000", e.getGlobalErrors(), e.getFieldErrors()));
		} catch (EventNotFoundException | UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse("1000", e.getGlobalErrors(), e.getFieldErrors()));
		} catch (UserEventExistException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ErrorResponse("1000", e.getGlobalErrors(), e.getFieldErrors()));
		} catch (RoleUnacceptableException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(new ErrorResponse("1000", e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.created(new URI("/events/" + event.getId())).build();
	}

	@GetMapping("/oauth2/redirect")
	@ResponseBody
	public String getOAuth2Redirect(HttpServletResponse response) throws URISyntaxException, IOException {
		return "http://localhost:3003/oauth2/authorize?client_id=event&redirect_uri=http://localhost:3002/auth/event&scope=openid"
				+ "&response_type=code" + "&response_mode=query" + "&state=xhfk2ronslf" + "&nonce=65jhtt4m9r8";
//		response.sendRedirect("http://localhost:3003/oauth2/authorize?client_id=event&redirect_uri=http://localhost:3002/auth/event&scope=openid"
//			+ "&response_type=code" + "&response_mode=query" + "&state=xhfk2ronslf" + "&nonce=65jhtt4m9r8");
	}

	@GetMapping("/auth/event")
	@ResponseStatus(code = HttpStatus.OK)
	public Map<String, String> getCode(@RequestParam String code, HttpServletResponse response)
			throws URISyntaxException, IOException {
		Map<String, String> resp = tokenService.getTokens(code);
		response.addCookie(new Cookie("access_token", "dwadw"));
		return resp;
//		response.sendRedirect("http://localhost:3000/event");
	}

	@GetMapping("/getName")
	@ResponseStatus(code = HttpStatus.OK)
	@ResponseBody
	public String getCode(HttpServletResponse response) throws URISyntaxException, IOException {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	@GetMapping("/events")
	public ResponseEntity<?> getEvents(String title, Double latitude, Double longitude, int page, int limit) {
		List<DistanceEvent> events;
		try {
			events = service.findNearestByCoordinates(title, latitude, longitude, page, limit);
		} catch (ValidationException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse("1000", e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.ok(events);
	}

	@GetMapping("/events/{id}")
	public ResponseEntity<?> getEvents(@PathVariable Long id) {
		Event event;
		try {
			event = service.findById(id);
		} catch (ValidationException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse("1000", e.getGlobalErrors(), e.getFieldErrors()));
		} catch (EventNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse("1001", e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.ok(mapper.toDto(event));
	}

}
