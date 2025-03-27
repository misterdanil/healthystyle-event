package org.healthystyle.event.service.role;

import javax.management.relation.Role;

public interface RoleService {
	Role findByType(Type type);
}
