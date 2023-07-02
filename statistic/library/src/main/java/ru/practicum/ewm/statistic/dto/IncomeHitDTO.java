package ru.practicum.ewm.statistic.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class IncomeHitDTO {
    @NotBlank(message = "field APP can't be empty")
    private String app;
    @NotBlank(message = "field URI can't be empty")
    private String uri;
    @NotBlank(message = "field IP can't be empty")
    private String ip;
    @NotBlank(message = "field CREATED can't be empty")
    private String timestamp;
}
