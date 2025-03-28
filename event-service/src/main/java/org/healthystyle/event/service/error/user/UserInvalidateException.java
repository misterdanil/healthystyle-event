package org.healthystyle.event.service.error.user;

import org.healthystyle.event.service.cache.dto.User;
import org.springframework.validation.BindingResult;

public class UserInvalidateException extends RuntimeException {
	private User user;
	private BindingResult result;

	public UserInvalidateException(User user, BindingResult result) {
		super(String.format("The user is invalid: %s. Result: %s", user, result));
		this.user = user;
		this.result = result;
	}

	public User getUser() {
		return user;
	}

	public BindingResult getResult() {
		return result;
	}

}
