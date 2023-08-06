package com.crewmeister.cmcodingchallenge.rate;

import com.crewmeister.cmcodingchallenge.currency.Currency;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Rate {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Currency currency;

    private LocalDate date;

    @Column(precision = 20, scale = 10)
    private BigDecimal exchangeRate;

    protected Rate() {
        // JPA needs no-arg constructor
    }

    public Rate(String currency, LocalDate date, BigDecimal exchangeRate) {
        this.currency = new Currency(currency);
        this.date = date;
        this.exchangeRate = exchangeRate;
    }

    public Rate(String currency, String date, BigDecimal exchangeRate) {
        this.currency = new Currency(currency);
        this.date = LocalDate.parse(date);
        this.exchangeRate = exchangeRate;
    }

    public Long getId() {
        return id;
    }

    public String getCurrency() {
        return currency.getCode();
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    @Override
    public String toString() {
        return String.format("Rate[currency='%s', date='%s', rate=%s]", currency, date, exchangeRate);
    }
}
