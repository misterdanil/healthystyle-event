package org.healthystyle.event.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.healthystyle.event.model.UserEvent;
import org.healthystyle.event.repository.dto.ParticipateStatus;
import org.healthystyle.event.service.UserEventService;
import org.healthystyle.event.service.dto.mapper.UserEventMapper;
import org.healthystyle.event.service.error.event.EventNotFoundException;
import org.healthystyle.event.service.error.event.RoleUnacceptableException;
import org.healthystyle.event.service.error.event.UserEventExistException;
import org.healthystyle.event.service.error.event.UserNotFoundException;
import org.healthystyle.util.error.ErrorResponse;
import org.healthystyle.util.error.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserEventController {
	@Autowired
	private UserEventService service;
	@Autowired
	private UserEventMapper mapper;

	@GetMapping(value = "/participate")
	public ResponseEntity<?> getParticipateStatuses(@RequestParam("user_id") Long userId,
			@RequestParam("event_ids") Long[] eventIds) {
		List<ParticipateStatus> statuses = service.checkStatus(userId, eventIds);

		return ResponseEntity.ok(statuses);
	}

	@GetMapping("/events/{id}/members")
	public ResponseEntity<?> getMembers(@PathVariable Long id, @RequestParam int page, @RequestParam int limit) {
		Page<UserEvent> members;
		try {
			members = service.findByEvent(id, page, limit);
		} catch (ValidationException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse("1000", e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.ok(members.map(mapper::toDto));
	}

	@GetMapping("/events/{id}/member")
	public ResponseEntity<?> join(@PathVariable Long id) throws URISyntaxException {
		UserEvent member;
		try {
			member = service.join(id);
		} catch (UserNotFoundException | EventNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse("1001", e.getGlobalErrors(), e.getFieldErrors()));
		} catch (UserEventExistException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ErrorResponse("1003", e.getGlobalErrors(), e.getFieldErrors()));
		} catch (RoleUnacceptableException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(new ErrorResponse("1004", e.getGlobalErrors(), e.getFieldErrors()));
		} catch (ValidationException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse("1000", e.getGlobalErrors(), e.getFieldErrors()));
		}

		return ResponseEntity.ok(new URI("/members/" + member.getId()));
	}
}
