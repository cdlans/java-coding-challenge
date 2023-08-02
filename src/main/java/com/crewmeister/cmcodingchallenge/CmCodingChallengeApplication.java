package com.crewmeister.cmcodingchallenge;

import com.crewmeister.cmcodingchallenge.bundesbank.BundesbankService;
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
    public CommandLineRunner startup(BundesbankService bundesbankService) {
        return args -> bundesbankService.initializeRates();
    }
}
