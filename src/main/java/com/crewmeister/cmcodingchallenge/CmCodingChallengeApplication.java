package com.crewmeister.cmcodingchallenge;

import com.crewmeister.cmcodingchallenge.bundesbank.BundesbankService;
import com.crewmeister.cmcodingchallenge.currency.Currency;
import com.crewmeister.cmcodingchallenge.currency.CurrencyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class CmCodingChallengeApplication {
    private static final Logger LOG = LoggerFactory.getLogger(CmCodingChallengeApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CmCodingChallengeApplication.class, args);
    }

    @Bean
    public CommandLineRunner startup(BundesbankService bundesbankService, CurrencyRepository repository) {
        return args -> {
            LOG.info("Retrieving currencies...");

            List<String> currencyStrings = bundesbankService.getCurrencyStrings();

            LOG.info("Retrieved {} currencies", currencyStrings.size());

            List<Currency> currencies = currencyStrings.stream()
                    .map(Currency::new)
                    .toList();

            repository.saveAll(currencies);

            LOG.info("Saved currencies to database");
        };
    }
}
