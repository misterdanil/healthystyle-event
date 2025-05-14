package org.healthystyle.event.service;

import org.healthystyle.util.user.User;
import org.healthystyle.util.user.UserAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserAccessorImpl implements UserAccessor {

	@Override
	public User getUser() {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();

		return new User(Long.valueOf(name), SecurityContextHolder.getContext().getAuthentication().getAuthorities()
				.stream().map(g -> g.getAuthority()).toList());
	}

}
