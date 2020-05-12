package com.jrmcdonald.ifsc.service.competitions;

import com.jrmcdonald.ifsc.logging.WebFluxLogger;
import com.jrmcdonald.ifsc.model.CompetitionList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Service
public class IfscCompetitionsService implements CompetitionsService {

    private final WebClient webClient;
    private final WebFluxLogger webFluxLogger;

    public IfscCompetitionsService(IfscCompetitionsConfig config, WebFluxLogger webFluxLogger) {
        webClient = WebClient.builder()
                .baseUrl(config.getHost())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, StandardCharsets.UTF_8.toString())
                .build();

        this.webFluxLogger = webFluxLogger;
    }

    @Override
    public Mono<CompetitionList> findAll() {
        return webClient.get()
                .uri("/egw/ranking/json.php")
                .retrieve()
                .bodyToMono(CompetitionList.class);
    }

    @Override
    public Mono<CompetitionList> findByCategory(List<String> categories) {
        return findAll()
                .doOnEach(webFluxLogger.logOnNext(competitionList -> log.info("Retrieved competitions for {}", kv("categories", String.join(",", categories)))))
                .map(competitionList ->
                        competitionList.getCompetitions()
                                .stream()
                                .filter(competition -> categories.contains(competition.getCategory()))
                                .collect(collectingAndThen(toList(), CompetitionList::new)));
    }
}
