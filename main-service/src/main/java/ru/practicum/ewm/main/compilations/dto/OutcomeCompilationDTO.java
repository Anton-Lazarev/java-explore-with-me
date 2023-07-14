package ru.practicum.ewm.main.compilations.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.main.event.dto.OutcomeEventShortDTO;

import java.util.List;

@Data
@Builder
public class OutcomeCompilationDTO {
    private long id;
    private String title;
    private boolean pinned;
    List<OutcomeEventShortDTO> events;
}
