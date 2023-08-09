package com.crewmeister.cmcodingchallenge.web;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Conversion {
    String currency;
    LocalDate date;
    BigDecimal exchangeRate;
    BigDecimal foreignAmount;
    BigDecimal euroAmount;

    public Conversion(String currency, LocalDate date, BigDecimal exchangeRate, BigDecimal foreignAmount,
                      BigDecimal euroAmount) {
        this.currency = currency;
        this.date = date;
        this.exchangeRate = exchangeRate;
        this.foreignAmount = foreignAmount;
        this.euroAmount = euroAmount;
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

    public BigDecimal getForeignAmount() {
        return foreignAmount;
    }

    public BigDecimal getEuroAmount() {
        return euroAmount;
    }
}
