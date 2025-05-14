package org.healthystyle.event.service.dto;

import java.time.Instant;

public class DistanceEvent {
	private Long id;
	private String title;
	private String description;
	private PlaceDto place;
	private Instant appointedTime;
	private Double distance;

	public DistanceEvent(Long id, String title, String description, PlaceDto place, Instant appointedTime,
			Double distance) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.place = place;
		this.appointedTime = appointedTime;
		this.distance = distance;
	}

	public DistanceEvent() {
		super();
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

	public Instant getAppointedTime() {
		return appointedTime;
	}

	public void setAppointedTime(Instant appointedTime) {
		this.appointedTime = appointedTime;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

}
