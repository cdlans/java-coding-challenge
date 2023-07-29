package com.crewmeister.cmcodingchallenge.currency;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController()
@RequestMapping("/api")
public class CurrencyController {

    private final CurrencyRepository currencyRepository;

    CurrencyController(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @GetMapping("/currencies")
    public List<Currency> findAll() {
        return currencyRepository.findAll();
    }

    @GetMapping("/currencies/{id}")
    public Currency findById(@PathVariable String id) {
        return currencyRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(NOT_FOUND, String.format("Unable to find currency with ID '%s'", id)));
    }
}
