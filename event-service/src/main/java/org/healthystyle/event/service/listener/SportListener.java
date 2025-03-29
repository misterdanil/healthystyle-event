package org.healthystyle.event.service.listener;

import org.healthystyle.event.model.status.StatusType;
import org.healthystyle.event.service.EventService;
import org.healthystyle.event.service.config.RabbitConfig;
import org.healthystyle.event.service.error.event.EventNotFoundException;
import org.healthystyle.event.service.listener.dto.SportDto;
import org.healthystyle.util.error.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class SportListener {
	@Autowired
	private EventService eventService;

	private static final Logger LOG = LoggerFactory.getLogger(SportListener.class);

	@RabbitListener(queues = RabbitConfig.SPORT_CREATED_QUEUE)
	public void getSport(SportDto sport) throws ValidationException, EventNotFoundException {
		LOG.debug("Got sport: {}", sport);
		Long eventId = sport.getEventId();
		if (eventId != null) {
			eventService.changeStatus(StatusType.ACCEPTED, eventId);
		}
	}
}
