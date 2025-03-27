package org.healthystyle.event.model.role.opportunity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(indexes = @Index(name = "opportunity_name_idx", columnList = "name", unique = true))
public class Opportunity {
	@Id
	@SequenceGenerator(name = "opportunity_generator", sequenceName = "opportunity_seq", initialValue = 1, allocationSize = 20)
	@GeneratedValue(generator = "opportunity_generator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, unique = true)
	private Name name;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Instant createdOn = Instant.now();

	public Opportunity() {
		super();
	}

	public Opportunity(Name name) {
		super();
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public Name getName() {
		return name;
	}

	public Instant getCreatedOn() {
		return createdOn;
	}
}
