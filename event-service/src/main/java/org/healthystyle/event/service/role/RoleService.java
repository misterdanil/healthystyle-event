package org.healthystyle.event.service.role;

import java.util.Set;

import org.healthystyle.event.model.role.Role;
import org.healthystyle.event.model.role.Type;

public interface RoleService {
	Role findByType(Type type);

	Set<Role> findByTypes(Set<Type> types);
	
	Set<Role> findByIds(Set<Long> ids);
}
