package org.healthystyle.event.service.cache;

import java.util.List;
import java.util.Set;

import org.healthystyle.event.service.cache.dto.User;
import org.healthystyle.util.error.ValidationException;

public interface UserService {
	List<User> findAllById(Set<Long> ids);
	
	User save(User user) throws ValidationException;
}
