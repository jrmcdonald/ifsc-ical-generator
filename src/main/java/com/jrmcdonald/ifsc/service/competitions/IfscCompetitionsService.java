package com.jrmcdonald.ifsc.service.competitions;

import com.jrmcdonald.ifsc.model.CompetitionList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Service
public class IfscCompetitionsService implements CompetitionsService {

    private final WebClient webClient;
    private final String ifscRankingUrl;

    public IfscCompetitionsService(@Value("${external.ifsc.ranking.url}") String ifscRankingUrl) {
        this.ifscRankingUrl = ifscRankingUrl;
        this.webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, StandardCharsets.UTF_8.toString())
                .build();
    }

    @Override
    public Mono<CompetitionList> findAll() {
        return webClient.get()
                .uri(URI.create(ifscRankingUrl))
                .retrieve()
                .bodyToMono(CompetitionList.class);
    }

    @Override
    public Mono<CompetitionList> findByCategory(List<String> categories) {
        return findAll()
                .map(competitionList ->
                        competitionList.getCompetitions()
                                .stream()
                                .filter(competition -> categories.contains(competition.getCategory()))
                                .collect(collectingAndThen(toList(), CompetitionList::new)));
    }
}
