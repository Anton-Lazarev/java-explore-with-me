package ru.practicum.ewm.main;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.statistic.client.HitClient;
import ru.practicum.ewm.statistic.dto.IncomeHitDTO;
import ru.practicum.ewm.statistic.dto.OutcomeHitDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class StatisticForEvents {
    private final HitClient hitClient;

    public void addHitToStatistic(String uri, String app, String ip, String timestamp) {
        log.info("Add hit to statistic with uri: {} from app: {}", uri, app);
        hitClient.addHit(IncomeHitDTO.builder()
                .app(app)
                .ip(ip)
                .uri(uri)
                .timestamp(timestamp)
                .build());
    }

    public HashMap<Long, Long> getHitsForEvents(List<Long> eventsIDs) {
        HashMap<Long, Long> mapOfEventIdsWithHits = new HashMap<>();
        List<String> uris = new ArrayList<>();
        for (long id : eventsIDs) {
            uris.add("/events/" + id);
            mapOfEventIdsWithHits.put(id, 0L);
        }
        log.info("Hits statistic request for URIs: {}", uris);
        List<OutcomeHitDTO> hitDTOs = hitClient.getHitStatistic("2010-01-01 00:00:00", "2030-01-01 00:00:00", uris, true);
        for (OutcomeHitDTO dto : hitDTOs) {
            long eventID = Long.parseLong(dto.getUri().substring(dto.getUri().lastIndexOf("/") + 1));
            mapOfEventIdsWithHits.put(eventID, dto.getHits());
        }
        log.info("Got hit statistic in map<eventID, hits>: {}", mapOfEventIdsWithHits.toString());
        return mapOfEventIdsWithHits;
    }
}
