package com.crewmeister.cmcodingchallenge;

import com.crewmeister.cmcodingchallenge.currency.Currency;
import com.crewmeister.cmcodingchallenge.currency.CurrencyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CmCodingChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmCodingChallengeApplication.class, args);
    }

    @Bean
    public CommandLineRunner startup(CurrencyRepository repository) {
        // TODO: fetch actual currencies & rates
        return args -> {
            repository.save(new Currency("EUR"));
            repository.save(new Currency("USD"));
            repository.save(new Currency("CHF"));
        };
    }
}
