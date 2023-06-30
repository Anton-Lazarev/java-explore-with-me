package ru.practicum.ewm.statistic.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.statistic.dto.IncomeHitDTO;
import ru.practicum.ewm.statistic.dto.OutcomeHitDTO;

import java.util.List;
import java.util.Map;

@Service
public class HitClient extends BaseClient {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public HitClient(@Value("${statistic-server.url}") String serverURL, RestTemplateBuilder restTemplateBuilder) {
        super(restTemplateBuilder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverURL))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public List<OutcomeHitDTO> getHitStatistic(String start, String end, List<String> uris, boolean unique) {
        Map<String, Object> params = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );
        String statisticPath = "/stats?start={start}&end={end}&uris={uris}&unique={unique}";
        ResponseEntity<Object> objectsResponse = get(statisticPath, params);
        return objectMapper.convertValue(objectsResponse.getBody(), new TypeReference<>() {});
    }

    public ResponseEntity<Object> addHit(IncomeHitDTO dto) {
        return post("/hit", dto);
    }
}
