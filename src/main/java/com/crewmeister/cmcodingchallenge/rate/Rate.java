package com.crewmeister.cmcodingchallenge.rate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Rate {
    @Id
    @GeneratedValue
    public Long id;

    public String currency;

    public LocalDate date;

    @Column(precision = 20, scale = 10)
    public BigDecimal exchangeRate;

    protected Rate() {
        // JPA needs no-arg constructor
    }

    public Rate(String currency, LocalDate date, BigDecimal exchangeRate) {
        this.currency = currency;
        this.date = date;
        this.exchangeRate = exchangeRate;
    }

    public Rate(String currency, String date, BigDecimal exchangeRate) {
        this.currency = currency;
        this.date = LocalDate.parse(date);
        this.exchangeRate = exchangeRate;
    }

    @Override
    public String toString() {
        return String.format("Rate[currency='%s', date='%s', rate=%s]", currency, date, exchangeRate);
    }
}
