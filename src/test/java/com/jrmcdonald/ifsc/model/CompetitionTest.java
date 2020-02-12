package com.jrmcdonald.ifsc.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class CompetitionTest {

    @Test
    @DisplayName("Should model Competition by default constructor")
    void shouldModelCompetitionByDefaultConstructor() {
        Competition competition = new Competition();
        competition.setName("Cliffhanger");
        competition.setHomepage("http://www.cliffhanger.domain");
        competition.setCategory("69");
        competition.setStartDate("2018-03-11");
        competition.setEndDate("2018-03-12");

        assertThat(competition.getName()).isEqualTo("Cliffhanger");
        assertThat(competition.getCategory()).isEqualTo("69");
        assertThat(competition.getHomepage()).isEqualTo("http://www.cliffhanger.domain");
        assertThat(competition.getStartDate()).isEqualTo(Instant.parse("2018-03-11T00:00:00Z"));
        assertThat(competition.getEndDate()).isEqualTo(Instant.parse("2018-03-12T00:00:00Z"));
    }

    @Test
    @DisplayName("Should model Competition by all arguments constructor")
    void shouldModelCompetitionByAllArgumentsConstructor() {
        Competition competition = new Competition("Cliffhanger", "http://www.cliffhanger.domain",
                "69", Instant.parse("2018-03-11T00:00:00Z"), Instant.parse("2018-03-12T00:00:00Z"));

        assertThat(competition.getName()).isEqualTo("Cliffhanger");
        assertThat(competition.getCategory()).isEqualTo("69");
        assertThat(competition.getHomepage()).isEqualTo("http://www.cliffhanger.domain");
        assertThat(competition.getStartDate()).isEqualTo(Instant.parse("2018-03-11T00:00:00Z"));
        assertThat(competition.getEndDate()).isEqualTo(Instant.parse("2018-03-12T00:00:00Z"));
    }
}