package com.crewmeister.cmcodingchallenge.bundesbank;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class BundesbankClient {

    private final WebClient client;

    BundesbankClient() {
        client = WebClient.builder()
                .baseUrl("https://api.statistiken.bundesbank.de/rest/data")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    String getCurrenciesJson() {
        return client.get()
                // TODO: remove `?lastNObservations=10`
                .uri("/BBEX3/D..EUR.BB.AC.000?lastNObservations=10")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
