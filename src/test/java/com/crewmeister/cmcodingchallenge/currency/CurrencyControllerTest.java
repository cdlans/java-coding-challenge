package com.crewmeister.cmcodingchallenge.currency;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.is;

class CurrencyControllerTest {

    @BeforeAll
    public static void setup() {
        RestAssuredMockMvc.basePath = "/api";
        RestAssuredMockMvc.standaloneSetup(new CurrencyController());
    }

    @Test
    void shouldReturnCollectionOfCurrencies() {
        given()
            .when()
                .get("/currencies")
            .then()
                .status(HttpStatus.OK)
                .body("$.size()", is(3))
                .body("[0].id", is("EUR"))
                .body("[1].id", is("USD"))
                .body("[2].id", is("CHF"));
    }
}
