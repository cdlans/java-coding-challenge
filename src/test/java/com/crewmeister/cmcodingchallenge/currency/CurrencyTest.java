package com.crewmeister.cmcodingchallenge.currency;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@Transactional
class CurrencyTest {

    @MockBean
    CommandLineRunner commandLineRunner; // Avoid fetching data from Bundesbank

    @Autowired
    CurrencyRepository currencyRepository;

    @BeforeEach
    void setup(WebApplicationContext context) {
        RestAssuredMockMvc.webAppContextSetup(context);
        RestAssuredMockMvc.basePath = "/api/v2";
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void getCurrenciesShouldReturnCollectionOfCurrencies() {
        currencyRepository.save(new Currency("EUR"));
        currencyRepository.save(new Currency("USD"));
        currencyRepository.save(new Currency("CHF"));

        when()
            .get("/currencies")
        .then()
            .status(HttpStatus.OK)
            .body("content.size()", is(3))
            .body("content", hasItem(hasEntry("code", "EUR")))
            .body("content", hasItem(hasEntry("code", "USD")))
            .body("content", hasItem(hasEntry("code", "CHF")));
    }

    @Test
    void getCurrencyByIdShouldReturnCurrency() {
        currencyRepository.save(new Currency("USD"));

        when()
            .get("/currencies/USD")
        .then()
            .status(HttpStatus.OK)
            .body("code", is("USD"));
    }

    @Test
    void getCurrenciesShouldReturn404WhenNotFound() {
        when()
            .get("/currencies/XYZ")
        .then()
            .status(HttpStatus.NOT_FOUND);
    }

    @Test
    void postCurrenciesShouldNotBeAllowed() {
        String json = """
                    { "id": "NEW" }
                """;

        given()
            .body(json)
        .when()
            .post("/currencies")
        .then()
            .status(HttpStatus.METHOD_NOT_ALLOWED);
    }
}
