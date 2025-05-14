package org.healthystyle.event.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.healthystyle.event.model.Event;
import org.healthystyle.event.model.UserEvent;
import org.healthystyle.event.model.role.Role;
import org.healthystyle.event.model.role.Type;
import org.healthystyle.event.repository.UserEventRepository;
import org.healthystyle.event.repository.dto.ParticipateStatus;
import org.healthystyle.event.service.EventService;
import org.healthystyle.event.service.UserEventService;
import org.healthystyle.event.service.dto.UserEventSaveRequest;
import org.healthystyle.event.service.dto.UserEventUpdateRequest;
import org.healthystyle.event.service.error.event.EventNotFoundException;
import org.healthystyle.event.service.error.event.NoRolesException;
import org.healthystyle.event.service.error.event.OwnerDeletionException;
import org.healthystyle.event.service.error.event.RoleUnacceptableException;
import org.healthystyle.event.service.error.event.UserEventExistException;
import org.healthystyle.event.service.error.event.UserEventNotFoundException;
import org.healthystyle.event.service.error.event.UserNotFoundException;
import org.healthystyle.event.service.role.RoleService;
import org.healthystyle.util.error.ValidationException;
import org.healthystyle.util.log.LogTemplate;
import org.healthystyle.util.user.User;
import org.healthystyle.util.user.UserAccessor;
import org.healthystyle.util.validation.ParamsChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;

@Service
public class UserEventServiceImpl implements UserEventService {
	@Autowired
	private UserEventRepository repository;
	@Autowired
	private Validator validator;
	@Autowired
	private EventService eventService;
	@Autowired
	private UserAccessor userAccessor;
	@Autowired
	private RoleService roleService;

	private static final Integer MAX_SIZE = 25;

	private static final Logger LOG = LoggerFactory.getLogger(UserEventServiceImpl.class);

	@Override
	public UserEvent findById(Long id) throws ValidationException, UserEventNotFoundException {
		BindingResult result = new MapBindingResult(new HashMap<>(), "userEvent");

		LOG.debug("Checking id for not null: {}", id);
		if (id == null) {
			result.reject("user_event.find.id.not_null", "Укажите идентификатор участника события для поиска");
			throw new ValidationException("The id is null", result);
		}

		LOG.debug("Checking user event for existence by id '{}'", id);
		Optional<UserEvent> userEvent = repository.findById(id);
		if (userEvent.isEmpty()) {
			result.reject("user_event.find.not_found", "Не удалось найти участника мероприятия");
			throw new UserEventNotFoundException(id, result);
		}
		LOG.info("Got user event successfully by id '{}'", id);

		return userEvent.get();
	}

	@Override
	public Page<UserEvent> findByEvent(Long eventId, int page, int limit) throws ValidationException {
		String params = LogTemplate.getParamsTemplate(FIND_BY_EVENT_PARAM_NAMES, eventId, page, limit);

		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "userEvent");
		LOG.debug("Validating params: {}", params);
		if (eventId == null) {
			result.reject("user_event.find.event_id.not_null", "Укажите идентификатор участника события для поиска");
		}
		ParamsChecker.checkPageNumber(page, result);
		ParamsChecker.checkLimit(limit, MAX_SIZE, result);
		if (result.hasErrors()) {
			throw new ValidationException("The params are invalid: %s. Result: %s", result, params, result);
		}

		Page<UserEvent> userEvents = repository.findByEvent(eventId, PageRequest.of(page, limit));
		LOG.info("Got members by params '{}' successfully", params);

		return userEvents;
	}

	@Override
	public List<ParticipateStatus> checkStatus(Long userId, Long[] eventIds) {
		return repository.checkStatus(userId, eventIds);
	}

	@Override
	public UserEvent save(UserEventSaveRequest saveRequest, Long eventId) throws ValidationException,
			EventNotFoundException, UserNotFoundException, UserEventExistException, RoleUnacceptableException {
		LOG.debug("Validating member: {}", saveRequest);
		BindingResult result = new BeanPropertyBindingResult(saveRequest, "userEvent");
		validator.validate(saveRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException("The event is invalid: %s. Result: %s", result, saveRequest, result);
		}

		Event event = eventService.findById(eventId);

		LOG.debug("The member is OK: {}", saveRequest);

		Long userId = saveRequest.getUserId();
//		if (!userService.existsById(userId)) {
//			result.reject("user_event.save.user_id.not_exists",
//					"Не удалось найти пользователя для добавления в мероприятие");
//			throw new UserNotFoundException(result, userId);
//		}

		if (repository.existsByUserIdAndEvent(userId, eventId)) {
			result.reject("user_event.save.exists", "Участник уже присутствует в собрании");
			throw new UserEventExistException(userId, eventId, result);
		}

		Set<Type> roleTypes = saveRequest.getRoleTypes();
		LOG.debug("Checking owner for existence");
		if (roleTypes.contains(Type.OWNER) && repository.existsByEventAndRole(eventId, Type.OWNER)) {
			result.reject("user_event.save.owner.exists", "Нельзя добавить больше одного владельца");
			throw new RoleUnacceptableException("Owner has already existed", Type.OWNER, result);
		}
		LOG.debug("Getting roles: {}", roleTypes);
		Set<Role> roles = roleService.findByTypes(roleTypes);

		LOG.debug("The user event is OK: {}", saveRequest);

		UserEvent userEvent = new UserEvent(userId, event, roles);
		userEvent = repository.save(userEvent);
		LOG.info("User event was saved successfully: {}", saveRequest);

		return userEvent;
	}

	@Override
	public UserEvent join(Long eventId) throws ValidationException, EventNotFoundException, UserEventExistException,
			UserNotFoundException, RoleUnacceptableException {
		BindingResult result = new MapBindingResult(new LinkedHashMap<>(), "event");
		if (eventId == null) {
			result.reject("user_event.join.event_id.not_null", "Укажите идентификатор мероприятия для присоединения");
			throw new ValidationException("Event id is null", result);
		}

		Event event = eventService.findById(eventId);
		User user = userAccessor.getUser();

		UserEvent userEvent = save(new UserEventSaveRequest(user.getId(), Type.MEMBER), eventId);

		return userEvent;
	}

	@Override
	@Transactional
	public void update(UserEventUpdateRequest updateRequest, Long id) throws ValidationException,
			UserEventNotFoundException, NoRolesException, RoleUnacceptableException, OwnerDeletionException {
		LOG.debug("Validating user event: {}", updateRequest);
		BindingResult result = new BeanPropertyBindingResult(new LinkedHashMap<>(), "userEvent");
		validator.validate(updateRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException("User event is invalid: %s. Result: %s", result, updateRequest, result);
		}

		if (!repository.existsById(id)) {
			result.reject("user_event.update.not_found", "Не удалось найти участника");
			throw new UserEventNotFoundException(id, result);
		}

		Set<Type> deleteRoleTypes = updateRequest.getDeleteRoleTypes();
		if (deleteRoleTypes != null && !deleteRoleTypes.isEmpty()) {
			if (deleteRoleTypes.contains(Type.OWNER)) {
				result.reject("user_event.update.delete_role.owner", "Нельзя удалять роль владельца");
				throw new OwnerDeletionException(id, result);
			}
			LOG.debug("Delete roles '{}' of user event '{}'", deleteRoleTypes, id);
			repository.deleteRolesByTypes(deleteRoleTypes, id);
		}

		Set<Type> roleTypes = updateRequest.getRoleTypes();
		if (roleTypes != null && !roleTypes.isEmpty()) {
			LOG.debug("Checking roles for owner: {}", roleTypes);
			if (roleTypes.contains(Type.OWNER)) {
				result.reject("user_event.update.delete_role.owner", "Нельзя добавить роль владельца");
				throw new RoleUnacceptableException("It's impossible to add owner role. It has already existed",
						Type.OWNER, result);
			}
			Set<Role> roles = repository.findNotOwningRoles(id, roleTypes);
			if (roles.size() > 0) {
				LOG.debug("Adding roles '{}' to user event '{}'", roles, id);
				repository.addRoles(roles, id);
			}
		}

		LOG.debug("Checking roles count of user event '{}'", id);
		if (repository.countRolesById(id) == 0) {
			result.reject("user_event.update.no_roles", "Не осталось ни одной роли");
			throw new NoRolesException(id, result);
		}

		LOG.info("User event '{}' was updated successfully: {}", id, updateRequest);
	}

	@Override
	public void deleteById(Long id) throws ValidationException, UserEventNotFoundException, OwnerDeletionException {
		BindingResult result = new MapBindingResult(new HashMap<>(), "userEvent");

		LOG.debug("Checking id for not null");
		if (id == null) {
			result.reject("user_event.delete.id.not_null", "Укажите идентификатор для удаления");
			throw new ValidationException("The id is null", result);
		}

		UserEvent userEvent = findById(id);

		LOG.debug("Checking member is not owner: {}", id);
		if (repository.hasRole(id, Type.OWNER)) {
			result.reject("user_event.delete.not_acceptable", "Нельзя удалять владельца");
			throw new OwnerDeletionException(id, result);
		}

		repository.delete(userEvent);
		LOG.info("The member was deleted successfully: {}", id);

	}

}
