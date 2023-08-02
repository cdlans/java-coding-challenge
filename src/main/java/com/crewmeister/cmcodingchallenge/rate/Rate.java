package com.crewmeister.cmcodingchallenge.rate;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Rate {
    @EmbeddedId
    public RateId id;

    @Column(precision = 20, scale = 10)
    public BigDecimal exchangeRate;

    protected Rate() {
        // JPA needs no-arg constructor
    }

    public Rate(String currency, LocalDate date, BigDecimal exchangeRate) {
        this.id = new RateId(currency, date);
        this.exchangeRate = exchangeRate;
    }

    @Override
    public String toString() {
        return String.format("Rate[currency='%s', date='%s', rate=%s]", id.currency, id.date, exchangeRate);
    }
}
