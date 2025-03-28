package org.healthystyle.event.service.error.event;

import org.healthystyle.util.error.AbstractException;
import org.springframework.validation.BindingResult;

public class OwnerDeletionException extends AbstractException {
	private Long userEventId;

	public OwnerDeletionException(Long userEventId, BindingResult result) {
		super(result, "It's impossible to delete owner role of user event '%s'", userEventId);
		this.userEventId = userEventId;
	}

	public Long getUserEventId() {
		return userEventId;
	}

}
