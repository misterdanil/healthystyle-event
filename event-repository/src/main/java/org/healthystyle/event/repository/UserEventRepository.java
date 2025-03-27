package org.healthystyle.event.repository;

import org.healthystyle.event.model.UserEvent;
import org.healthystyle.event.model.role.Type;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEventRepository extends JpaRepository<UserEvent, Long> {
	@Query("SELECT ue FROM UserEvent ue WHERE ue.event.id = :eventId ORDER BY ue.createdOn DESC")
	Page<UserEvent> findByEvent(Long eventId, Pageable pageable);

	@Query("SELECT ue FROM UserEvent ue INNER JOIN ue.roles r WHERE r.type = :type ORDER BY ue.createdOn DESC")
	Page<UserEvent> findByRole(Type type, Pageable pageable);
}
