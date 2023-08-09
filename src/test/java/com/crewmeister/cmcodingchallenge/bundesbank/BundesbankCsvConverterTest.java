package com.crewmeister.cmcodingchallenge.bundesbank;

import com.crewmeister.cmcodingchallenge.domain.Rate;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BundesbankCsvConverterTest {

    private final BundesbankCsvConverter bundesbankCsvConverter = new BundesbankCsvConverter();

    @Test
    void shouldRetrieveAllCurrenciesAndRates() throws IOException {
        Path path = ResourceUtils.getFile("classpath:bundesbank.csv").toPath();
        Stream<String> csvLines = Files.lines(path);

        List<Rate> rates = bundesbankCsvConverter.convertToRates(csvLines).toList();

        assertEquals(281, rates.size());
        assertTrue(rates.contains(new Rate("AUD", LocalDate.parse("2023-07-31"), new BigDecimal("1.6436"))));
        assertTrue(rates.contains(new Rate("LVL", LocalDate.parse("2013-12-24"), new BigDecimal("0.702200"))));
        assertTrue(rates.contains(new Rate("ZAR", LocalDate.parse("2023-08-08"), new BigDecimal("20.6870"))));
    }
}
