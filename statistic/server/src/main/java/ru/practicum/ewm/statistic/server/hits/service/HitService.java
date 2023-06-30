package ru.practicum.ewm.statistic.server.hits.service;

import ru.practicum.ewm.statistic.dto.IncomeHitDTO;
import ru.practicum.ewm.statistic.dto.OutcomeHitDTO;
import ru.practicum.ewm.statistic.server.hits.model.Hit;

import java.util.List;

public interface HitService {
    Hit addHit(IncomeHitDTO dto);

    List<OutcomeHitDTO> getStatisticForURIs(String start, String end, List<String> uris, boolean unique);
}
