package com.crewmeister.cmcodingchallenge;

import com.crewmeister.cmcodingchallenge.bundesbank.BundesbankClient;
import com.crewmeister.cmcodingchallenge.bundesbank.BundesbankCsvConverter;
import com.crewmeister.cmcodingchallenge.domain.RateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@SpringBootApplication
public class CmCodingChallengeApplication {

    private static final int LOG_INTERVAL = 10_000;
    private final Logger log = LoggerFactory.getLogger(CmCodingChallengeApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CmCodingChallengeApplication.class, args);
    }

    @Bean
    public CommandLineRunner startup(BundesbankClient client, BundesbankCsvConverter converter,
                                     RateRepository repository) {
        return args -> {
            log.info("Importing exchange rates from Bundesbank...");
            AtomicLong counter = new AtomicLong();
            Instant start = Instant.now();

            Stream<String> csvLines = client.streamRatesAsCsv();
            converter.convertToRates(csvLines).forEach(rate -> {
                repository.save(rate);

                long c = counter.incrementAndGet();
                if (c % LOG_INTERVAL == 0) {
                    log.info("Imported {} exchange rates...", c);
                }
            });

            Instant end = Instant.now();
            long seconds = Duration.between(start, end).getSeconds();
            log.info("Finished importing {} exchange rates from Bundesbank in {} seconds", counter.get(), seconds);
        };
    }
}
