package org.healthystyle.event.service;

import org.healthystyle.event.model.Place;
import org.healthystyle.event.service.dto.PlaceSaveRequest;
import org.healthystyle.event.service.dto.PlaceUpdateRequest;
import org.healthystyle.event.service.error.event.PlaceNotFoundException;
import org.healthystyle.util.error.ValidationException;

public interface PlaceService {
	Place findById(Long id) throws ValidationException, PlaceNotFoundException;

	Place save(PlaceSaveRequest saveRequest) throws ValidationException;

	void update(PlaceUpdateRequest updateResquest, Long id) throws ValidationException, PlaceNotFoundException;
}
