package event;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.healthystyle.event.app.Main;
import org.healthystyle.event.model.Event;
import org.healthystyle.event.model.Place;
import org.healthystyle.event.model.UserEvent;
import org.healthystyle.event.model.role.Role;
import org.healthystyle.event.model.role.Type;
import org.healthystyle.event.model.status.Status;
import org.healthystyle.event.model.status.StatusType;
import org.healthystyle.event.repository.EventRepository;
import org.healthystyle.event.repository.PlaceRepository;
import org.healthystyle.event.repository.UserEventRepository;
import org.healthystyle.event.service.EventService;
import org.healthystyle.event.service.dto.EventSaveRequest;
import org.healthystyle.event.service.dto.PlaceSaveRequest;
import org.healthystyle.event.service.dto.UserEventSaveRequest;
import org.healthystyle.event.service.error.event.EventNotFoundException;
import org.healthystyle.event.service.error.event.RoleUnacceptableException;
import org.healthystyle.event.service.error.event.UserEventExistException;
import org.healthystyle.event.service.error.event.UserNotFoundException;
import org.healthystyle.util.error.ValidationException;
import org.healthystyle.util.user.User;
import org.healthystyle.util.user.UserAccessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = Main.class)
public class EventTest {
	private Event event;
	private EventSaveRequest eventSaveRequest;

	private Place place;
	private PlaceSaveRequest placeSaveRequest;

	private Status status;

	private UserEvent userEvent;
	private UserEventSaveRequest userEventSaveRequest;

	private Role role;

	@Autowired
	private EventService eventService;
	@MockitoBean
	private EventRepository eventRepository;

	@MockitoBean
	private PlaceRepository placeRepository;

	@MockitoBean
	private UserEventRepository userEventRepository;

	@MockitoBean
	private UserAccessor accessor;

	private User user;
	private User member;

	@BeforeEach
	public void setup() {
		status = new Status(StatusType.ACCEPTED);

		List<String> roles = new ArrayList<>();
		roles.add("USER");
		user = new User(1L, roles);
		member = new User(2L, roles);

		role = new Role();
		role.setType(Type.OWNER);

		place = new Place("Исторический сквер", "Около памятника Петра 1", 54.32, 55.33);
		placeSaveRequest = new PlaceSaveRequest();
		placeSaveRequest.setTitle(place.getTitle());
		placeSaveRequest.setDescription(place.getDescription());
		placeSaveRequest.setLatitude(place.getLatitude());
		placeSaveRequest.setLatitude(place.getLongitude());

		event = new Event("Утренняя пробежка", "Стимулирует кардио", place, Instant.now(), status);
		eventSaveRequest = new EventSaveRequest();
		eventSaveRequest.setTitle(event.getTitle());
		eventSaveRequest.setAppointedTime(event.getAppointedTime());
		eventSaveRequest.setDescription(event.getDescription());
		eventSaveRequest.setPlace(placeSaveRequest);
		Set<Long> ids = new HashSet<>();
		ids.add(2L);
		eventSaveRequest.setUserIds(ids);

		Set<Role> rolesE = new HashSet<>();
		Set<Type> types = new HashSet<>();
		types.add(Type.MEMBER);
		rolesE.add(role);
		userEvent = new UserEvent(1L, event, rolesE);
		userEventSaveRequest = new UserEventSaveRequest();
		userEventSaveRequest.setRoleTypes(types);
	}

	@Test
	public void createEventTest() throws ValidationException, UserNotFoundException, EventNotFoundException,
			UserEventExistException, RoleUnacceptableException {
		when(accessor.getUser()).thenReturn(user);
		when(eventRepository.save(any(Event.class))).thenReturn(event);
		when(userEventRepository.save(any(UserEvent.class))).thenReturn(userEvent);
		when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));

		when(userEventRepository.existsByEventAndRole(event.getId(), Type.OWNER)).thenReturn(false);
		when(userEventRepository.existsByUserIdAndEvent(user.getId(), event.getId())).thenReturn(false);

		eventService.save(eventSaveRequest);
		verify(eventRepository, times(1)).save(any(Event.class));
	}

	@Test
	public void createEventPlaceCoordsValidationTest() throws ValidationException, UserNotFoundException,
			EventNotFoundException, UserEventExistException, RoleUnacceptableException {
		placeSaveRequest.setLatitude(-360.0);

		when(accessor.getUser()).thenReturn(user);
		when(eventRepository.save(any(Event.class))).thenReturn(event);
		when(userEventRepository.save(any(UserEvent.class))).thenReturn(userEvent);
		when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));

		when(userEventRepository.existsByEventAndRole(event.getId(), Type.OWNER)).thenReturn(false);
		when(userEventRepository.existsByUserIdAndEvent(user.getId(), event.getId())).thenReturn(false);

		assertThrows(ValidationException.class, () -> {
			eventService.save(eventSaveRequest);
		});
	}

	@Test
	public void createEventMembersEmptyTest() throws ValidationException, UserNotFoundException, EventNotFoundException,
			UserEventExistException, RoleUnacceptableException {
		eventSaveRequest.setUserIds(Collections.EMPTY_SET);

		when(accessor.getUser()).thenReturn(user);
		when(eventRepository.save(any(Event.class))).thenReturn(event);
		when(userEventRepository.save(any(UserEvent.class))).thenReturn(userEvent);
		when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));

		when(userEventRepository.existsByEventAndRole(event.getId(), Type.OWNER)).thenReturn(false);
		when(userEventRepository.existsByUserIdAndEvent(user.getId(), event.getId())).thenReturn(false);

		eventService.save(eventSaveRequest);
		verify(eventRepository, times(1)).save(any(Event.class));
	}

	@Test
	public void createUserEventRoleOwnerDuplicateTest() throws ValidationException, UserNotFoundException,
			EventNotFoundException, UserEventExistException, RoleUnacceptableException {
		when(accessor.getUser()).thenReturn(user);
		when(eventRepository.save(any(Event.class))).thenReturn(event);
		when(userEventRepository.save(any(UserEvent.class))).thenReturn(userEvent);

		when(userEventRepository.existsByEventAndRole(event.getId(), Type.OWNER)).thenReturn(true);

		assertThrows(RoleUnacceptableException.class, () -> {
			eventService.save(eventSaveRequest);
		});
	}

	@Test
	public void fetchEventsTest() throws ValidationException, UserNotFoundException, EventNotFoundException,
			UserEventExistException, RoleUnacceptableException {
		when(accessor.getUser()).thenReturn(user);
		when(eventRepository.findByTitle(any(String.class), any(PageRequest.class)))
				.thenReturn(new PageImpl<>(List.of(event)));

		eventService.findByTitle("Исторический сквер", 1, 25);
		verify(eventRepository, times(1)).findByTitle(any(String.class), any(PageRequest.class));
	}

	@Test
	public void fetchEventsPageIncorrectTest() throws ValidationException, UserNotFoundException,
			EventNotFoundException, UserEventExistException, RoleUnacceptableException {
		when(accessor.getUser()).thenReturn(user);

		assertThrows(ValidationException.class, () -> {
			eventService.findByTitle("Исторический сквер", -5, 25);
		});
		verify(eventRepository, times(0)).findByTitle(any(String.class), any(PageRequest.class));
	}
	
	@Test
	public void fetchEventsMaxLimitTooMuchTest() throws ValidationException, UserNotFoundException,
			EventNotFoundException, UserEventExistException, RoleUnacceptableException {
		when(accessor.getUser()).thenReturn(user);

		assertThrows(ValidationException.class, () -> {
			eventService.findByTitle("Исторический сквер", 1, 2500);
		});
		verify(eventRepository, times(0)).findByTitle(any(String.class), any(PageRequest.class));
	}
}
