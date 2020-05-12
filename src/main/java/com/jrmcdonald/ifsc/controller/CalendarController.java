package com.jrmcdonald.ifsc.controller;

import com.jrmcdonald.ifsc.service.calendar.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping(produces = "text/calendar")
    public Mono<ResponseEntity<String>> getCalendar() {
        return calendarService.createCalendar().map(ResponseEntity::ok);
    }

    @PostMapping(produces = "text/calendar", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> getCalendarByCategory(@RequestBody Mono<List<String>> categoriesMono) {
        return categoriesMono.flatMap(calendarService::createCalendar).map(ResponseEntity::ok);
    }

}
