package org.healthystyle.event.service.dto;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.healthystyle.event.model.role.Type;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UserEventSaveRequest {
	@NotNull(message = "Укажите идентификатор пользователя")
	private Long userId;
	@NotEmpty(message = "Укажите хотя бы одну роль")
	private Set<Type> roleTypes;

	public UserEventSaveRequest() {
		super();
	}

	public UserEventSaveRequest(Long userId, Type... roleTypes) {
		super();
		this.userId = userId;
		this.roleTypes = new HashSet<>(Arrays.asList(roleTypes));
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Set<Type> getRoleTypes() {
		return roleTypes;
	}

	public void setRoleTypes(Set<Type> roleTypes) {
		this.roleTypes = roleTypes;
	}

}
