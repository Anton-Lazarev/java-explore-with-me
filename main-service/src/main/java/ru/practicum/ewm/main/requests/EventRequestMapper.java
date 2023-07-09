package ru.practicum.ewm.main.requests;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.main.requests.dto.EventRequestDTO;

@UtilityClass
public class EventRequestMapper {
    public EventRequestDTO requestToEventRequestDTO(EventRequest request) {
        return EventRequestDTO.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .created(request.getCreated())
                .build();
    }
}
