package org.healthystyle.event.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.healthystyle.event.model.status.Status;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(indexes = @Index(name = "event_title_idx", columnList = "title"))
public class Event {
	@Id
	@SequenceGenerator(name = "event_generator", sequenceName = "event_seq", initialValue = 1, allocationSize = 5)
	@GeneratedValue(generator = "event_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(nullable = false)
	private String title;
	@Column(nullable = false, length = 5000)
	private String description;
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "place_id", nullable = false)
	private Place place;
	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<UserEvent> users;
	@ManyToOne
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;
	@Column(name = "event_type")
	private String eventType;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Instant createdOn = Instant.now();

	public Event() {
		super();
	}

	public Event(String title, String description, Place place, Status status) {
		super();
		this.title = title;
		this.description = description;
		this.place = place;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Place getPlace() {
		return place;
	}

	public List<UserEvent> getUsers() {
		if (users == null) {
			users = new ArrayList<>();
		}
		return users;
	}

	public void addUsers(List<UserEvent> users) {
		getUsers().addAll(users);
	}

	public void addUser(UserEvent user) {
		getUsers().add(user);
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public Instant getCreatedOn() {
		return createdOn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
