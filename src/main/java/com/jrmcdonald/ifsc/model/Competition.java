package com.jrmcdonald.ifsc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Competition {
    private String name;
    private String homepage;
    @JsonProperty("cat_id")
    private String category;
    private Instant startDate;
    private Instant endDate;

    @JsonSetter("date")
    public void setStartDate(String date) {
        startDate = convertDateStringToInstant(date);
    }

    @JsonSetter("date_end")
    public void setEndDate(String date) {
        endDate = convertDateStringToInstant(date);
    }

    private Instant convertDateStringToInstant(String date) {
        return LocalDate.parse(date).atStartOfDay(ZoneOffset.UTC).toInstant();
    }
}
