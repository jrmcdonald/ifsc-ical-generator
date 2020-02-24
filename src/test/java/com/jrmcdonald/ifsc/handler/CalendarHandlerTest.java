package com.jrmcdonald.ifsc.handler;

import com.jrmcdonald.ifsc.config.RouterConfig;
import com.jrmcdonald.ifsc.service.calendar.CalendarService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CalendarHandlerTest {

    @Mock
    CalendarService calendarService;

    @Captor
    ArgumentCaptor<Mono<List<String>>> categoriesMonoCaptor;

    private WebTestClient client;

    @BeforeEach
    void beforeEach() {
        CalendarHandler calendarHandler = new CalendarHandler(calendarService);
        RouterConfig routerConfig = new RouterConfig();
        client = WebTestClient.bindToRouterFunction(routerConfig.route(calendarHandler)).build();
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(calendarService);
    }

    @Test
    @DisplayName("Should execute service")
    void shouldExecuteService() {
        String expectedValue = "ICALENDAR_VALUE";

        when(calendarService.createCalendar(any())).thenReturn(Mono.just(expectedValue));

        FluxExchangeResult<String> exchangeResult = client.post()
                      .uri("/calendar")
                      .bodyValue(singletonList("69"))
                      .accept(MediaType.APPLICATION_JSON)
                      .exchange()
                      .expectHeader()
                      .contentType("text/calendar")
                      .returnResult(String.class);

        StepVerifier.create(exchangeResult.getResponseBody()).assertNext(result -> {
            assertThat(result).isEqualTo(expectedValue);
        })
        .verifyComplete();

        verify(calendarService).createCalendar(categoriesMonoCaptor.capture());

        StepVerifier.create(categoriesMonoCaptor.getValue()).assertNext(categoriesList -> {
            assertThat(categoriesList).containsOnly("69");
        });
    }
}