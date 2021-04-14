package com.jrmcdonald.ifsc.service.competitions;

import com.jrmcdonald.ifsc.model.CompetitionList;

import reactor.core.publisher.Mono;

public interface CompetitionsService {
    Mono<CompetitionList> findAll(String leagueId);
}
