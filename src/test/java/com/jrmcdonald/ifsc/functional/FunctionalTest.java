package com.jrmcdonald.ifsc.functional;

import com.jrmcdonald.ifsc.Application;
import com.jrmcdonald.ifsc.extensions.MockWebServerExtension;
import com.jrmcdonald.ifsc.functional.config.FunctionalTestConfiguration;
import com.jrmcdonald.ifsc.server.MockWebServerWrapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import okhttp3.mockwebserver.MockResponse;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith({SpringExtension.class, MockWebServerExtension.class})
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ContextConfiguration(initializers = FunctionalTestConfiguration.Initializer.class)
@ActiveProfiles("test")
class FunctionalTest {

    @LocalServerPort
    private int port;

    private WebTestClient client;

    @BeforeEach
    void beforeEach() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
    }

    @Test
    @DisplayName("Should execute successfully")
    void shouldExecuteSuccessfully(MockWebServerWrapper wrapper) throws IOException {
        wrapper.getServer()
                .enqueue(
                        new MockResponse()
                                .setResponseCode(HttpStatus.OK.value())
                                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .setBody(Files.readString(Paths.get("src/test/resources/data/ifscResponseInput.json"))));

        client.get()
                .uri("/calendar")
                .exchange()
                .expectHeader()
                .contentType("text/calendar;charset=UTF-8")
                .expectStatus().isOk();
    }
}
