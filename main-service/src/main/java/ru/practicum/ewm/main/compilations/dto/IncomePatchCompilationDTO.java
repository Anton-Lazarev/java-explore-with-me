package ru.practicum.ewm.main.compilations.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class IncomePatchCompilationDTO {
    private List<Long> events;
    private Boolean pinned;
    @Size(min = 1, max = 50, message = "Size of title should be between 1 and 50")
    private String title;
}
