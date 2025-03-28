package org.healthystyle.event.service.dto;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UserEventSaveRequest {
	@NotNull(message = "Укажите идентификатор пользователя")
	private Long userId;
	@NotEmpty(message = "Укажите хотя бы одну роль")
	private Set<Long> roleIds;

	public UserEventSaveRequest() {
		super();
	}

	public UserEventSaveRequest(Long userId, Long... roleIds) {
		super();
		this.userId = userId;
		this.roleIds = new HashSet<>(Arrays.asList(roleIds));
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Set<Long> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(Set<Long> roleIds) {
		this.roleIds = roleIds;
	}

}
