package org.healthystyle.event.service.dto;

public class EventDto {
	private Long id;
	private String title;
	private String description;
	private PlaceDto place;
	private String eventType;
	private String body;

	public EventDto() {
		super();
	}

	public EventDto(Long id, String title, String description, PlaceDto place, String eventType, String body) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.place = place;
		this.eventType = eventType;
		this.body = body;
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

	public PlaceDto getPlace() {
		return place;
	}

	public void setPlace(PlaceDto place) {
		this.place = place;
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
