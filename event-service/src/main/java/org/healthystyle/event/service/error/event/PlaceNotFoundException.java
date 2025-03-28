package org.healthystyle.event.service.error.event;

import org.healthystyle.util.error.AbstractException;
import org.springframework.validation.BindingResult;

public class PlaceNotFoundException extends AbstractException {
	private Long id;

	public PlaceNotFoundException(Long id, BindingResult result) {
		super(result, "Could not found place by id '%s'", id);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

}
