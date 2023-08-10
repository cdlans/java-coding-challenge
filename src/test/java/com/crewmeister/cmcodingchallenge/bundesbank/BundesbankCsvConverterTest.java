package com.crewmeister.cmcodingchallenge.bundesbank;

import com.crewmeister.cmcodingchallenge.domain.Rate;
import org.junit.jupiter.api.Test;
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
}
