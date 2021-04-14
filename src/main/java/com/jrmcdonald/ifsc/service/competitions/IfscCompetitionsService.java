package com.jrmcdonald.ifsc.service.competitions;

import com.jrmcdonald.ifsc.model.CompetitionList;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class IfscCompetitionsService implements CompetitionsService {

    private final WebClient webClient;

    public IfscCompetitionsService(IfscCompetitionsConfig config) {
        webClient = WebClient.builder()
                .baseUrl(config.getHost())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, StandardCharsets.UTF_8.toString())
                .build();
    }

    @Override
    public Mono<CompetitionList> findAll(String leagueId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/results-api.php")
                                             .queryParam("api", "season_leagues_calendar")
                                             .queryParam("league", leagueId)
                                             .build())
                .retrieve()
                .bodyToMono(CompetitionList.class);
    }
}
