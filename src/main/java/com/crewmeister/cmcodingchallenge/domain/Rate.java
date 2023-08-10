package com.crewmeister.cmcodingchallenge.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Rate {

    private static final int EURO_AMOUNT_SCALE = 2;

    @Id
    @GeneratedValue
    private Long id;

    private String currency;

    private LocalDate date;

    @Column(precision = 20, scale = 6)
    private BigDecimal exchangeRate;

    protected Rate() {
        // JPA needs no-arg constructor
    }

    public Rate(String currency, LocalDate date, BigDecimal exchangeRate) {
        this.currency = currency;
        this.date = date;
        this.exchangeRate = exchangeRate;
    }

    public Rate(String currency, String date, String exchangeRate) {
        this.currency = currency;
        this.date = LocalDate.parse(date);
        this.exchangeRate = new BigDecimal(exchangeRate);
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

    public Conversion convert(BigDecimal foreignAmount) throws ConversionException {
        BigDecimal euroAmount;
        try {
            euroAmount = foreignAmount.divide(getExchangeRate(), EURO_AMOUNT_SCALE, RoundingMode.HALF_EVEN);
        } catch (ArithmeticException cause) {
            String message = String.format("Could not convert currency %s %f to EUR with exchange rate %f",
                    getCurrency(), foreignAmount, getExchangeRate());
            throw new ConversionException(message, cause);
        }

        return new Conversion(getCurrency(), getDate(), getExchangeRate(), foreignAmount, euroAmount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rate rate = (Rate) o;
        return Objects.equals(currency, rate.currency)
                && Objects.equals(date, rate.date)
                && Objects.equals(exchangeRate, rate.exchangeRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, date, exchangeRate);
    }
}
