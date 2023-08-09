package com.crewmeister.cmcodingchallenge.web;

import com.crewmeister.cmcodingchallenge.domain.RateRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static com.crewmeister.cmcodingchallenge.web.BasePath.BASE_PATH;

@RestController
@RequestMapping(value = BASE_PATH + "/currencies", produces = "application/json")
public class CurrencyController {

    private final RateRepository rateRepository;
    private final CurrencyModelAssembler currencyModelAssembler;
    private final PagedResourcesAssembler<Currency> pagedResourcesAssembler;

    CurrencyController(RateRepository rateRepository,
                       CurrencyModelAssembler currencyModelAssembler,
                       PagedResourcesAssembler<Currency> pagedResourcesAssembler) {
        this.rateRepository = rateRepository;
        this.currencyModelAssembler = currencyModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Operation(summary = "Find all currencies")
    @GetMapping
    public PagedModel<EntityModel<Currency>> findAll(@ParameterObject Pageable pageable) {
        Page<Currency> page = rateRepository.findAllCurrencies(pageable)
                .map(Currency::new);

        return pagedResourcesAssembler.toModel(page, currencyModelAssembler);
    }

    @Operation(summary = "Find a currency by ID")
    @GetMapping(value = "/{id}")
    public EntityModel<Currency> findOne(
            @Parameter(description="The three-letter currency code, for example 'USD'")
            @PathVariable String id
    ) {
        String currency = rateRepository.findCurrencyById(id).orElseThrow(() -> {
                    String message = String.format("Could not find currency '%s'", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        });

        return currencyModelAssembler.toModel(new Currency(currency));
    }
}
