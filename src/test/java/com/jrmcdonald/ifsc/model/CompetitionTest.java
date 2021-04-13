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
        competition.setEvent("Cliffhanger");
        competition.setStartDate("2021-04-16");
        competition.setEndDate("2021-04-17");

        assertThat(competition.getEvent()).isEqualTo("Cliffhanger");
        assertThat(competition.getStartDate()).isEqualTo(Instant.parse("2021-04-16T00:00:00Z"));
        assertThat(competition.getEndDate()).isEqualTo(Instant.parse("2021-04-17T00:00:00Z"));
    }

    @Test
    @DisplayName("Should model Competition by all arguments constructor")
    void shouldModelCompetitionByAllArgumentsConstructor() {
        Competition competition = new Competition("Cliffhanger", Instant.parse("2018-03-11T00:00:00Z"), Instant.parse("2018-03-12T00:00:00Z"));

        assertThat(competition.getEvent()).isEqualTo("Cliffhanger");
        assertThat(competition.getStartDate()).isEqualTo(Instant.parse("2018-03-11T00:00:00Z"));
        assertThat(competition.getEndDate()).isEqualTo(Instant.parse("2018-03-12T00:00:00Z"));
    }
}