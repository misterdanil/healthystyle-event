package org.healthystyle.event.service.cache.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.healthystyle.event.service.cache.UserRepository;
import org.healthystyle.event.service.cache.UserService;
import org.healthystyle.event.service.cache.dto.User;
import org.healthystyle.event.service.error.user.UserInvalidateException;
import org.healthystyle.util.error.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;

//@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository repository;
	@Autowired
	private Validator validator;

	private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public List<User> findAllById(Set<Long> ids) {
		LOG.debug("Checking ids for empty: {}", ids);
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyList();
		}

		LOG.debug("The ids are OK: {}", ids);

		List<User> users = new ArrayList<>();
		LOG.debug("Getting users by ids: {}", ids);
		Iterable<User> itUsers = repository.findAllById(ids);
		itUsers.forEach(user -> {
			LOG.debug("Got user by: {}", user);
			users.add(user);
		});

		LOG.debug("Got users successfully: {}", users);

		return users;

	}

	@Override
	public User save(User user) {
		LOG.debug("Validating user: {}", user);
		BindingResult result = new BeanPropertyBindingResult(user, "user");
		validator.validate(user, result);
		if (result.hasErrors()) {
			throw new UserInvalidateException(user, result);
		}
		LOG.debug("User is OK: {}", user);

		user = repository.save(user);

		return user;
	}

	@Override
	public boolean existsById(Long id) {
		LOG.debug("Checking user existence by id '{}'", id);
		boolean exists = repository.existsById(id);
		LOG.debug("Got existence result by id '{}' successfully", id);
		return exists;
	}

	@Override
	public void delete(User user) {
		LOG.debug("Deleteing user: {}", user);
		repository.delete(user);
	}

}
