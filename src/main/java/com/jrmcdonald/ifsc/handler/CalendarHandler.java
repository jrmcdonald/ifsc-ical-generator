package com.jrmcdonald.ifsc.handler;

import com.jrmcdonald.ifsc.service.calendar.CalendarService;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.List;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CalendarHandler {

    private final CalendarService calendarService;

    public Mono<ServerResponse> getCalendar(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .header("Content-Type", "text/calendar")
                .body(calendarService.createCalendar(), String.class);
    }

    public Mono<ServerResponse> getCalendarByCategory(ServerRequest serverRequest) {
        Mono<List<String>> categoriesMono = serverRequest.bodyToMono(new ParameterizedTypeReference<>() {});
        Mono<String> calendar = calendarService.createCalendar(categoriesMono);
        return ServerResponse.ok()
                .header("Content-Type", "text/calendar")
                .body(calendar, String.class);
    }
}
