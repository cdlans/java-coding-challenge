package com.crewmeister.cmcodingchallenge.bundesbank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BundesbankServiceTest {

    private BundesbankService bundesbankService;
    private BundesbankClient bundesbankClient;

    @BeforeEach
    void setup() {
        bundesbankClient = mock(BundesbankClient.class);
        bundesbankService = new BundesbankService(bundesbankClient);
    }

    @Test
    void shouldRetrieveAllCurrencies() throws IOException {
        Path path = ResourceUtils.getFile("classpath:bundesbank.json").toPath();
        String response = Files.readString(path);
        when(bundesbankClient.getCurrenciesJson()).thenReturn(response);

        List<String> currencyStrings = bundesbankService.getCurrencyStrings();
        assertEquals(42, currencyStrings.size());
        assertEquals("AUD", currencyStrings.get(0));
        assertEquals("ZAR", currencyStrings.get(41));
    }
}
