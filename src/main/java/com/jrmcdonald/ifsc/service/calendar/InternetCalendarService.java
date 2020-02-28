package com.jrmcdonald.ifsc.service.calendar;

import biweekly.ICalendar;
import biweekly.component.VEvent;
import com.jrmcdonald.ifsc.model.Competition;
import com.jrmcdonald.ifsc.service.competitions.CompetitionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class InternetCalendarService implements CalendarService {

    private final CompetitionsService competitionsService;

    @Override
    public Mono<String> createCalendar() {
        return competitionsService.findAll()
                .flatMap(competitions -> Mono.just(competitions.getCompetitions()))
                .flatMap(this::mapEventsToCalendar);
    }

    @Override
    public Mono<String> createCalendar(Mono<List<String>> categoriesMono) {
        return competitionsService.findByCategory(categoriesMono)
                .flatMap(competitions -> Mono.just(competitions.getCompetitions()))
                .flatMap(this::mapEventsToCalendar);
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
        event.setSummary(competition.getName());
        event.setDescription(competition.getHomepage());

        return event;
    }

    private ICalendar createCalendar(List<VEvent> events) {
        ICalendar calendar = new ICalendar();
        calendar.getEvents().addAll(events);
        return calendar;
    }

    private String writeCalendar(List<VEvent> events) {
        return createCalendar(events).write();
    }
}
