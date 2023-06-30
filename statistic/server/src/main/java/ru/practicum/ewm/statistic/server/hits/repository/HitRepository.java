package ru.practicum.ewm.statistic.server.hits.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.statistic.dto.OutcomeHitDTO;
import ru.practicum.ewm.statistic.server.hits.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {
    String newOutcomeHitDTO = "new ru.practicum.ewm.statistic.dto.OutcomeHitDTO";

    @Query("select " + newOutcomeHitDTO + "(h.app, h.uri, count(h.uri)) from Hit as h " +
            "where h.uri in (:uris) and h.created between :start and :end " +
            "group by h.app, h.uri " +
            "order by count (h.uri) desc")
    List<OutcomeHitDTO> getStatsForUrisWithoutUniqueIPs(@Param("start") LocalDateTime start,
                                                        @Param("end") LocalDateTime end,
                                                        @Param("uris") List<String> uris);

    @Query("select " + newOutcomeHitDTO + "(h.app, h.uri, count(distinct h.ip)) from Hit as h " +
            "where h.uri in (:uris) and h.created between :start and :end " +
            "group by h.app, h.uri " +
            "order by count (distinct h.ip) desc")
    List<OutcomeHitDTO> getStatsForUrisWithUniqueIPs(@Param("start") LocalDateTime start,
                                                     @Param("end") LocalDateTime end,
                                                     @Param("uris") List<String> uris);

    @Query("select " + newOutcomeHitDTO + "(h.app, h.uri, count(distinct h.ip)) from Hit as h " +
            "where h.created between :start and :end " +
            "group by h.app, h.uri " +
            "order by count (distinct h.ip) desc")
    List<OutcomeHitDTO> getAllStatsWithUniqueIPs(@Param("start") LocalDateTime start,
                                                 @Param("end") LocalDateTime end);

    @Query("select " + newOutcomeHitDTO + "(h.app, h.uri, count(h.uri)) from Hit as h " +
            "where h.created between :start and :end " +
            "group by h.app, h.uri " +
            "order by count (h.uri) desc")
    List<OutcomeHitDTO> getAllStatsWithoutUniqueIPs(@Param("start") LocalDateTime start,
                                                 @Param("end") LocalDateTime end);
}
