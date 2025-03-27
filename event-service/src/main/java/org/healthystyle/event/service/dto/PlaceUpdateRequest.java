package org.healthystyle.event.service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PlaceUpdateRequest {
	@NotBlank(message = "Укажите название места")
	private String title;
	private String description;
	@NotNull(message = "Укажите широту")
	@Min(value = -90, message = "Широта не может быть меньше -90 градусов")
	@Max(value = 90, message = "Широта не может быть больше 90 градусов")
	private Double latitude;
	@NotNull(message = "Укажите долготу")
	@Min(value = -180, message = "Долгота не может быть меньше -180 градусов")
	@Max(value = 180, message = "Долгота не может быть больше 180 градусов")
	private Double longitude;

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

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

}
