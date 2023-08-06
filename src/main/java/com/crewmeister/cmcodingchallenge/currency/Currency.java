package com.crewmeister.cmcodingchallenge.currency;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Currency {

    @Id
    String id;
    String code;

    protected Currency() {
        // JPA needs no-arg constructor
    }

    public Currency(String id) {
        this.id = id;
        this.code = id;
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return String.format("Currency[id='%s', code='%s']", id, code);
    }
}
