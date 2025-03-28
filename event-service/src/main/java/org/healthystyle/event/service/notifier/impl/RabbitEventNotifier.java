package org.healthystyle.event.service.notifier.impl;

import org.healthystyle.event.service.config.RabbitConfig;
import org.healthystyle.event.service.dto.EventDto;
import org.healthystyle.event.service.notifier.EventNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitEventNotifier implements EventNotifier {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	private static final Logger LOG = LoggerFactory.getLogger(RabbitEventNotifier.class);

	@Override
	public void notifyCreated(EventDto dto) {
		LOG.debug("Sending event to rabbit by exchange '{}', routing key '{}'. Body: {}",
				RabbitConfig.EVENT_DIRECT_EXCHANGE, RabbitConfig.EVENT_CREATED_ROUTING_KEY, dto);
		rabbitTemplate.convertAndSend(RabbitConfig.EVENT_DIRECT_EXCHANGE, RabbitConfig.EVENT_CREATED_ROUTING_KEY, dto);
	}

	@Override
	public void notifyDeleted(EventDto dto) {
		LOG.debug("Sending event to rabbit by exchange '{}', routing key '{}'. Body: {}",
				RabbitConfig.EVENT_DIRECT_EXCHANGE, RabbitConfig.EVENT_DELETED_ROUTING_KEY, dto);
		rabbitTemplate.convertAndSend(RabbitConfig.EVENT_DIRECT_EXCHANGE, RabbitConfig.EVENT_DELETED_ROUTING_KEY, dto);

	}

}
