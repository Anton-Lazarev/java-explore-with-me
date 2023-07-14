package ru.practicum.ewm.main.compilations;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.main.compilations.dto.IncomeCompilationDTO;
import ru.practicum.ewm.main.compilations.dto.OutcomeCompilationDTO;
import ru.practicum.ewm.main.event.dto.OutcomeEventShortDTO;

import java.util.List;

@UtilityClass
public class CompilationMapper {
    public Compilation incomeDtoToCompilation(IncomeCompilationDTO dto) {
        return Compilation.builder()
                .pinned(dto.getPinned())
                .title(dto.getTitle())
                .build();
    }

    public OutcomeCompilationDTO compilationToOutcomeDTO(Compilation compilation, List<OutcomeEventShortDTO> events) {
        return OutcomeCompilationDTO.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(events)
                .build();
    }
}
