package com.crewmeister.cmcodingchallenge.currency;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CurrencyModelAssembler implements RepresentationModelAssembler<Currency, EntityModel<Currency>> {

    @Override
    @NonNull
    public EntityModel<Currency> toModel(@NonNull Currency currency) {
        return EntityModel.of(currency,
                linkTo(methodOn(CurrencyController.class).findOne(currency.getId())).withSelfRel(),
                linkTo(methodOn(CurrencyController.class).findAll(Pageable.unpaged())).withRel("currencies"));
    }
}