package org.healthystyle.event.service.listener;

import org.healthystyle.event.service.cache.UserService;
import org.healthystyle.event.service.cache.dto.User;
import org.healthystyle.event.service.config.RabbitConfig;
import org.healthystyle.util.error.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

//@Controller
public class UserListener {
	@Autowired
	private UserService userService;

	private static final Logger LOG = LoggerFactory.getLogger(UserListener.class);

	@RabbitListener(queues = RabbitConfig.USER_CREATED_QUEUE)
	public void createUser(User user) throws ValidationException {
		LOG.debug("Got user: {}", user);
		user = userService.save(user);
	}

	@RabbitListener(queues = RabbitConfig.USER_DELETED_QUEUE)
	public void deleteUser(User user) {
		LOG.debug("Got user: {}", user);
		userService.delete(user);
	}

}
