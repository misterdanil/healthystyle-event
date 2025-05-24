package org.healthystyle.event.model.role;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.healthystyle.event.model.role.opportunity.Opportunity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(indexes = @Index(name = "role_type_idx", columnList = "type", unique = true))
public class Role {
	@Id
	@SequenceGenerator(name = "role_generator", sequenceName = "role_seq", initialValue = 1, allocationSize = 20)
	private Long id;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, unique = true)
	private Type type;
	@ManyToMany
	@JoinTable(name = "role_opportunity", joinColumns = @JoinColumn(name = "role_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "opportunity_id", nullable = false))
	private List<Opportunity> opportunities;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Instant createdOn = Instant.now();

	public Long getId() {
		return id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public List<Opportunity> getOpportunities() {
		if (opportunities == null) {
			opportunities = new ArrayList<>();
		}
		return opportunities;
	}

	public void addOpportunity(Opportunity opportunity) {
		getOpportunities().add(opportunity);
	}

	public void addOpportunities(List<Opportunity> opportunities) {
		getOpportunities().addAll(opportunities);
	}

	public Instant getCreatedOn() {
		return createdOn;
	}

}
