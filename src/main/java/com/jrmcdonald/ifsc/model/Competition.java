package com.jrmcdonald.ifsc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Competition {

    private String event;
    private Instant startDate;
    private Instant endDate;

    @JsonSetter("local_start_date")
    public void setStartDate(String date) {
        startDate = convertDateStringToInstant(date);
    }

    @JsonSetter("local_end_date")
    public void setEndDate(String date) {
        endDate = convertDateStringToInstant(date);
    }

    private Instant convertDateStringToInstant(String date) {
        return LocalDate.parse(date).atStartOfDay(ZoneOffset.UTC).toInstant();
    }
}
