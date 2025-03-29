package org.healthystyle.event.service.dto.mapper;

import java.util.List;

import org.healthystyle.event.model.UserEvent;
import org.healthystyle.event.service.dto.UserEventDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEventMapper {
	UserEventDto toDto(UserEvent userEvent);

	List<UserEventDto> toDtos(List<UserEvent> userEvents);
}
