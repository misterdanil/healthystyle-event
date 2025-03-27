package org.healthystyle.event.service.impl;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Set;

import org.healthystyle.event.model.Event;
import org.healthystyle.event.model.Place;
import org.healthystyle.event.model.role.Type;
import org.healthystyle.event.model.status.Status;
import org.healthystyle.event.model.status.StatusType;
import org.healthystyle.event.repository.EventRepository;
import org.healthystyle.event.service.EventService;
import org.healthystyle.event.service.PlaceService;
import org.healthystyle.event.service.UserEventService;
import org.healthystyle.event.service.cache.UserRepository;
import org.healthystyle.event.service.cache.dto.User;
import org.healthystyle.event.service.dto.EventSaveRequest;
import org.healthystyle.event.service.dto.EventUpdateRequest;
import org.healthystyle.event.service.error.EventNotFoundException;
import org.healthystyle.event.service.role.RoleService;
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
	private RoleService roleService;
	@Autowired
	private StatusService statusService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserAccessor userAccessor;
	@Autowired
	private UserEventService userEventService;

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
	public Page<Event> findNearestByCoordinates(Double latitude, Double longitude, int page, int limit)
			throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_NEAREST_BY_COORDINATES_PARAM_NAMES, latitude, longitude,
				page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "event");
		LOG.debug("Validating params: {}", params);
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

		Page<Event> events = repository.findNearestByCoordinates(latitude, longitude, PageRequest.of(page, limit));
		LOG.info("Got events successfully by params: {}", params);

		return events;
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
	public Event save(EventSaveRequest saveRequest) {
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
		
		Place place = placeService.save(saveRequest.getPlace());
		Status status = statusService.findByType(StatusType.PENDING);
		
		Event event = new Event(saveRequest.getTitle(), saveRequest.getDescription(), place, status);
		event = repository.save(event);
		
		Set<Long> userIds = saveRequest.getUserIds();
		LOG.debug("Checking users for existence by ids '{}'", userIds);
		Iterable<User> users = userRepository.findAllById(userIds);
		Set<Long> foundedIds = new HashSet<>();
		users.forEach(u -> foundedIds.add(u.getId()));
		userIds.removeAll(foundedIds);
		if (!userIds.isEmpty()) {
			result.reject("event.save.user.not_found", "Не удалось найти пользователей по идентификаторам: " + userIds);
			throw new UserNotFoundException(result, userIds);
		}
		
		Role ownerRole = roleService.findByType(Type.OWNER);
		Role memberRole = roleService.findByType(Type.MEMBER);
		
		for (Long foundedId : foundedIds) {
			UserEventSaveRequest userEventSaveRequest = new UserEventSaveRequest(foundedId, event.getId(),);
		}

		saveRequest.getUserIds();
	}

	@Override
	public void update(EventUpdateRequest updateRequest, Long id) {
		// TODO Auto-generated method stub

	}

}
