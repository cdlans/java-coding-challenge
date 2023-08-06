package com.crewmeister.cmcodingchallenge.bundesbank;

import com.crewmeister.cmcodingchallenge.currency.Currency;
import com.crewmeister.cmcodingchallenge.currency.CurrencyRepository;
import com.crewmeister.cmcodingchallenge.rate.Rate;
import com.crewmeister.cmcodingchallenge.rate.RateRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class BundesbankServiceTest {

    @Autowired private CurrencyRepository currencyRepository;
    @Autowired private RateRepository rateRepository;
    @Autowired private BundesbankService bundesbankService;
    @MockBean private BundesbankClient bundesbankClient;
    @MockBean private CommandLineRunner commandLineRunner; // Avoid fetching data from Bundesbank

    @Test
    void shouldRetrieveAllCurrenciesAndRates() throws IOException {
        Path path = ResourceUtils.getFile("classpath:bundesbank.json").toPath();
        String response = Files.readString(path);
        when(bundesbankClient.retrieveRatesJson()).thenReturn(response);

        bundesbankService.initializeRates();

        List<Currency> currencies = currencyRepository.findAll(Pageable.unpaged()).getContent();
        assertEquals(42, currencies.size());
        assertEquals("AUD", currencies.get(0).getId());
        assertEquals("ZAR", currencies.get(41).getId());

        List<Rate> rates = rateRepository.findAll(Pageable.unpaged()).getContent();
        assertEquals(251, rates.size());

//        Rate rate = rateRepository.findByCurrencyAndDate("AUD", LocalDate.parse("2023-07-17")).orElseThrow();
//        assertEquals(0, new BigDecimal("1.6487").compareTo(rate.getExchangeRate()));
//
//        rate = rateRepository.findByCurrencyAndDate("LVL", LocalDate.parse("2013-12-24")).orElseThrow();
//        assertEquals(0, new BigDecimal("0.702200").compareTo(rate.getExchangeRate()));
//
//        rate = rateRepository.findByCurrencyAndDate("ZAR", LocalDate.parse("2023-07-24")).orElseThrow();
//        assertEquals(0, new BigDecimal("19.7927").compareTo(rate.getExchangeRate()));
    }
}
