package com.jrmcdonald.ifsc.service.calendar;

import biweekly.Biweekly;
import biweekly.ICalendar;
import com.jrmcdonald.ifsc.model.Competition;
import com.jrmcdonald.ifsc.model.CompetitionList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class InternetCalendarServiceTest {

    private CalendarService calendarService;

    @BeforeEach
    void beforeEach() {
        calendarService = new InternetCalendarService();
    }

    @Test
    @DisplayName("Should create a calendar from competitions")
    void shouldCreateACalendarFromCompetitions() {
        Instant epoch = Instant.EPOCH;
        Instant epochPlusOne = Instant.EPOCH.plus(1, ChronoUnit.DAYS);

        Competition cliffhanger = new Competition("Cliffhanger", "http://www.cliffhanger.com", "70", epoch, epoch);
        Competition olympics = new Competition("Olympics", "http://www.olympics.com", "70", epochPlusOne, epochPlusOne);

        Mono<String> calendarMono = calendarService.createCalendar(Mono.just(new CompetitionList(asList(cliffhanger, olympics))));

        StepVerifier.create(calendarMono)
                .assertNext(calendar -> {
                    ICalendar actual = Biweekly.parse(calendar).first();
                    assertThat(actual.getEvents()).hasSize(2);

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                    try {
                        assertThat(actual.getEvents().get(0).getSummary().getValue()).isEqualTo("Cliffhanger");
                        assertThat(actual.getEvents().get(0).getDescription().getValue()).isEqualTo("http://www.cliffhanger.com");
                        assertThat(actual.getEvents().get(0).getDateStart().getValue()).isEqualTo(formatter.parse("1970-01-01"));
                        assertThat(actual.getEvents().get(0).getDateEnd().getValue()).isEqualTo(formatter.parse("1970-01-01"));

                        assertThat(actual.getEvents().get(1).getSummary().getValue()).isEqualTo("Olympics");
                        assertThat(actual.getEvents().get(1).getDescription().getValue()).isEqualTo("http://www.olympics.com");
                        assertThat(actual.getEvents().get(1).getDateStart().getValue()).isEqualTo(formatter.parse("1970-01-02"));
                        assertThat(actual.getEvents().get(1).getDateEnd().getValue()).isEqualTo(formatter.parse("1970-01-02"));
                    } catch (ParseException e) {
                        fail("Unexpected exception parsing date", e);
                    }
                })
                .expectComplete()
                .verify();
    }

}