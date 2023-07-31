package com.crewmeister.cmcodingchallenge.currency;

import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

public interface CurrencyRepository extends Repository<Currency, String> {

    List<Currency> findAll();

    Optional<Currency> findById(String id);

    @RestResource(exported = false)
    Currency save(Currency currency);

    @RestResource(exported = false)
    List<Currency> saveAll(Iterable<Currency> currencies);
}
