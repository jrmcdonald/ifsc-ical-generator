package com.jrmcdonald.ifsc.controller;

import com.jrmcdonald.ifsc.service.calendar.CalendarService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping(produces = "text/calendar")
    public Mono<ResponseEntity<String>> getCalendar(@RequestParam("leagueId") String leagueId) {
        return calendarService.createCalendar(leagueId).map(ResponseEntity::ok);
    }

}
