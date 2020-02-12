package com.jrmcdonald.ifsc.service.calendar;

import com.jrmcdonald.ifsc.model.CompetitionList;
import reactor.core.publisher.Mono;

public interface CalendarService {
    Mono<String> createCalendar(Mono<CompetitionList> competitions);
}
