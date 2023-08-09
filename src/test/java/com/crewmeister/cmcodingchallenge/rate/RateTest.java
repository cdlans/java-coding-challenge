package com.crewmeister.cmcodingchallenge.rate;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RateTest {

    @Test
    void shouldConvertCorrectly() throws ConversionException {
        Rate rate = new Rate("USD", "2023-08-06", new BigDecimal("1.10"));

        Conversion conversion = rate.convert(new BigDecimal(110));

        assertEquals("USD", conversion.getCurrency());
        assertEquals(LocalDate.of(2023, 8, 6), conversion.getDate());
        assertEquals(new BigDecimal("1.10"), conversion.getExchangeRate());
        assertEquals(new BigDecimal("110"), conversion.getForeignAmount());
        assertEquals(new BigDecimal("100.00"), conversion.getEuroAmount());
    }

    @Test
    void shouldThrowConversionException() {
        Rate rate = new Rate("USD", "2023-08-06", new BigDecimal("0"));
        BigDecimal foreignAmount = new BigDecimal(110);

        Exception exception = assertThrows(ConversionException.class,
                () -> rate.convert(foreignAmount));

        assertTrue(exception.getMessage().contains("Could not convert currency"));
    }
}
