package org.healthystyle.event.service.dto.mapper;

import org.healthystyle.event.model.Event;
import org.healthystyle.event.service.dto.DistanceEvent;
import org.healthystyle.event.service.dto.EventDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventMapper {
	EventDto toDto(Event event);
	
	DistanceEvent toDistanceEvent(Event event);

}
