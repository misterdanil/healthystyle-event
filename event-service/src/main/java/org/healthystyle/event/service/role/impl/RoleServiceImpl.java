package org.healthystyle.event.service.role.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import org.healthystyle.event.model.role.Role;
import org.healthystyle.event.model.role.Type;
import org.healthystyle.event.repository.role.RoleRepository;
import org.healthystyle.event.service.role.RoleService;
import org.healthystyle.util.error.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleRepository repository;

	private static final Logger LOG = LoggerFactory.getLogger(RoleServiceImpl.class);

	@Override
	public Role findByType(Type type) throws ValidationException {
		BindingResult result = new MapBindingResult(new HashMap<>(), "role");

		LOG.debug("Checking type for not null: {}", type);
		if (type == null) {
			result.reject("role.find.type.not_null", "Укажите тип роли для поиска");
			throw new ValidationException("The type is null", result);
		}

		Role role = repository.findByType(type);
		LOG.debug("Got role successfully by type '{}'", type);

		return role;
	}

	@Override
	public Set<Role> findByTypes(Set<Type> types) {
		LOG.debug("Checking types for emptiness: {}", types);
		if(types == null || types.isEmpty()) {
			LOG.debug("Returning empty set");
			return Collections.emptySet();
		}
		
		Set<Role> roles = repository.findByTypes(types);
		LOG.info("Got roles successfully by types '{}'", types);
		
		return roles;
	}

}
