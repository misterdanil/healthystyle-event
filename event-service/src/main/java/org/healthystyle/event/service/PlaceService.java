package org.healthystyle.event.service;

import org.healthystyle.event.model.Place;
import org.healthystyle.event.service.dto.PlaceSaveRequest;
import org.healthystyle.event.service.dto.PlaceUpdateRequest;

public interface PlaceService {
	Place findById(Long id);

	Place save(PlaceSaveRequest saveRequest);

	void update(PlaceUpdateRequest updateRequest, Long id);
}
