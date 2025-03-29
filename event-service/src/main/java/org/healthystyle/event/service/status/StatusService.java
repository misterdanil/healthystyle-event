package org.healthystyle.event.service.status;

import org.healthystyle.event.model.status.Status;
import org.healthystyle.event.model.status.StatusType;
import org.healthystyle.util.error.ValidationException;

public interface StatusService {
	Status findByType(StatusType type) throws ValidationException;
}
