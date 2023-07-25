package com.crewmeister.cmcodingchallenge.currency;

import org.springframework.data.repository.ListCrudRepository;

public interface CurrencyRepository extends ListCrudRepository<Currency, String> {
}
