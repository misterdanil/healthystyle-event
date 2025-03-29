package org.healthystyle.event.service.status.impl;

import java.util.HashMap;

import org.healthystyle.event.model.status.Status;
import org.healthystyle.event.model.status.StatusType;
import org.healthystyle.event.repository.status.StatusRepository;
import org.healthystyle.event.service.status.StatusService;
import org.healthystyle.util.error.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@Service
public class StatusServiceImpl implements StatusService {
	@Autowired
	private StatusRepository repository;

	private static final Logger LOG = LoggerFactory.getLogger(StatusServiceImpl.class);

	@Override
	public Status findByType(StatusType type) throws ValidationException {
		LOG.debug("Checking type for not null: {}", type);
		if (type == null) {
			BindingResult result = new MapBindingResult(new HashMap<>(), "status");
			result.reject("status.find.type.not_null", "Укажите тип статуса для поиска");
			throw new ValidationException("The type is null", result);
		}

		Status status = repository.findByType(type);
		LOG.info("Got status by type '{}' successfully", type);

		return status;
	}
}
