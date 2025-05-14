package org.healthystyle.event.service;

import java.util.List;

import org.healthystyle.event.model.UserEvent;
import org.healthystyle.event.repository.dto.ParticipateStatus;
import org.healthystyle.event.service.dto.UserEventSaveRequest;
import org.healthystyle.event.service.dto.UserEventUpdateRequest;
import org.healthystyle.event.service.error.event.EventNotFoundException;
import org.healthystyle.event.service.error.event.NoRolesException;
import org.healthystyle.event.service.error.event.OwnerDeletionException;
import org.healthystyle.event.service.error.event.RoleUnacceptableException;
import org.healthystyle.event.service.error.event.UserEventExistException;
import org.healthystyle.event.service.error.event.UserEventNotFoundException;
import org.healthystyle.event.service.error.event.UserNotFoundException;
import org.healthystyle.util.error.ValidationException;
import org.healthystyle.util.log.MethodNameHelper;
import org.springframework.data.domain.Page;

public interface UserEventService {
	static final String[] FIND_BY_EVENT_PARAM_NAMES = MethodNameHelper.getMethodParamNames(UserEventService.class,
			"findByEvent", Long.class, int.class, int.class);

	UserEvent findById(Long id) throws ValidationException, UserEventNotFoundException;

	Page<UserEvent> findByEvent(Long eventId, int page, int limit) throws ValidationException;

	List<ParticipateStatus> checkStatus(Long userId, Long[] eventIds);

	UserEvent save(UserEventSaveRequest saveRequest, Long eventId) throws ValidationException, EventNotFoundException,
			UserNotFoundException, UserEventExistException, RoleUnacceptableException;

	UserEvent join(Long eventId) throws ValidationException, EventNotFoundException, UserEventExistException,
			UserNotFoundException, RoleUnacceptableException;

	void update(UserEventUpdateRequest updateRequest, Long id) throws ValidationException, UserEventNotFoundException,
			NoRolesException, RoleUnacceptableException, OwnerDeletionException;

	void deleteById(Long id) throws ValidationException, UserEventNotFoundException, OwnerDeletionException;
}
