package com.jrmcdonald.ifsc.filter;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import com.jrmcdonald.ifsc.Application;
import com.jrmcdonald.ifsc.service.calendar.CalendarService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.jrmcdonald.ifsc.filter.WebFluxContextFilter.APPLICATION_KEY;
import static com.jrmcdonald.ifsc.filter.WebFluxContextFilter.DURATION_KEY;
import static com.jrmcdonald.ifsc.filter.WebFluxContextFilter.HTTP_METHOD_KEY;
import static com.jrmcdonald.ifsc.filter.WebFluxContextFilter.HTTP_STATUS_CODE_KEY;
import static com.jrmcdonald.ifsc.filter.WebFluxContextFilter.URI_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@AutoConfigureWebTestClient
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
class WebFluxContextFilterTest {

    @MockBean
    CalendarService calendarService;

    @Mock
    private Appender<ILoggingEvent> mockAppender;

    @Captor
    private ArgumentCaptor<LoggingEvent> captorLoggingEvent;

    @Autowired
    private WebTestClient webTestClient;

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(calendarService);
    }

    @Test
    @DisplayName("Should log service entry message")
    void shouldLogServiceEntryMessage() {
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.addAppender(mockAppender);

        when(calendarService.createCalendar()).thenReturn(Mono.just("ICALENDAR_VALUE"));

        webTestClient.get()
                .uri("/calendar")
                .exchange()
                .expectStatus()
                .isOk();

        verify(mockAppender, atLeast(1)).doAppend(captorLoggingEvent.capture());
        verify(calendarService).createCalendar();

        List<LoggingEvent> loggingEvents = captorLoggingEvent.getAllValues();

        Map<String, String> expectedMdcProperties = Map.of(
                APPLICATION_KEY, "ifsc-ical-generator",
                HTTP_METHOD_KEY, "GET",
                URI_KEY, "/calendar"
        );

        Optional<LoggingEvent> serviceEntryEvent = loggingEvents.stream()
                .filter(event -> event.getFormattedMessage().matches("Entering service"))
                .findFirst();

        assertThat(serviceEntryEvent).isPresent();
        assertThat(serviceEntryEvent.get().getMDCPropertyMap()).containsAllEntriesOf(expectedMdcProperties);

        logger.detachAppender(mockAppender);
    }

    @Test
    @DisplayName("Should log service exit message")
    void shouldLogServiceExitMessage() {
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.addAppender(mockAppender);

        when(calendarService.createCalendar()).thenReturn(Mono.just("ICALENDAR_VALUE"));

        webTestClient.get()
                .uri("/calendar")
                .exchange()
                .expectStatus()
                .isOk();

        verify(mockAppender, atLeast(1)).doAppend(captorLoggingEvent.capture());
        verify(calendarService).createCalendar();

        List<LoggingEvent> loggingEvents = captorLoggingEvent.getAllValues();

        Map<String, String> expectedMdcProperties = Map.of(
                APPLICATION_KEY, "ifsc-ical-generator",
                HTTP_METHOD_KEY, "GET",
                HTTP_STATUS_CODE_KEY, "200",
                URI_KEY, "/calendar"
        );

        Optional<LoggingEvent> serviceEntryEvent = loggingEvents.stream()
                .filter(event -> event.getFormattedMessage().matches("Exiting service"))
                .findFirst();

        assertThat(serviceEntryEvent).isPresent();
        assertThat(serviceEntryEvent.get().getMDCPropertyMap()).containsAllEntriesOf(expectedMdcProperties);
        assertThat(serviceEntryEvent.get().getMDCPropertyMap()).containsKey(DURATION_KEY);
        assertThat(serviceEntryEvent.get().getMDCPropertyMap().get(DURATION_KEY)).isNotNull();

        logger.detachAppender(mockAppender);
    }
}