package com.crewmeister.cmcodingchallenge.bundesbank;

import com.crewmeister.cmcodingchallenge.domain.Rate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.stream.Stream;

@Component
public class BundesbankCsvConverter {

    private final Logger log = LoggerFactory.getLogger(BundesbankCsvConverter.class);

    public Stream<Rate> convertToRates(Stream<String> csvLines) {
        return csvLines
                .skip(1)    // skip header
                .filter(line -> !line.isBlank())
                .map(line -> line.split(";"))
                .filter(values -> values.length >= 9)    // filter incomplete lines
                .filter(values -> !values[8].equals("."))    // filter empty conversion rates
                .map(values -> {
                    String currency = values[2];
                    String dateString = values[7];
                    String rateString = values[8];

                    LocalDate date;
                    try {
                        date = LocalDate.parse(dateString);
                    } catch (DateTimeParseException e) {
                        log.error("Could not parse date '{}'", dateString, e);
                        return null;
                    }

                    BigDecimal rate;
                    try {
                        rate = new BigDecimal(rateString);
                    } catch (NumberFormatException e) {
                        log.error("Could not parse exchange rate '{}'", rateString, e);
                        return null;
                    }

                    return new Rate(currency, date, rate);
                })
                .filter(Objects::nonNull);    // filter the `null` Rates in case there were parse errors
    }
}
