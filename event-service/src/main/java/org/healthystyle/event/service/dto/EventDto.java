package org.healthystyle.event.service.dto;

import java.time.Instant;

public class EventDto {
	private Long id;
	private String title;
	private String description;
	private PlaceDto place;
	private Instant appointedTime;
	private String eventType;
	private String body;
	private Double distance;

	public EventDto() {
		super();
	}

	public EventDto(Long id, String title, String description, PlaceDto place, Instant appointedTime, String eventType, String body) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.place = place;
		this.eventType = eventType;
		this.body = body;
	}

	public EventDto(Long id, String title, String description, PlaceDto place, Instant appointedTime, Double distance) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.place = place;
		this.distance = distance;
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

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

}
