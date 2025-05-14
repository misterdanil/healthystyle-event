package org.healthystyle.event.service.dto;

import java.time.Instant;
import java.util.Set;

import jakarta.validation.constraints.Future;
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
	@NotNull(message = "Укажите время и дату проведения")
	@Future(message = "Дата и время должны быть в будущем времени")
	private Instant appointedTime;
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

	public Instant getAppointedTime() {
		return appointedTime;
	}

	public void setAppointedTime(Instant appointedTime) {
		this.appointedTime = appointedTime;
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
