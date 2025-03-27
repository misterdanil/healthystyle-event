package org.healthystyle.event.service.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public class EventUpdateRequest {
	@NotBlank(message = "Укажите название события")
	private String title;
	@NotBlank(message = "Укажите описание")
	private String description;
	private List<Long> excludedUserIds;
	private List<Long> userIds;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Long> getExcludedUserIds() {
		return excludedUserIds;
	}

	public void setExcludedUserIds(List<Long> excludedUserIds) {
		this.excludedUserIds = excludedUserIds;
	}

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

}
