package com.crewmeister.cmcodingchallenge.rate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ConversionService {
    private static final int EURO_AMOUNT_SCALE = 2;
    private final Logger log = LoggerFactory.getLogger(ConversionService.class);

    Conversion conversion(Rate rate, BigDecimal foreignAmount) throws ConversionException {
        BigDecimal euroAmount;
        try {
            euroAmount = foreignAmount.divide(rate.getExchangeRate(), EURO_AMOUNT_SCALE, RoundingMode.HALF_EVEN);
        } catch (ArithmeticException cause) {
            String message = String.format("Could not convert currency %s %f to EUR with exchange rate %f",
                    rate.getCurrency(), foreignAmount, rate.getExchangeRate());
            ConversionException exception = new ConversionException(message, cause);
            log.error(message, exception);
            throw exception;
        }

        return new Conversion(rate.getCurrency(), rate.getDate(), rate.getExchangeRate(), foreignAmount, euroAmount);
    }
}
