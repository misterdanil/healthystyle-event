package org.healthystyle.event.service.dto;

import java.util.Set;

import org.healthystyle.event.model.role.Type;

public class UserEventUpdateRequest {
	private Set<Type> deleteRoleTypes;
	private Set<Type> roleTypes;

	public Set<Type> getRoleTypes() {
		return roleTypes;
	}

	public void setRoleTypes(Set<Type> roleTypes) {
		this.roleTypes = roleTypes;
	}

	public Set<Type> getDeleteRoleTypes() {
		return deleteRoleTypes;
	}

	public void setDeleteRoleTypes(Set<Type> deleteRoleTypes) {
		this.deleteRoleTypes = deleteRoleTypes;
	}

}
