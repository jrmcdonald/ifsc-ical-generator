package com.jrmcdonald.ifsc.filter;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import com.jrmcdonald.ifsc.logging.WebFluxLogger;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.jrmcdonald.ifsc.filter.WebFluxFilter.APPLICATION_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WebFluxFilterTest {

    private final WebFluxFilter webFluxFilter = new WebFluxFilter("test-filter-application", new WebFluxLogger());

    @Mock
    private Appender<ILoggingEvent> mockAppender;

    @Captor
    private ArgumentCaptor<LoggingEvent> captorLoggingEvent;

    private WebTestClient webTestClient;

    @Before
    public void beforeTest() {
        MDC.clear();
    }

    @Test
    @DisplayName("Should set reactor context")
    void shouldSetReactorContext() {
        WebHandler handler = exchange -> {
            exchange.getResponse().setStatusCode(HttpStatus.OK);
            return Mono.subscriberContext().map(ctx -> {
                Map<String, String> logContext = ctx.get(WebFluxFilter.LOG_CONTEXT_MAP);
                assertThat(logContext).isNotNull();
                assertThat(logContext.get(APPLICATION_KEY)).isEqualTo("test-filter-application");
                return ctx;
            }).then();
        };

        webTestClient = WebTestClient.bindToWebHandler(handler).webFilter(webFluxFilter).build();
    }

    @Test
    @DisplayName("Should log service entry and exit messages")
    void shouldLogServiceEntryAndExitMessages() {
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.addAppender(mockAppender);

        WebHandler handler = exchange -> {
            exchange.getResponse().setStatusCode(HttpStatus.OK);
            return Mono.empty();
        };

        webTestClient = WebTestClient.bindToWebHandler(handler).webFilter(webFluxFilter).build();

        webTestClient.get()
                .uri("/test")
                .exchange()
                .expectStatus()
                .isOk();

        verify(mockAppender, atLeast(1)).doAppend(captorLoggingEvent.capture());

        List<LoggingEvent> loggingEvents = captorLoggingEvent.getAllValues();

        Map<String, String> expectedMdcProperties = new HashMap<>();
        expectedMdcProperties.put(APPLICATION_KEY, "test-filter-application");

        Optional<LoggingEvent> serviceEntryEvent = loggingEvents.stream()
                .filter(event -> event.getFormattedMessage().matches("Entering service"))
                .findFirst();

        assertThat(serviceEntryEvent).isPresent();
        assertThat(serviceEntryEvent.get().getMDCPropertyMap()).containsAllEntriesOf(expectedMdcProperties);

        Optional<LoggingEvent> serviceExitEvent = loggingEvents.stream()
                .filter(event -> event.getFormattedMessage().matches("Exiting service"))
                .findFirst();

        assertThat(serviceExitEvent).isPresent();
        assertThat(serviceExitEvent.get().getMDCPropertyMap()).containsAllEntriesOf(expectedMdcProperties);

        logger.detachAppender(mockAppender);
    }
}