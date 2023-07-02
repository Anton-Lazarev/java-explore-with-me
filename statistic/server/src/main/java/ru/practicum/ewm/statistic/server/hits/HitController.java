package ru.practicum.ewm.statistic.server.hits;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.statistic.dto.IncomeHitDTO;
import ru.practicum.ewm.statistic.dto.OutcomeHitDTO;
import ru.practicum.ewm.statistic.server.hits.model.Hit;
import ru.practicum.ewm.statistic.server.hits.service.HitService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/")
@AllArgsConstructor
@Slf4j
public class HitController {
    private final HitService service;

    @PostMapping("/hit")
    public ResponseEntity<Hit> save(@Valid @RequestBody IncomeHitDTO dto) {
        log.info("Statistic : POST to /hit with {}", dto.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addHit(dto));
    }

    @GetMapping("/stats")
    public List<OutcomeHitDTO> getStatistic(@RequestParam String start,
                                            @RequestParam String end,
                                            @RequestParam(required = false) List<String> uris,
                                            @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Statistic : GET to /stats with start: {} ; end: {} ; uris: {} ; unique: {}", start, end, uris, unique);
        return service.getStatisticForURIs(start, end, uris, unique);
    }
}
