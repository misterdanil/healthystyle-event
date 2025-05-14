package org.healthystyle.event.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.healthystyle.event.model.Event;
import org.healthystyle.event.model.Place;
import org.healthystyle.event.model.UserEvent;
import org.healthystyle.event.model.role.Type;
import org.healthystyle.event.model.status.Status;
import org.healthystyle.event.model.status.StatusType;
import org.healthystyle.event.repository.EventRepository;
import org.healthystyle.event.service.EventService;
import org.healthystyle.event.service.PlaceService;
import org.healthystyle.event.service.UserEventService;
import org.healthystyle.event.service.dto.DistanceEvent;
import org.healthystyle.event.service.dto.EventDto;
import org.healthystyle.event.service.dto.EventSaveRequest;
import org.healthystyle.event.service.dto.EventUpdateRequest;
import org.healthystyle.event.service.dto.UserEventSaveRequest;
import org.healthystyle.event.service.dto.mapper.EventMapper;
import org.healthystyle.event.service.error.event.EventNotFoundException;
import org.healthystyle.event.service.error.event.RoleUnacceptableException;
import org.healthystyle.event.service.error.event.UserEventExistException;
import org.healthystyle.event.service.error.event.UserNotFoundException;
import org.healthystyle.event.service.notifier.EventNotifier;
import org.healthystyle.event.service.status.StatusService;
import org.healthystyle.util.error.ValidationException;
import org.healthystyle.util.log.LogTemplate;
import org.healthystyle.util.user.UserAccessor;
import org.healthystyle.util.validation.ParamsChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;

@Service
public class EventServiceImpl implements EventService {
	@Autowired
	private EventRepository repository;
	@Autowired
	private Validator validator;
	@Autowired
	private PlaceService placeService;
	@Autowired
	private StatusService statusService;
	@Autowired
	private UserAccessor userAccessor;
	@Autowired
	private UserEventService userEventService;
	@Autowired
	private EventMapper mapper;
	@Autowired
	private EventNotifier notifier;
	private static final Integer MAX_SIZE = 25;

	private static final Logger LOG = LoggerFactory.getLogger(EventServiceImpl.class);

	@Override
	public Event findById(Long id) throws ValidationException, EventNotFoundException {
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "event");

		LOG.debug("Checking id for not null");
		if (id == null) {
			result.reject("event.find.id.not_null", "Укажите идентификатор события для поиска");
			throw new ValidationException("The id is null", result);
		}

		Optional<Event> event = repository.findById(id);
		LOG.debug("Checking event for existence by id '{}'", id);
		if (event.isEmpty()) {
			result.reject("event.find.not_found", "Не удалось найти событие");
			throw new EventNotFoundException(id, result);
		}
		LOG.info("Got event by id '{}' successfully", id);

		return event.get();
	}

	@Override
	public Page<Event> findByTitle(String title, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_TITLE_PARAM_NAMES, title, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "event");
		LOG.debug("Validating params: {}", params);
		if (title == null || title.isBlank()) {
			result.reject("event.find.title.not_empty", "Укажите название события для поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		LOG.debug("The params are OK: {}", params);

		Page<Event> events = repository.findByTitle(title, PageRequest.of(page, limit));
		LOG.info("Got events successfully by params: {}", params);

		return events;
	}

	@Override
	public List<DistanceEvent> findNearestByCoordinates(String title, Double latitude, Double longitude, int page,
			int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_NEAREST_BY_COORDINATES_PARAM_NAMES, title, latitude,
				longitude, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "event");
		LOG.debug("Validating params: {}", params);
		if (title == null) {
			result.reject("event.find.title.not_null", "Укажите заголовок для поиска");
		}
		if (latitude == null) {
			result.reject("event.find.latitude.not_null", "Укажите широту для поиска");
		}
		if (longitude == null) {
			result.reject("event.find.longitude.not_null", "Укажите долготу для поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		LOG.debug("The params are OK: {}", params);

		Page<Object[][]> events = repository.findNearestByCoordinatesHaversine(title, latitude, longitude,
				PageRequest.of(page, limit));
		LOG.info("Got events successfully by params: {}", params);

		List<DistanceEvent> distances = new ArrayList<>();

		for (Object[] e : events.getContent()) {

			Event event = (Event) e[0];
			DistanceEvent distanceEvent = mapper.toDistanceEvent(event);
			distanceEvent.setDistance((Double) e[1]);
			
			distances.add(distanceEvent);
		}

		return distances;
	}

	@Override
	public Page<Event> find(int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_PARAM_NAMES, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "event");
		LOG.debug("Validating params: {}", params);
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		LOG.debug("The params are OK: {}", params);

		Page<Event> events = repository.find(PageRequest.of(page, limit));
		LOG.info("Got events successfully by params: {}", params);

		return events;
	}

	@Override
	public Page<Event> findByMember(Long userId, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_MEMBER_PARAM_NAMES, userId, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "event");
		LOG.debug("Validating params: {}", params);
		if (userId == null) {
			result.reject("event.find.user_id.not_null", "Укажите идентификатор пользователя для поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		LOG.debug("The params are OK: {}", params);

		Page<Event> events = repository.findByMember(userId, PageRequest.of(page, limit));
		LOG.info("Got events successfully by params: {}", params);

		return events;
	}

	@Override
	public Event save(EventSaveRequest saveRequest) throws ValidationException, UserNotFoundException,
			EventNotFoundException, UserEventExistException, RoleUnacceptableException {
		LOG.debug("Validating event: {}", saveRequest);
		BindingResult result = new BeanPropertyBindingResult(saveRequest, "event");
		validator.validate(saveRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException("The event is invalid: %s. Result: %s", result, saveRequest, result);
		}

		String description = saveRequest.getDescription();
		LOG.debug("Checking description for not blank: {}", description);
		if (description != null && description.isBlank()) {
			description = null;
		}
		String eventType = saveRequest.getEventType();
		LOG.debug("Checking event type for not blank: {}", eventType);
		if (eventType != null && eventType.isBlank()) {
			eventType = null;
		}

		Place place = placeService.save(saveRequest.getPlace());
		Status status = statusService.findByType(StatusType.PENDING);

		LOG.debug("The event is OK: {}", saveRequest);
		Event event = new Event(saveRequest.getTitle(), saveRequest.getDescription(), place, saveRequest.getAppointedTime(), status);
		event.setEventType(eventType);
		event = repository.save(event);

//		Set<Long> userIds = saveRequest.getUserIds();
//		LOG.debug("Checking users for existence by ids '{}'", userIds);
//		List<User> users = userService.findAllById(userIds);
//		if (users.size() < userIds.size()) {
//			userIds.removeAll(users.stream().map(u -> u.getId()).toList());
//			result.reject("event.save.user.not_found", "Не удалось найти пользователей по идентификаторам: " + userIds);
//			throw new UserNotFoundException(result, userIds);
//		}

		LOG.debug("Getting owner");
		org.healthystyle.util.user.User owner = userAccessor.getUser();
		UserEventSaveRequest ownerSaveRequest = new UserEventSaveRequest(owner.getId(), Type.OWNER, Type.MEMBER);
		LOG.debug("Saving owner: {}", ownerSaveRequest);
		userEventService.save(ownerSaveRequest, event.getId());

		Set<Long> userIds = saveRequest.getUserIds();
		LOG.debug("Saving users by ids '{}'", userIds);
		for (Long userId : userIds) {
			LOG.debug("Saving user by id '{}'", userId);
			UserEventSaveRequest userEventSaveRequest = new UserEventSaveRequest(userId, Type.MEMBER);
			UserEvent userEvent = userEventService.save(userEventSaveRequest, event.getId());
			event.addUser(userEvent);
		}

		LOG.debug("The event and its data was saved successfully. Preparing data for sending");

		EventDto dto = mapper.toDto(event);
		dto.setEventType(saveRequest.getEventType());
		dto.setBody(saveRequest.getBody());
		notifier.notifyCreated(dto);

		return event;
	}

	@Override
	public void update(EventUpdateRequest updateRequest, Long id) throws ValidationException, EventNotFoundException {
		LOG.debug("Validating event: {}", updateRequest);
		BindingResult result = new BeanPropertyBindingResult(updateRequest, "event");
		validator.validate(updateRequest, result);
		LOG.debug("Checking id for not null");
		if (id == null) {
			result.reject("event.update.id.not_null", "Укажите идентификатор события для обновления");
		}
		if (result.hasErrors()) {
			throw new ValidationException("The event is invalid: %s. Result: %s", result, updateRequest, result);
		}

		LOG.debug("Checking event for existence by id '{}'", id);
		Event event = findById(id);

		LOG.debug("The event is OK: {}", updateRequest);

		String title = updateRequest.getTitle();
		String oldTitle = event.getTitle();
		if (!title.equals(oldTitle)) {
			LOG.debug("Setting title from '{}' to '{}'", oldTitle, title);
			event.setTitle(title);
		}

		String description = updateRequest.getDescription();
		String oldDescription = event.getDescription();
		if (!description.equals(oldDescription)) {
			LOG.debug("Setting description from '{}' to '{}'", oldDescription, description);
			event.setDescription(description);
		}

		event = repository.save(event);
		LOG.info("The event was updated successfully");
	}

	@Override
	public void deleteById(Long id) throws ValidationException, EventNotFoundException {
		BindingResult result = new MapBindingResult(new HashMap<>(), "event");

		LOG.debug("Checking id for not null: {}", id);
		if (id == null) {
			result.reject("event.delete.id.not_null", "Укажите идентификатор события для удаления");
			throw new ValidationException("The id is null", result);
		}

		LOG.debug("Checking event for existence by id '{}'", id);
		Event event = findById(id);

		LOG.debug("The data is OK: {}. Deleting event");
		repository.delete(event);

		LOG.debug("The event was deleted successfully. Preparing data for sending");
		EventDto dto = mapper.toDto(event);
		notifier.notifyDeleted(dto);
	}

	@Override
	public void changeStatus(StatusType type, Long id) throws ValidationException, EventNotFoundException {
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "event");

		LOG.debug("Checking type is not null: {}", type);
		if (type == null) {
			result.reject("event.update.status.type.not_null", "Укажите статус");
			throw new ValidationException("Status type is null", result);
		}

		Status status = statusService.findByType(type);

		Event event = findById(id);

		event.setStatus(status);

		repository.save(event);
		LOG.info("Status '{}' of event '{}' has been changed successfully", type, id);
	}

}
