package org.healthystyle.event.repository.status;

import org.healthystyle.event.model.status.Status;
import org.healthystyle.event.model.status.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
	@Query("SELECT s from Status s WHERE s.type = :type")
	Status findByType(StatusType type);
}
