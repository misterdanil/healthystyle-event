package org.healthystyle.event.service.dto;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UserEventSaveRequest {
	@NotNull(message = "Укажите идентификатор пользователя")
	private Long userId;
	@NotNull(message = "Укажите идентификатор мероприятия")
	private Long eventId;
	@NotEmpty(message = "Укажите хотя бы одну роль")
	private Set<Long> roleIds;

	public UserEventSaveRequest() {
		super();
	}

	public UserEventSaveRequest(Long userId, Long eventId, Set<Long> roleIds) {
		super();
		this.userId = userId;
		this.eventId = eventId;
		this.roleIds = roleIds;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Set<Long> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(Set<Long> roleIds) {
		this.roleIds = roleIds;
	}

}
