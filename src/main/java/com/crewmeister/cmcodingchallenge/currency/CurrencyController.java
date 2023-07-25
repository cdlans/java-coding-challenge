package com.crewmeister.cmcodingchallenge.currency;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/api")
public class CurrencyController {

    @GetMapping("/currencies")
    public List<Currency> getCurrencies() {
        return List.of(
                new Currency("EUR"),
                new Currency("USD"),
                new Currency("CHF")
        );
    }
}
