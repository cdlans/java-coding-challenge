package com.crewmeister.cmcodingchallenge.rate;

import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RateRepository extends Repository<Rate, Long> {

    List<Rate> findAll();

    Optional<Rate> findById(Long id);

    Optional<Rate> findByCurrencyAndDate(String currency, LocalDate date);

    @RestResource(exported = false)
    Rate save(Rate rate);

    @RestResource(exported = false)
    List<Rate> saveAll(Iterable<Rate> rates);
}
