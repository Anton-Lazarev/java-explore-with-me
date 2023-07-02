package ru.practicum.ewm.statistic.server.hits;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.statistic.dto.IncomeHitDTO;
import ru.practicum.ewm.statistic.dto.OutcomeHitDTO;
import ru.practicum.ewm.statistic.server.hits.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class HitMapper {
    public Hit incomeHitDtoToHit(IncomeHitDTO dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Hit.builder()
                .app(dto.getApp())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .created(LocalDateTime.parse(dto.getTimestamp(), formatter))
                .build();
    }

    public OutcomeHitDTO hitToOutcomeHitDTO(Hit hit, long hits) {
        return OutcomeHitDTO.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .hits(hits)
                .build();
    }
}
