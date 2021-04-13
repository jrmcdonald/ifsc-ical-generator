package com.jrmcdonald.ifsc.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

class CompetitionListTest {

    @Test
    @DisplayName("Should model CompetitionList by default constructor")
    void shouldModelCompetitionListByDefaultConstructor() {
        Competition expectedCompetition = new Competition("Cliffhanger", Instant.parse("2018-03-11T00:00:00Z"), Instant.parse("2018-03-12T00:00:00Z"));
        
        CompetitionList competitionList = new CompetitionList();
        competitionList.setEvents(singletonList(expectedCompetition));

        assertThat(competitionList.getEvents()).hasSize(1);
        assertThat(competitionList.getEvents().get(0)).isEqualTo(expectedCompetition);
    }

    @Test
    @DisplayName("Should model CompetitionList by all arguments constructor")
    void shouldModelCompetitionListByAllArgumentsConstructor() {
        Competition expectedCompetition = new Competition("Cliffhanger", Instant.parse("2018-03-11T00:00:00Z"), Instant.parse("2018-03-12T00:00:00Z"));

        CompetitionList competitionList = new CompetitionList(singletonList(expectedCompetition));

        assertThat(competitionList.getEvents()).hasSize(1);
        assertThat(competitionList.getEvents().get(0)).isEqualTo(expectedCompetition);
    }
}