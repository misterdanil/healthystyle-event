package org.healthystyle.event.repository.role.opportunity;

import org.healthystyle.event.model.role.opportunity.Name;
import org.healthystyle.event.model.role.opportunity.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {
	@Query("SELECT o FROM Opportunity o WHERE o.name = :name")
	Opportunity findByName(Name name);
}
