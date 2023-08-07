package com.crewmeister.cmcodingchallenge.currency;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v2/currencies")
public class CurrencyController {

    private final CurrencyRepository currencyRepository;

    CurrencyController(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @GetMapping
    Page<Currency> findAll(Pageable pageable) {
        return currencyRepository.findAll(pageable);
    }

    @GetMapping(value = "/{id}")
    Currency findOne(@PathVariable String id) {
        return currencyRepository.findById(id).orElseThrow(() -> {
                    String message = String.format("Could not find currency '%s'", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        });
    }
}
