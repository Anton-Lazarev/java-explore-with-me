package ru.practicum.ewm.main.requests.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OutcomeGroupedRequestsDTO {
    private List<EventRequestDTO> confirmedRequests;
    private List<EventRequestDTO> rejectedRequests;
}
