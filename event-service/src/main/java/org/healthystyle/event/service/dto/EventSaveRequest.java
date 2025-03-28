package org.healthystyle.event.service.dto;

import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class EventSaveRequest {
	@NotBlank(message = "Укажите название события")
	private String title;
	@NotBlank(message = "Укажите описание")
	private String description;
	@NotNull(message = "Укажите место проведения")
	private PlaceSaveRequest place;
	@NotEmpty(message = "Укажите участников события")
	private Set<Long> userIds;
	private String eventType;
	private String body;

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

	public PlaceSaveRequest getPlace() {
		return place;
	}

	public void setPlace(PlaceSaveRequest place) {
		this.place = place;
	}

	public Set<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(Set<Long> userIds) {
		this.userIds = userIds;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
