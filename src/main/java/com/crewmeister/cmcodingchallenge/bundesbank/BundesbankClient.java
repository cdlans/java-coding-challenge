package com.crewmeister.cmcodingchallenge.bundesbank;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class BundesbankClient {

    private static final int MAX_CODEC_BYTES = 100 * 1024 * 1024;

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

    String getCurrenciesJson() {
        return client.get()
                .uri("/BBEX3/D..EUR.BB.AC.000")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
