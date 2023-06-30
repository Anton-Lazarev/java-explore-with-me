package ru.practicum.ewm.statistic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OutcomeHitDTO {
    private String app;
    private String uri;
    private long hits;
}
