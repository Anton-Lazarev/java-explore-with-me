package ru.practicum.ewm.main.event.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SearchAdminParams {
    private List<Long> userIDs;
    private List<String> states;
    private List<Long> categoryIDs;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
}
