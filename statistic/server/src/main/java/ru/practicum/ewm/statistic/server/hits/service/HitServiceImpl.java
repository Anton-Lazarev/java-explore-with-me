package ru.practicum.ewm.statistic.server.hits.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.statistic.dto.IncomeHitDTO;
import ru.practicum.ewm.statistic.dto.OutcomeHitDTO;
import ru.practicum.ewm.statistic.server.exceptions.DateTimeValidationException;
import ru.practicum.ewm.statistic.server.hits.HitMapper;
import ru.practicum.ewm.statistic.server.hits.model.Hit;
import ru.practicum.ewm.statistic.server.hits.repository.HitRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class HitServiceImpl implements HitService {
    private final HitRepository repository;

    @Override
    public Hit addHit(IncomeHitDTO dto) {
        Hit newHit = repository.save(HitMapper.incomeHitDtoToHit(dto));
        log.info("Save new hit with ID {} for URI {}", newHit.getId(), newHit.getUri());
        return newHit;
    }

    @Override
    public List<OutcomeHitDTO> getStatisticForURIs(String start, String end, List<String> uris, boolean unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        LocalDateTime endDate = LocalDateTime.parse(end, formatter);
        if (startDate.isAfter(endDate)) {
            throw new DateTimeValidationException("Start date should be before end date");
        }
        List<OutcomeHitDTO> outcomeHitDTOs = Collections.emptyList();
        if (uris == null && unique) {
            outcomeHitDTOs = repository.getAllStatsWithUniqueIPs(startDate, endDate);
        }
        if (uris == null && !unique) {
            outcomeHitDTOs = repository.getAllStatsWithoutUniqueIPs(startDate, endDate);
        }
        if (uris != null && unique) {
            outcomeHitDTOs = repository.getStatsForUrisWithUniqueIPs(startDate, endDate, uris);
        }
        if (uris != null && !unique) {
            outcomeHitDTOs = repository.getStatsForUrisWithoutUniqueIPs(startDate, endDate, uris);
        }
        log.info("Get statistic for URIs with size {}", outcomeHitDTOs.size());
        return outcomeHitDTOs;
    }
}
