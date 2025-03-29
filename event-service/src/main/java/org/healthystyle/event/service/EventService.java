package org.healthystyle.event.service;

import org.healthystyle.event.model.Event;
import org.healthystyle.event.model.status.StatusType;
import org.healthystyle.event.service.dto.EventSaveRequest;
import org.healthystyle.event.service.dto.EventUpdateRequest;
import org.healthystyle.event.service.error.event.EventNotFoundException;
import org.healthystyle.event.service.error.event.RoleUnacceptableException;
import org.healthystyle.event.service.error.event.UserEventExistException;
import org.healthystyle.event.service.error.event.UserNotFoundException;
import org.healthystyle.util.error.ValidationException;
import org.healthystyle.util.log.MethodNameHelper;
import org.springframework.data.domain.Page;

public interface EventService {
	static final String[] FIND_BY_TITLE_PARAM_NAMES = MethodNameHelper.getMethodParamNames(EventService.class,
			"findByTitle", String.class, int.class, int.class);

	static final String[] FIND_NEAREST_BY_COORDINATES_PARAM_NAMES = MethodNameHelper.getMethodParamNames(
			EventService.class, "findNearestByCoordinates", Double.class, Double.class, int.class, int.class);

	static final String[] FIND_PARAM_NAMES = MethodNameHelper.getMethodParamNames(EventService.class, "find", int.class,
			int.class);

	static final String[] FIND_BY_MEMBER_PARAM_NAMES = MethodNameHelper.getMethodParamNames(EventService.class,
			"findByMember", Long.class, int.class, int.class);

	Event findById(Long id) throws ValidationException, EventNotFoundException;

	Page<Event> findByTitle(String title, int page, int limit) throws ValidationException;

	Page<Event> findNearestByCoordinates(Double latitude, Double longitude, int page, int limit)
			throws ValidationException;

	Page<Event> find(int page, int limit) throws ValidationException;

	Page<Event> findByMember(Long userId, int page, int limit) throws ValidationException;

	Event save(EventSaveRequest saveRequest) throws ValidationException, UserNotFoundException, EventNotFoundException,
			UserEventExistException, RoleUnacceptableException;

	void update(EventUpdateRequest updateRequest, Long id) throws ValidationException, EventNotFoundException;

	void deleteById(Long id) throws ValidationException, EventNotFoundException;

	void changeStatus(StatusType type, Long id) throws ValidationException, EventNotFoundException;
}
