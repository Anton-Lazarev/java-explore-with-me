package ru.practicum.ewm.main.requests.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EventRequestDTO {
    private long id;
    private long event;
    private long requester;
    private EventRequestStatus status;
    private LocalDateTime created;
}
