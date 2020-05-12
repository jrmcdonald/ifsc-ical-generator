package com.jrmcdonald.ifsc.controller;

import com.jrmcdonald.ifsc.Application;
import com.jrmcdonald.ifsc.service.calendar.CalendarService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@AutoConfigureWebTestClient
@SpringBootTest(classes = Application.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ActiveProfiles("test")
class CalendarControllerTest {

    @MockBean
    CalendarService calendarService;

    @Captor
    ArgumentCaptor<List<String>> categoriesCaptor;

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

        StepVerifier.create(exchangeResult.getResponseBody()).assertNext(result -> {
            assertThat(result).isEqualTo(expectedValue);
        }).verifyComplete();

        verify(calendarService).createCalendar();
    }

    @Test
    @DisplayName("Should execute service with categories")
    void shouldExecuteServiceWithCategories() {
        String expectedValue = "ICALENDAR_VALUE";

        when(calendarService.createCalendar(any())).thenReturn(Mono.just(expectedValue));

        FluxExchangeResult<String> exchangeResult = client.post()
                .uri("/calendar")
                .bodyValue(singletonList("69"))
                .exchange()
                .expectHeader()
                .contentType("text/calendar;charset=UTF-8")
                .returnResult(String.class);

        StepVerifier.create(exchangeResult.getResponseBody()).assertNext(result -> {
            assertThat(result).isEqualTo(expectedValue);
        }).verifyComplete();

        verify(calendarService).createCalendar(categoriesCaptor.capture());
        assertThat(categoriesCaptor.getValue()).containsOnly("69");
    }

}