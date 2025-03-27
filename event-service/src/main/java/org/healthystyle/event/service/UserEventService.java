package org.healthystyle.event.service;

import java.util.List;

import org.healthystyle.event.model.UserEvent;

public interface UserEventService {
	UserEvent save(Long userId, List<Long> roles, Long eventId);
}
