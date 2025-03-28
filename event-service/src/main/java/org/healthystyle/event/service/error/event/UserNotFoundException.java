package org.healthystyle.event.service.error.event;

import org.healthystyle.util.error.AbstractException;
import org.springframework.validation.BindingResult;

public class UserNotFoundException extends AbstractException {
	private Long id;

	public UserNotFoundException(BindingResult result, Long id) {
		super(result, "Could not found user by id '%s'", id);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

}
