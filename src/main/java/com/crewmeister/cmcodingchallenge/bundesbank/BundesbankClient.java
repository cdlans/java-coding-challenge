package com.crewmeister.cmcodingchallenge.bundesbank;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Stream;

@Component
public class BundesbankClient {

    @Value("${crewmeister.bundesbank.url}")
    private String url;

    public Stream<String> streamRatesAsCsv() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .header("Accept", "text/csv")
                    .build();

            return HttpClient
                    .newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofLines())
                    .body();
        } catch (URISyntaxException | IOException e) {
            throw new BundesbankException("An error occurred while retrieving exchange rates from Bundesbank", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BundesbankException("An error occurred while retrieving exchange rates from Bundesbank", e);
        }
    }
}
