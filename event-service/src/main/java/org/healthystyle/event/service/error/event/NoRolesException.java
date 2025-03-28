package org.healthystyle.event.service.error.event;

import org.healthystyle.util.error.AbstractException;
import org.springframework.validation.BindingResult;

public class NoRolesException extends AbstractException {
	private Long userEventId;

	public NoRolesException(Long userEventId, BindingResult result) {
		super(result, "User event '%s' has no roles anymore", userEventId);
		this.userEventId = userEventId;
	}

	public Long getUserEventId() {
		return userEventId;
	}

}
