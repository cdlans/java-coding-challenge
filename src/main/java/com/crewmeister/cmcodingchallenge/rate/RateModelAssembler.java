package com.crewmeister.cmcodingchallenge.rate;

import com.crewmeister.cmcodingchallenge.currency.CurrencyController;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RateModelAssembler implements RepresentationModelAssembler<Rate, EntityModel<Rate>> {

    @Override
    @NonNull
    public EntityModel<Rate> toModel(@NonNull Rate rate) {
        return EntityModel.of(rate,
                linkTo(methodOn(RateController.class).findOne(rate.getId())).withSelfRel(),
                linkTo(methodOn(RateController.class).findAll(null, null, Pageable.unpaged())).withRel("rates"),
                linkTo(methodOn(RateController.class).conversion(rate.getId(), null)).withRel("conversion"),
                linkTo(methodOn(CurrencyController.class).findOne(rate.getCurrency())).withRel("currency"));
    }
}
