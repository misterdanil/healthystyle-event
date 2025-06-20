package org.healthystyle.event.repository;

import org.healthystyle.event.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
	@Query("SELECT e FROM Event e WHERE LOWER(e.title) = LOWER(:title) ORDER BY e.createdOn DESC")
	Page<Event> findByTitle(String title, Pageable pageable);

	@Query("SELECT e FROM Event e INNER JOIN e.place p WHERE LOWER(e.title) LIKE CONCAT('%', LOWER(:title), '%') ORDER BY SQRT((:latitude - p.latitude) * (:latitude - p.latitude) + (:longitude - p.longitude) * (:longitude - p.longitude))")
	Page<Event> findNearestByCoordinates(String title, Double latitude, Double longitude, Pageable pageable);

	@Query("SELECT e, haversine(:latitude, :longitude, p.latitude, p.longitude) AS distance FROM Event e INNER JOIN e.place p WHERE LOWER(e.title) LIKE CONCAT('%', LOWER(:title), '%') ORDER BY haversine(:latitude, :longitude, p.latitude, p.longitude)")
	Page<Object[][]> findNearestByCoordinatesHaversine(String title, Double latitude, Double longitude,
			Pageable pageable);

	@Query("SELECT e FROM Event e ORDER BY e.createdOn DESC")
	Page<Event> find(Pageable pageable);

	@Query("SELECT e FROM Event e INNER JOIN e.users u WHERE u.userId = :userId ORDER BY u.createdOn DESC")
	Page<Event> findByMember(Long userId, Pageable pageable);
}
