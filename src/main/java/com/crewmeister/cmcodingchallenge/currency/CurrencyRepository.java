package com.crewmeister.cmcodingchallenge.currency;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface CurrencyRepository extends Repository<Currency, String> {

    Page<Currency> findAll(Pageable pageable);

    Optional<Currency> findById(String id);

    Currency save(Currency currency);

    List<Currency> saveAll(Iterable<Currency> currencies);
}
