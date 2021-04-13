package com.jrmcdonald.ifsc.service.calendar;

import com.jrmcdonald.ifsc.model.Competition;
import com.jrmcdonald.ifsc.model.CompetitionList;
import com.jrmcdonald.ifsc.service.competitions.CompetitionsService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import biweekly.Biweekly;
import biweekly.ICalendar;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InternetCalendarServiceTest {

    private CalendarService calendarService;

    @Mock
    private CompetitionsService competitionsService;

    @BeforeEach
    void beforeEach() {
        calendarService = new InternetCalendarService(competitionsService);
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(competitionsService);
    }

    @Test
    @DisplayName("Should create calendar")
    void shouldCreateCalendar() {
        Instant epoch = Instant.EPOCH;
        Instant epochPlusOne = Instant.EPOCH.plus(1, ChronoUnit.DAYS);

        Competition cliffhanger = new Competition("Cliffhanger", epoch, epoch);
        Competition olympics = new Competition("Olympics", epochPlusOne, epochPlusOne);

        when(competitionsService.findAll("388")).thenReturn(Mono.just(new CompetitionList(asList(cliffhanger, olympics))));

        Mono<String> calendarMono = calendarService.createCalendar("388");

        StepVerifier.create(calendarMono)
                .assertNext(calendarString -> {
                    ICalendar calendar = Biweekly.parse(calendarString).first();
                    assertThat(calendar.getEvents()).hasSize(2);

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                    try {
                        assertThat(calendar.getEvents().get(0).getSummary().getValue()).isEqualTo("Cliffhanger");
                        assertThat(calendar.getEvents().get(0).getDateStart().getValue()).isEqualTo(formatter.parse("1970-01-01"));
                        assertThat(calendar.getEvents().get(0).getDateEnd().getValue()).isEqualTo(formatter.parse("1970-01-01"));

                        assertThat(calendar.getEvents().get(1).getSummary().getValue()).isEqualTo("Olympics");
                        assertThat(calendar.getEvents().get(1).getDateStart().getValue()).isEqualTo(formatter.parse("1970-01-02"));
                        assertThat(calendar.getEvents().get(1).getDateEnd().getValue()).isEqualTo(formatter.parse("1970-01-02"));
                    } catch (ParseException e) {
                        fail("Unexpected exception parsing date", e);
                    }
                }).verifyComplete();
    }
}