package org.healthystyle.event.service.error.event;

import org.healthystyle.util.error.AbstractException;
import org.springframework.validation.BindingResult;

public class UserEventExistException extends AbstractException {
	private Long userId;
	private Long eventId;

	public UserEventExistException(Long userId, Long eventId, BindingResult result) {
		super(result, "A member has already existed by user id '%s' and event id '%s'", userId, eventId);
		this.userId = userId;
		this.eventId = eventId;
	}

	public Long getUserId() {
		return userId;
	}

	public Long getEventId() {
		return eventId;
	}

}
