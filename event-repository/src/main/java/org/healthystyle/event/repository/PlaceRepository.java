package org.healthystyle.event.repository;

import org.healthystyle.event.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {

}
