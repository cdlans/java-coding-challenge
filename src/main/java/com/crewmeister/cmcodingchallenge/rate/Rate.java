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
    private Long id;

    private String currency;

    private LocalDate date;

    @Column(precision = 20, scale = 10)
    private BigDecimal exchangeRate;

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

    public Long getId() {
        return id;
    }

    public String getCurrency() {
        return currency;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }
}
