package org.healthystyle.event.repository;

import java.util.List;
import java.util.Set;

import org.healthystyle.event.model.UserEvent;
import org.healthystyle.event.model.role.Role;
import org.healthystyle.event.model.role.Type;
import org.healthystyle.event.repository.dto.ParticipateStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEventRepository extends JpaRepository<UserEvent, Long>, CustomUserEventRepository {
	@Query("SELECT ue FROM UserEvent ue WHERE ue.event.id = :eventId ORDER BY ue.createdOn DESC")
	Page<UserEvent> findByEvent(Long eventId, Pageable pageable);

	@Query("SELECT ue FROM UserEvent ue INNER JOIN ue.roles r WHERE r.type = :type ORDER BY ue.createdOn DESC")
	Page<UserEvent> findByRole(Type type, Pageable pageable);

	@Query(value = "SELECT r FROM role r LEFT JOIN user_event_role uer ON uer.role_id = r.id AND uer.user_event_id = :id WHERE uer IS NULL AND r.type IN :roleTypes", nativeQuery = true)
	Set<Role> findNotOwningRoles(Long id, Set<Type> roleTypes);

	@Query("SELECT COUNT(r) FROM UserEvent ue INNER JOIN ue.roles r WHERE ue.id = :id GROUP BY ue.id")
	Integer countRolesById(Long id);

	@Query("SELECT new org.healthystyle.event.repository.dto.ParticipateStatus(u.userId, e.id) FROM Event e INNER JOIN e.users u WHERE u.userId = :userId AND e.id IN :eventIds")
	List<ParticipateStatus> checkStatus(Long userId, Long[] eventIds);

	@Query("SELECT EXISTS (SELECT ue FROM UserEvent ue WHERE ue.userId = :userId AND ue.event.id = :eventId)")
	boolean existsByUserIdAndEvent(Long userId, Long eventId);

	@Query("SELECT EXISTS (SELECT ue FROM UserEvent ue INNER JOIN ue.roles r WHERE ue.event.id = :eventId AND r.type = :type)")
	boolean existsByEventAndRole(Long eventId, Type type);

	@Query("SELECT EXISTS (SELECT ue FROM UserEvent ue INNER JOIN ue.roles r WHERE ue.id = :id AND r.type = :type)")
	boolean hasRole(Long id, Type type);

	@Query(value = "DELETE FROM user_event_role uer INNER JOIN role r ON r.id = uer.role_id WHERE uer.user_event_id = :id AND r.type IN :roleTypes", nativeQuery = true)
	void deleteRolesByTypes(Set<Type> roleTypes, Long id);
}
