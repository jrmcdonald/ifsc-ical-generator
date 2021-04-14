package com.jrmcdonald.ifsc.service.competitions;

import com.jrmcdonald.ifsc.model.Competition;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IfscCompetitionsServiceTest {

    @Mock
    IfscCompetitionsConfig config;

    private MockWebServer mockWebServer;
    private CompetitionsService service;

    @BeforeEach
    void beforeEach() {
        mockWebServer = new MockWebServer();
        when(config.getHost()).thenReturn(mockWebServer.url("/").toString());
        service = new IfscCompetitionsService(config);
    }

    @AfterEach
    void afterEach() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("Should return competitions")
    void shouldReturnCompetitions() throws Exception {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(HttpStatus.OK.value())
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(Files.readString(Paths.get("src/test/resources/data/ifscResponseInput.json"))));

        StepVerifier.create(service.findAll("388"))
                .assertNext(competitionList -> {
                    assertThat(competitionList.getEvents()).hasSize(3);

                    List<Competition> competitions = competitionList.getEvents();

                    assertThat(competitions.get(0).getEvent()).isEqualTo("IFSC - Climbing World Cup (B) - Meiringen (SUI) 2021");
                    assertThat(competitions.get(0).getStartDate()).isEqualTo(Instant.parse("2021-04-16T00:00:00Z"));
                    assertThat(competitions.get(0).getEndDate()).isEqualTo(Instant.parse("2021-04-17T00:00:00Z"));

                    assertThat(competitions.get(1).getEvent()).isEqualTo("IFSC - Climbing World Cup (B) - Salt Lake City (USA) 2021");
                    assertThat(competitions.get(1).getStartDate()).isEqualTo(Instant.parse("2021-05-21T00:00:00Z"));
                    assertThat(competitions.get(1).getEndDate()).isEqualTo(Instant.parse("2021-05-22T00:00:00Z"));

                    assertThat(competitions.get(2).getEvent()).isEqualTo("IFSC - Climbing World Cup (B,S) - Salt Lake City (USA) 2021");
                    assertThat(competitions.get(2).getStartDate()).isEqualTo(Instant.parse("2021-05-28T00:00:00Z"));
                    assertThat(competitions.get(2).getEndDate()).isEqualTo(Instant.parse("2021-05-30T00:00:00Z"));
                })
                .verifyComplete();

        assertThat(mockWebServer.takeRequest().getPath()).isEqualTo("/results-api.php?api=season_leagues_calendar&league=388");
    }
}
