package org.healthystyle.event.service;

import java.util.List;

import org.healthystyle.event.model.UserEvent;
import org.healthystyle.event.service.dto.UserEventSaveRequest;

public interface UserEventService {
	UserEvent save(UserEventSaveRequest saveRequest, Long eventId);
}
