package com.crewmeister.cmcodingchallenge.rate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RateRepository extends Repository<Rate, Long> {

    Page<Rate> findAll(Pageable page);

    Optional<Rate> findById(Long id);

//    Page<Rate> findByCurrency(String currency, Pageable pageable);

    Page<Rate> findByDate(LocalDate date, Pageable pageable);

//    Optional<Rate> findByCurrencyAndDate(String currency, LocalDate date);

    @RestResource(exported = false)
    Rate save(Rate rate);

    @RestResource(exported = false)
    List<Rate> saveAll(Iterable<Rate> rates);
}
