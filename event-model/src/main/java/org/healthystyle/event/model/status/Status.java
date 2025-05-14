package org.healthystyle.event.model.status;

import java.time.Instant;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table
public class Status {
	@Id
	@SequenceGenerator(name = "status_generator", sequenceName = "status_seq", initialValue = 1, allocationSize = 10)
	@GeneratedValue(generator = "status_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, unique = true)
	private StatusType type;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Instant createdOn = Instant.now();

	public Status() {
		super();
	}

	public Status(StatusType type) {
		super();

		Objects.requireNonNull(type, "Type must be pointed");

		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public StatusType getType() {
		return type;
	}

	public Instant getCreatedOn() {
		return createdOn;
	}
}
