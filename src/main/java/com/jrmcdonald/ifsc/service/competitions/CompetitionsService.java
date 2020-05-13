package com.jrmcdonald.ifsc.service.competitions;

import com.jrmcdonald.ifsc.model.CompetitionList;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CompetitionsService {
    Mono<CompetitionList> findAll();
    Mono<CompetitionList> findByCategory(List<String> categories);
}
