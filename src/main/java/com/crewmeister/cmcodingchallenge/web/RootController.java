package com.crewmeister.cmcodingchallenge.web;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

import static com.crewmeister.cmcodingchallenge.web.BasePath.BASE_PATH;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = {BASE_PATH, BASE_PATH + "/"}, produces = "application/json")
public class RootController {

    @Operation(summary = "Entrypoint to currencies and rates")
    @GetMapping
    public CollectionModel<String> root() {
        return CollectionModel.of(Collections.emptyList(),
                linkTo(methodOn(RootController.class).root()).withSelfRel(),
                linkTo(methodOn(RateController.class).findAll(null, null, Pageable.unpaged())).withRel("rates"),
                linkTo(methodOn(CurrencyController.class).findAll(Pageable.unpaged())).withRel("currencies"));
    }
}
