package com.crewmeister.cmcodingchallenge.currency;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CurrencyControllerTest {

    private CurrencyRepository currencyRepository;

    @BeforeEach
    public void setup() {
        currencyRepository = mock(CurrencyRepository.class);

        RestAssuredMockMvc.basePath = "/api";
        RestAssuredMockMvc.standaloneSetup(new CurrencyController(currencyRepository));
    }

    @Test
    void shouldReturnCollectionOfCurrencies() {
        when(currencyRepository.findAll()).thenReturn(List.of(
            new Currency("EUR"),
            new Currency("USD"),
            new Currency("CHF")
        ));

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
