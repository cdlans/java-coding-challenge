package com.crewmeister.cmcodingchallenge.currency;

import com.crewmeister.cmcodingchallenge.rate.Rate;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Currency {

    @Id
    private String id;
    private String code;

    @OneToMany(mappedBy = "currency")
    private final Set<Rate> rates = new HashSet<>();

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
