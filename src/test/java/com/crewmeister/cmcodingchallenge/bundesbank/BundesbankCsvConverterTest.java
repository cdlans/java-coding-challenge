package com.crewmeister.cmcodingchallenge.bundesbank;

import com.crewmeister.cmcodingchallenge.domain.Rate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
        assertTrue(rates.contains(new Rate("AUD", "2023-07-31", "1.6436")));
        assertTrue(rates.contains(new Rate("LVL", "2013-12-24", "0.702200")));
        assertTrue(rates.contains(new Rate("ZAR", "2023-08-08", "20.6870")));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "BBK:BBEX3(1.0);D;AUD;EUR;BB;AC;000;this is not a date;1.2345;;-0.4",    // malformed date
            "BBK:BBEX3(1.0);D;AUD;EUR;BB;AC;000;2000-01-01;this is not a number;;-0.4",    // malformed exchange rate
            "BBK:BBEX3(1.0);D;;EUR;BB;AC;000;2000-01-01;1.2345;;-0.4",    // blank currency

    })
    void shouldHandleMalformedInput(String malformedLine) {
        Stream<String> csvLines = Stream.of("HEADER", malformedLine);

        List<Rate> rates = bundesbankCsvConverter.convertToRates(csvLines).toList();

        assertEquals(0, rates.size());
    }
}
