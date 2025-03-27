package org.healthystyle.event.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.healthystyle.event.model.role.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(indexes = @Index(name = "user_event_event_id_idx", columnList = "event_id"))
public class UserEvent {
	@Id
	@SequenceGenerator(name = "user_event_generator", sequenceName = "user_event_seq", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "user_event_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(name = "user_id", nullable = false)
	private Long userId;
	@ManyToOne
	@JoinColumn(name = "event_id", nullable = false)
	private Event event;
	@ManyToMany
	@JoinTable(name = "user_event", joinColumns = @JoinColumn(name = "user_event_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false))
	private List<Role> roles;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Instant createdOn = Instant.now();

	public UserEvent() {
		super();
	}

	public UserEvent(Long userId, Event event, Role... roles) {
		super();
		this.userId = userId;
		this.event = event;
		this.roles = Arrays.asList(roles);
	}

	public Long getId() {
		return id;
	}

	public Long getUserId() {
		return userId;
	}

	public Event getEvent() {
		return event;
	}

	public List<Role> getRoles() {
		if (roles == null) {
			roles = new ArrayList<>();
		}
		return roles;
	}

	public void addRoles(List<Role> roles) {
		getRoles().addAll(roles);
	}

	public void addRole(Role role) {
		getRoles().add(role);
	}

	public Instant getCreatedOn() {
		return createdOn;
	}

}
