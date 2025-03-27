package org.healthystyle.event.service.status;

import org.healthystyle.event.model.status.Status;
import org.healthystyle.event.model.status.StatusType;
import org.springframework.data.jpa.repository.Query;

public interface StatusService {
	@Query("SELECT s FROM Status s WHERE s.type = :type")
	Status findByType(StatusType type);
}
