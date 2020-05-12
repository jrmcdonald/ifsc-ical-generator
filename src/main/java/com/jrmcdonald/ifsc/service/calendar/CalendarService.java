package com.jrmcdonald.ifsc.service.calendar;

import reactor.core.publisher.Mono;

import java.util.List;

public interface CalendarService {
    Mono<String> createCalendar();
    Mono<String> createCalendar(List<String> categories);
}
