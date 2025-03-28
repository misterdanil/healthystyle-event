package org.healthystyle.event.service.notifier;

import org.healthystyle.event.service.dto.EventDto;

public interface EventNotifier {
	void notifyCreated(EventDto dto);

	void notifyDeleted(EventDto dto);
}
