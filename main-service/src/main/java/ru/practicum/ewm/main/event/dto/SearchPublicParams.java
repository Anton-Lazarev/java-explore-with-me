package ru.practicum.ewm.main.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.main.event.dto.enums.EventSort;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SearchPublicParams {
    private String text;
    private List<Integer> categoryIDs;
    private boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private boolean onlyAvailable;
    private EventSort sort;
}
