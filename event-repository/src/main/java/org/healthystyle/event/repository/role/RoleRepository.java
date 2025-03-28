package org.healthystyle.event.repository.role;

import java.util.List;
import java.util.Set;

import org.healthystyle.event.model.role.Role;
import org.healthystyle.event.model.role.Type;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository {
	@Query("SELECT r FROM Role r WHERE r.type = :type")
	Role findByType(Type type);

	@Query("SELECT r FROM Role r WHERE r.type IN :types")
	List<Role> findByTypes(Set<Type> types);
}
