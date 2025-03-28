package org.healthystyle.event.service.error.event;

import org.healthystyle.event.model.role.Type;
import org.healthystyle.util.error.AbstractException;
import org.springframework.validation.BindingResult;

public class RoleUnacceptableException extends AbstractException {
	private Type type;

	public RoleUnacceptableException(String message, Type type, BindingResult result, Object... args) {
		super(message, result, args);
		this.type = type;
	}

	public Type getType() {
		return type;
	}
}