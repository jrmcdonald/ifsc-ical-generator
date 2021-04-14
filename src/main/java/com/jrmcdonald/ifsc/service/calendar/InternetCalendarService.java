package com.jrmcdonald.ifsc.service.calendar;

import com.jrmcdonald.ifsc.model.Competition;
import com.jrmcdonald.ifsc.service.competitions.CompetitionsService;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import biweekly.ICalendar;
import biweekly.component.VEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class InternetCalendarService implements CalendarService {

    private final CompetitionsService competitionsService;

    @Override
    public Mono<String> createCalendar(String leagueId) {
        return competitionsService.findAll(leagueId)
                .flatMap(competitions -> Mono.just(competitions.getEvents()))
                .flatMap(this::mapEventsToCalendar)
                .doFinally(calendar -> log.info("Created calendar for all categories"));
    }

    private Mono<String> mapEventsToCalendar(List<Competition> competitions) {
        return Mono.just(competitions.stream()
                .map(this::createEvent)
                .collect(collectingAndThen(toList(), this::writeCalendar)));
    }

    private VEvent createEvent(Competition competition) {
        VEvent event = new VEvent();

        event.setDateStart(Date.from(competition.getStartDate()), false);
        event.setDateEnd(Date.from(competition.getEndDate()), false);
        event.setSummary(competition.getEvent());

        return event;
    }

    private ICalendar createInternetCalendar(List<VEvent> events) {
        ICalendar calendar = new ICalendar();
        calendar.getEvents().addAll(events);
        return calendar;
    }

    private String writeCalendar(List<VEvent> events) {
        return createInternetCalendar(events).write();
    }
}
