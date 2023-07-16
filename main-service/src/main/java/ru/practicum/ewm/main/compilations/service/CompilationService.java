package ru.practicum.ewm.main.compilations.service;

import ru.practicum.ewm.main.compilations.dto.IncomeCompilationDTO;
import ru.practicum.ewm.main.compilations.dto.IncomePatchCompilationDTO;
import ru.practicum.ewm.main.compilations.dto.OutcomeCompilationDTO;

import java.util.List;

public interface CompilationService {
    OutcomeCompilationDTO addCompilation(IncomeCompilationDTO dto);

    OutcomeCompilationDTO updateCompilation(long compID, IncomePatchCompilationDTO dto);

    void deleteCompilationByID(long compID);

    List<OutcomeCompilationDTO> getCompilations(boolean pinned, int from, int size);

    OutcomeCompilationDTO getCompilationByID(long compID);
}
