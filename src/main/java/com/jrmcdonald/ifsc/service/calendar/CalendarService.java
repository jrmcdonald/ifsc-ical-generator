package com.jrmcdonald.ifsc.service.calendar;

import reactor.core.publisher.Mono;

public interface CalendarService {
    Mono<String> createCalendar(String leagueId);
}
