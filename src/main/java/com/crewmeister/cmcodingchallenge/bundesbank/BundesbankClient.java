package com.crewmeister.cmcodingchallenge.bundesbank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class BundesbankClient {

    private static final int MAX_CODEC_BYTES = 100 * 1024 * 1024;
    private final Logger log = LoggerFactory.getLogger(BundesbankClient.class);
    private final WebClient client;

    BundesbankClient() {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(MAX_CODEC_BYTES))
                .build();

        client = WebClient.builder()
                .exchangeStrategies(strategies)
                .baseUrl("https://api.statistiken.bundesbank.de/rest/data")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    String retrieveRatesJson() {
        log.info("Fetching data from Bundesbank...");

        String json = client.get()
//                .uri("/BBEX3/D..EUR.BB.AC.000")
                .uri("/BBEX3/D..EUR.BB.AC.000?lastNObservations=100")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info("Finished fetching data from Bundesbank");
        return json;
    }
}
