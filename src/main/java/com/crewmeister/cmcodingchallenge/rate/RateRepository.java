package com.crewmeister.cmcodingchallenge.rate;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RateRepository extends Repository<Rate, Long> {

    Page<Rate> findAll(Pageable page);

    Page<Rate> findAll(Example<Rate> rateExample, Pageable pageable);

    Optional<Rate> findById(Long id);

    Optional<Rate> findByCurrencyAndDate(String currency, LocalDate date);

    Rate save(Rate rate);

    List<Rate> saveAll(Iterable<Rate> rates);

    @Query("SELECT DISTINCT r.currency FROM Rate r")
    Page<String> findAllCurrencies(Pageable page);

    @Query("SELECT DISTINCT r.currency FROM Rate r WHERE r.currency = :id")
    Optional<String> findCurrencyById(String id);
}
