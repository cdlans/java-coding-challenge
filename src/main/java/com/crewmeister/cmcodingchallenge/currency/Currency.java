package com.crewmeister.cmcodingchallenge.currency;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Currency {

    @JsonProperty
    String id;

    public Currency(String id) {
        this.id = id;
    }
}
