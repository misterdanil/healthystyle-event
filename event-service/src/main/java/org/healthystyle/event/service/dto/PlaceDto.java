package org.healthystyle.event.service.dto;

public class PlaceDto {
	private Long id;
	private String title;
	private String description;
	private Double latitude;
	private Double longitude;

	public PlaceDto() {
		super();
	}

	public PlaceDto(Long id, String title, String description, Double latitude, Double longitude) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
