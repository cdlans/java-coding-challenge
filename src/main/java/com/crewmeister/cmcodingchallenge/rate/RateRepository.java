package com.crewmeister.cmcodingchallenge.rate;

import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

public interface RateRepository extends Repository<Rate, RateId> {

    List<Rate> findAll();

    Optional<Rate> findById(RateId id);

    @RestResource(exported = false)
    Rate save(Rate rate);

    @RestResource(exported = false)
    List<Rate> saveAll(Iterable<Rate> rates);
}
