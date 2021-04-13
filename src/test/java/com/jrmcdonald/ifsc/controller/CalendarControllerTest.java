package com.jrmcdonald.ifsc.controller;

import com.jrmcdonald.ifsc.Application;
import com.jrmcdonald.ifsc.service.calendar.CalendarService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@AutoConfigureWebTestClient
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
class CalendarControllerTest {

    @MockBean
    CalendarService calendarService;

    @Autowired
    private WebTestClient client;

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(calendarService);
    }

    @Test
    @DisplayName("Should execute service")
    void shouldExecuteService() {
        String expectedValue = "ICALENDAR_VALUE";

        when(calendarService.createCalendar()).thenReturn(Mono.just(expectedValue));

        FluxExchangeResult<String> exchangeResult = client.get()
                .uri("/calendar")
                .exchange()
                .expectHeader()
                .contentType("text/calendar;charset=UTF-8")
                .returnResult(String.class);

        StepVerifier.create(exchangeResult.getResponseBody())
                    .assertNext(result -> assertThat(result).isEqualTo(expectedValue))
                    .verifyComplete();

        verify(calendarService).createCalendar();
    }
}