package org.healthystyle.event.service.role;


import org.healthystyle.event.model.role.Role;
import org.healthystyle.event.model.role.Type;

public interface RoleService {
	Role findByType(Type type);
}
