package com.crewmeister.cmcodingchallenge.rate;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/v1/rates")
public class RateController {

    private final RateRepository rateRepository;
    private final RateModelAssembler rateModelAssembler;
    private final PagedResourcesAssembler<Rate> pagedResourcesAssembler;


    RateController(RateRepository rateRepository,
                   RateModelAssembler rateModelAssembler,
                   PagedResourcesAssembler<Rate> pagedResourcesAssembler
    ) {
        this.rateRepository = rateRepository;
        this.rateModelAssembler = rateModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public PagedModel<EntityModel<Rate>> findAll(@RequestParam(required = false) String currency,
                                          @RequestParam(required = false) LocalDate date,
                                          Pageable pageable
    ) {
        Example<Rate> example = Example.of(new Rate(currency, date, null));
        Page<Rate> page = rateRepository.findAll(example, pageable);

        return pagedResourcesAssembler.toModel(page, rateModelAssembler);
    }

    @GetMapping(value = "/{id}")
    public EntityModel<Rate> findOne(@PathVariable Long id) {
        Rate rate = rateRepository.findById(id).orElseThrow(() -> {
                    String message = String.format("Could not find rate '%s'", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        });

        return rateModelAssembler.toModel(rate);
    }

    @GetMapping(value = "/{id}/conversion")
    public EntityModel<Conversion> convert(@PathVariable Long id, @RequestParam(defaultValue = "1") BigDecimal foreignAmount) {
        Rate rate = rateRepository.findById(id).orElseThrow(() -> {
                    String message = String.format("Could not find rate '%s'", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        });

        Conversion conversion = rateRepository.conversion(rate.getId(), foreignAmount);

        return EntityModel.of(conversion,
                linkTo(methodOn(RateController.class).convert(rate.getId(), foreignAmount)).withSelfRel(),
                linkTo(methodOn(RateController.class).findOne(rate.getId())).withRel("rate"));
    }
}
