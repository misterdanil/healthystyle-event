package org.healthystyle.event.service.config;

public class RabbitConfig {
	public static final String EVENT_CREATED_ROUTING_KEY = "event.created";
	public static final String EVENT_DELETED_ROUTING_KEY = "event.deleted";

	public static final String EVENT_DIRECT_EXCHANGE = "event-direct-exchange";
}
