package com.crewmeister.cmcodingchallenge.web;

import com.crewmeister.cmcodingchallenge.domain.ConversionDto;
import com.crewmeister.cmcodingchallenge.domain.Rate;
import com.crewmeister.cmcodingchallenge.domain.RateRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = BasePath.BASE_PATH + "/rates", produces = "application/json")
@Validated
public class RateController {

    private final RateRepository rateRepository;
    private final RateModelAssembler rateModelAssembler;
    private final PagedResourcesAssembler<Rate> pagedResourcesAssembler;

    RateController(RateRepository rateRepository,
                   RateModelAssembler rateModelAssembler,
                   PagedResourcesAssembler<Rate> pagedResourcesAssembler) {
        this.rateRepository = rateRepository;
        this.rateModelAssembler = rateModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Operation(summary = "Find all rates, optionally filtered by currency and/or rate")
    @GetMapping
    public PagedModel<EntityModel<Rate>> findAll(
            @Parameter(description="The three-letter currency code to filter by, for example 'USD'")
            @RequestParam(required = false) String currency,
            @Parameter(description="The date to filter by formatted as yyyy-mm-dd, for example '2021-12-31'")
            @RequestParam(required = false) LocalDate date,
            @ParameterObject Pageable pageable
    ) {
        Example<Rate> example = Example.of(new Rate(currency, date, null));
        Page<Rate> page = rateRepository.findAll(example, pageable);

        return pagedResourcesAssembler.toModel(page, rateModelAssembler);
    }

    @Operation(summary = "Find a rate by ID")
    @GetMapping(value = "/{id}")
    public EntityModel<Rate> findOne(
            @Parameter(description="The numeric ID of a rate, for example '42'")
            @PathVariable Long id
    ) {
        Rate rate = rateRepository.findById(id).orElseThrow(() -> {
                    String message = String.format("Could not find rate '%s'", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        });

        return rateModelAssembler.toModel(rate);
    }

    @Operation(summary = "Get a conversion to EUR for a foreign currency")
    @GetMapping(value = "/{id}/conversion")
    public EntityModel<ConversionDto> conversion(
            @Parameter(description="The numeric ID of a rate, for example '42'")
            @PathVariable Long id,
            @Parameter(description="The amount of the foreign currency to convert, for example '200'")
            @Min(0)
            @Max(Long.MAX_VALUE)    // Set a maximum to prevent a DoS attack
            @RequestParam(defaultValue = "1") BigDecimal foreignAmount
    ) {
        Rate rate = rateRepository.findById(id).orElseThrow(() -> {
            String message = String.format("Could not find rate '%s'", id);
            return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        });

        ConversionDto conversion = rate.convert(foreignAmount);

        return EntityModel.of(conversion,
                linkTo(methodOn(RateController.class).conversion(rate.getId(), foreignAmount)).withSelfRel(),
                linkTo(methodOn(RateController.class).findOne(rate.getId())).withRel("rate"));
    }
}
