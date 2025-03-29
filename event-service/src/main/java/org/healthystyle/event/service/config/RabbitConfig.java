package org.healthystyle.event.service.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
	public static final String EVENT_CREATED_ROUTING_KEY = "event.created";
	public static final String EVENT_CREATED_QUEUE = "event-created-queue";

	public static final String EVENT_DELETED_ROUTING_KEY = "event.deleted";
	public static final String EVENT_DIRECT_EXCHANGE = "event-direct-exchange";

	public static final String USER_CREATED_QUEUE = "user-created-queue";
	public static final String USER_DELETED_QUEUE = "user-deleted-queue";

	public static final String SPORT_CREATED_QUEUE = "sport-created-queue";

	@Bean
	public DirectExchange eventDirectExchange() {
		return new DirectExchange(EVENT_DIRECT_EXCHANGE);
	}
}
