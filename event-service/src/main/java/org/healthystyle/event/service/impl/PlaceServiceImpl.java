package org.healthystyle.event.service.impl;

import java.util.HashMap;
import java.util.Optional;

import org.healthystyle.event.model.Place;
import org.healthystyle.event.repository.PlaceRepository;
import org.healthystyle.event.service.PlaceService;
import org.healthystyle.event.service.dto.PlaceSaveRequest;
import org.healthystyle.event.service.dto.PlaceUpdateRequest;
import org.healthystyle.event.service.error.event.PlaceNotFoundException;
import org.healthystyle.util.error.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;

@Service
public class PlaceServiceImpl implements PlaceService {
	@Autowired
	private PlaceRepository repository;
	@Autowired
	private Validator validator;

	private static final Logger LOG = LoggerFactory.getLogger(PlaceServiceImpl.class);

	@Override
	public Place findById(Long id) throws ValidationException, PlaceNotFoundException {
		BindingResult result = new MapBindingResult(new HashMap<>(), "place");

		LOG.debug("Checking id for not null: {}", id);
		if (id == null) {
			result.reject("place.find.id.not_null", "Укажите идентификатор места для поиска");
			throw new ValidationException("The id is null", result);
		}

		LOG.debug("Checking place for existence by id '{}'", id);
		Optional<Place> place = repository.findById(id);
		if (place.isEmpty()) {
			result.reject("place.find.not_found", "Не удалось найти места");
			throw new PlaceNotFoundException(id, result);
		}
		LOG.info("Got place successfully by id '%s'", id);

		return place.get();
	}

	@Override
	public Place save(PlaceSaveRequest saveRequest) throws ValidationException {
		LOG.debug("Validating place: {}", saveRequest);
		BindingResult result = new BeanPropertyBindingResult(saveRequest, "place");
		validator.validate(saveRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException("The place is invalid: %s. Result: %s", result, saveRequest, result);
		}

		LOG.debug("The place is OK: {}", saveRequest);

		String description = saveRequest.getDescription();
		if (description != null && description.isBlank()) {
			description = null;
		}

		Place place = new Place(saveRequest.getTitle(), saveRequest.getDescription(), saveRequest.getLatitude(),
				saveRequest.getLongitude());
		place = repository.save(place);
		LOG.info("Place was saved successfully: {}", saveRequest);

		return place;
	}

	@Override
	public void update(PlaceUpdateRequest updateRequest, Long id) throws ValidationException, PlaceNotFoundException {
		LOG.debug("Vlidating place: {}", updateRequest);
		BindingResult result = new BeanPropertyBindingResult(updateRequest, "place");
		validator.validate(updateRequest, result);
		if (result.hasErrors()) {
			throw new ValidationException("The place is invalid: %s. Result: %s", result, updateRequest, result);
		}

		Place place = findById(id);

		LOG.debug("The data is OK");

		String description = updateRequest.getDescription();
		if (description != null && description.isBlank()) {
			description = null;
		}

		String title = updateRequest.getTitle();
		String oldTitle = place.getTitle();
		if (!title.equals(oldTitle)) {
			LOG.debug("Setting title from '{}' to '{}'", oldTitle, title);
			place.setTitle(title);
		}

		String oldDescription = place.getDescription();
		LOG.debug("Setting description from '{}' to '{}'", oldDescription, description);
		place.setDescription(oldDescription);

		Double latitude = updateRequest.getLatitude();
		Double oldLatitude = place.getLatitude();
		if (!latitude.equals(oldTitle)) {
			LOG.debug("Setting latitude from '{}' to '{}'", oldLatitude, latitude);
			place.setLatitude(latitude);
		}

		Double longitude = updateRequest.getLongitude();
		Double oldLongitude = place.getLongitude();
		if (!longitude.equals(oldLongitude)) {
			LOG.debug("Setting longitude from '{}' to '{}'", oldLongitude, longitude);
			place.setLongitude(longitude);
		}

		repository.save(place);
		LOG.info("Place was updated successfully: {}", updateRequest);
	}

}
