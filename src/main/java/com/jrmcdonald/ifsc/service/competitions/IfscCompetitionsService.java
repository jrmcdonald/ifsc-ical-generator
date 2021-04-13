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
    public Mono<CompetitionList> findAll() {
        return webClient.get()
                .uri("/results-api.php?api=season_leagues_calendar&league=388")
                .retrieve()
                .bodyToMono(CompetitionList.class);
    }
}
