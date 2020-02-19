package com.jrmcdonald.ifsc.service.competitions;

import com.jrmcdonald.ifsc.model.CompetitionList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Service
public class IfscCompetitionsService implements CompetitionsService {

    private final WebClient webClient;

    public IfscCompetitionsService(@Value("${external.ifsc.ranking.host}") String ifscRankingHost) {
        this.webClient = WebClient.builder()
                .baseUrl(ifscRankingHost)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, StandardCharsets.UTF_8.toString())
                .build();
    }

    @Override
    public Mono<CompetitionList> findAll() {
        return webClient.get()
                .uri("/egw/ranking/json.php")
                .retrieve()
                .bodyToMono(CompetitionList.class);
    }

    @Override
    public Mono<CompetitionList> findByCategory(Mono<List<String>> categoriesMono) {
        return categoriesMono.flatMap(categories ->
                findAll()
                        .map(competitionList ->
                                competitionList.getCompetitions()
                                        .stream()
                                        .filter(competition -> categories.contains(competition.getCategory()))
                                        .collect(collectingAndThen(toList(), CompetitionList::new))));
    }
}
