package org.healthystyle.event.repository.dto;

public class ParticipateStatus {
	private Long userId;
	private Long eventId;

	public ParticipateStatus() {
		super();
	}

	public ParticipateStatus(Long userId, Long eventId) {
		super();
		this.userId = userId;
		this.eventId = eventId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

}
