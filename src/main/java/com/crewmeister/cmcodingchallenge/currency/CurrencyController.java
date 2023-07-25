package com.crewmeister.cmcodingchallenge.currency;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/api")
public class CurrencyController {

    private final CurrencyRepository currencyRepository;

    CurrencyController(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @GetMapping("/currencies")
    public List<Currency> getCurrencies() {
        return currencyRepository.findAll();
    }
}
