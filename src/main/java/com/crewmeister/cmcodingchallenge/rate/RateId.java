package com.crewmeister.cmcodingchallenge.rate;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
public class RateId implements Serializable {
    public String currency;
    public LocalDate date;

    protected RateId() {
        // JPA needs no-arg constructor
    }

    public RateId(String currency, LocalDate date) {
        this.currency = currency;
        this.date = date;
    }

    public RateId(String currency, String dateString) {
        this.currency = currency;
        this.date = LocalDate.parse(dateString);
    }
}
