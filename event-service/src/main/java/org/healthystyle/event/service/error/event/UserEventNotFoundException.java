package org.healthystyle.event.service.error.event;

import org.healthystyle.util.error.AbstractException;
import org.springframework.validation.BindingResult;

public class UserEventNotFoundException extends AbstractException {
	private Long id;

	public UserEventNotFoundException(Long id, BindingResult result) {
		super(result, "Could not found user of event by id '%s'", id);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

}
