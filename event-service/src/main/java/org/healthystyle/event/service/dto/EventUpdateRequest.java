package org.healthystyle.event.service.dto;

import jakarta.validation.constraints.NotBlank;

public class EventUpdateRequest {
	@NotBlank(message = "Укажите название события")
	private String title;
	@NotBlank(message = "Укажите описание")
	private String description;

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
}
