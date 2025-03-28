package org.healthystyle.event.service.error.event;

import org.healthystyle.util.error.AbstractException;
import org.springframework.validation.BindingResult;

public class EventNotFoundException extends AbstractException {
	private Long id;

	public EventNotFoundException(Long id, BindingResult result) {
		super(result, "Could not found event by id '%s'", id);
		this.id = id;
	}

	public Long getId() {
		return id;
	}
}
