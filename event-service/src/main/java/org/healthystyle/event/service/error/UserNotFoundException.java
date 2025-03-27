package org.healthystyle.event.service.error;

import java.util.Set;

import org.healthystyle.util.error.AbstractException;
import org.springframework.validation.BindingResult;

public class UserNotFoundException extends AbstractException {
	private Long[] ids;

	public UserNotFoundException(BindingResult result, Set<Long> ids) {
		super(result, "Could not found users by ids '%s'", ids);
		this.ids = ids.toArray(new Long[ids.size()]);
	}

	public Long[] getIds() {
		return ids;
	}

}
