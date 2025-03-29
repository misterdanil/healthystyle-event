package org.healthystyle.event.service.role;

import java.util.Set;

import org.healthystyle.event.model.role.Role;
import org.healthystyle.event.model.role.Type;
import org.healthystyle.util.error.ValidationException;

public interface RoleService {
	Role findByType(Type type) throws ValidationException;

	Set<Role> findByTypes(Set<Type> types);
}
