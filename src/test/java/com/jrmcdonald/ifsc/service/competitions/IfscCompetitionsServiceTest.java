package com.jrmcdonald.ifsc.service.competitions;

import com.jrmcdonald.ifsc.model.Competition;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.Locale;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class IfscCompetitionsServiceTest {

    private MockWebServer mockWebServer;
    private CompetitionsService service;

    @BeforeEach
    void beforeEach() {
        mockWebServer = new MockWebServer();
        service = new IfscCompetitionsService(mockWebServer.url("localhost/").toString());
    }

    @AfterEach
    void afterEach() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("Should return competitions")
    void shouldReturnCompetitions() throws IOException {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(new String(Files.readAllBytes(Paths.get("src/test/resources/find_all.json")))));

        StepVerifier.create(service.findAll())
                .assertNext(competitionList -> {
                    assertThat(competitionList.getCompetitions()).hasSize(3);

                    List<Competition> competitions = competitionList.getCompetitions();

                    assertThat(competitions.get(0).getName()).isEqualTo("Promotional Event Gravical (B) - Singapore (SGP) 2018");
                    assertThat(competitions.get(0).getCategory()).isEqualTo("70");
                    assertThat(competitions.get(0).getHomepage()).isEqualTo("http://www.gravical.domain");
                    assertThat(competitions.get(0).getStartDate()).isEqualTo(Instant.parse("2018-01-11T00:00:00Z"));
                    assertThat(competitions.get(0).getEndDate()).isEqualTo(Instant.parse("2018-01-14T00:00:00Z"));

                    assertThat(competitions.get(1).getName()).isEqualTo("Promotional Event Gravical (B) - Tai'an (SGP) 2018");
                    assertThat(competitions.get(1).getCategory()).isEqualTo("70");
                    assertThat(competitions.get(1).getHomepage()).isEqualTo("http://www.gravical.domain");
                    assertThat(competitions.get(1).getStartDate()).isEqualTo(Instant.parse("2018-02-11T00:00:00Z"));
                    assertThat(competitions.get(1).getEndDate()).isEqualTo(Instant.parse("2018-02-14T00:00:00Z"));

                    assertThat(competitions.get(2).getName()).isEqualTo("Cliffhanger");
                    assertThat(competitions.get(2).getCategory()).isEqualTo("69");
                    assertThat(competitions.get(2).getHomepage()).isEqualTo("http://www.cliffhanger.domain");
                    assertThat(competitions.get(2).getStartDate()).isEqualTo(Instant.parse("2018-03-11T00:00:00Z"));
                    assertThat(competitions.get(2).getEndDate()).isEqualTo(Instant.parse("2018-03-14T00:00:00Z"));
                }).expectComplete().verify();
    }

    @Test
    @DisplayName("Should return competitions filtered by category")
    void shouldReturnCompetitionsFilteredByCategory() throws IOException {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(new String(Files.readAllBytes(Paths.get("src/test/resources/find_all.json")))));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        StepVerifier.create(service.findByCategory(singletonList("69")))
                .assertNext(competitionList -> {
                    assertThat(competitionList.getCompetitions()).hasSize(1);

                    List<Competition> competitions = competitionList.getCompetitions();

                    try {
                        assertThat(competitions.get(0).getName()).isEqualTo("Cliffhanger");
                        assertThat(competitions.get(0).getCategory()).isEqualTo("69");
                        assertThat(competitions.get(0).getHomepage()).isEqualTo("http://www.cliffhanger.domain");
                        assertThat(competitions.get(0).getStartDate()).isEqualTo(formatter.parse("2018-03-11").toInstant());
                        assertThat(competitions.get(0).getEndDate()).isEqualTo(formatter.parse("2018-03-14").toInstant());
                    } catch (ParseException e) {
                        fail("Unexpected exception parsing date", e);
                    }
                })
                .expectComplete()
                .verify();
    }
}
