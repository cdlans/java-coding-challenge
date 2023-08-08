package com.crewmeister.cmcodingchallenge.currency;

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

@RestController
@RequestMapping(value = "/api/v1/currencies", produces = "application/json")
public class CurrencyController {

    private final CurrencyRepository currencyRepository;
    private final CurrencyModelAssembler currencyModelAssembler;
    private final PagedResourcesAssembler<Currency> pagedResourcesAssembler;

    CurrencyController(CurrencyRepository currencyRepository,
                       CurrencyModelAssembler currencyModelAssembler,
                       PagedResourcesAssembler<Currency> pagedResourcesAssembler) {
        this.currencyRepository = currencyRepository;
        this.currencyModelAssembler = currencyModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Operation(summary = "Find all currencies")
    @GetMapping
    public PagedModel<EntityModel<Currency>> findAll(@ParameterObject Pageable pageable) {
        Page<Currency> page = currencyRepository.findAll(pageable);

        return pagedResourcesAssembler.toModel(page, currencyModelAssembler);
    }

    @Operation(summary = "Find a currency by ID")
    @GetMapping(value = "/{id}")
    public EntityModel<Currency> findOne(
            @Parameter(description="The three-letter currency code, for example 'USD'")
            @PathVariable String id
    ) {
        Currency currency = currencyRepository.findById(id).orElseThrow(() -> {
                    String message = String.format("Could not find currency '%s'", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        });

        return currencyModelAssembler.toModel(currency);
    }
}
