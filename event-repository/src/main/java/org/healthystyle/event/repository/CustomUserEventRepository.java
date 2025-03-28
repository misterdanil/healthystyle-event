package org.healthystyle.event.repository;

import java.util.Set;

import org.healthystyle.event.model.role.Role;

public interface CustomUserEventRepository {
	void addRoles(Set<Role> roles, Long id);
}
