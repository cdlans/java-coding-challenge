package com.crewmeister.cmcodingchallenge.currency;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Currency {

    @Id
    String id;

    protected Currency() {
        // JPA needs no-arg constructor
    }

    public Currency(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("Currency[id='%s']", id);
    }
}
