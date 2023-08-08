package com.crewmeister.cmcodingchallenge.currency;

import com.crewmeister.cmcodingchallenge.rate.Rate;
import com.crewmeister.cmcodingchallenge.rate.RateRepository;
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

import java.math.BigDecimal;
import java.time.LocalDate;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@Transactional
class CurrencyTest {

    @Autowired private RateRepository rateRepository;
    @MockBean private CommandLineRunner commandLineRunner; // Avoid fetching data from Bundesbank

    @BeforeEach
    void setup(WebApplicationContext context) {
        RestAssuredMockMvc.webAppContextSetup(context);
        RestAssuredMockMvc.basePath = "/api/v1";
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void getCurrenciesShouldReturnCollectionOfCurrencies() {
        rateRepository.save(new Rate("EUR", LocalDate.of(2023, 1, 1), new BigDecimal(1)));
        rateRepository.save(new Rate("USD", LocalDate.of(2023, 1, 1), new BigDecimal(1)));
        rateRepository.save(new Rate("CHF", LocalDate.of(2023, 1, 1), new BigDecimal(1)));

        when()
            .get("/currencies")
        .then()
            .status(HttpStatus.OK)
            .body("_embedded.currencyList.size()", is(3))
            .body("_embedded.currencyList", hasItem(hasEntry("id", "EUR")))
            .body("_embedded.currencyList", hasItem(hasEntry("id", "USD")))
            .body("_embedded.currencyList", hasItem(hasEntry("id", "CHF")));
    }

    @Test
    void getCurrenciesShouldReturnPagedResult() {
        rateRepository.save(new Rate("EUR", LocalDate.of(2023, 1, 1), new BigDecimal(1)));
        rateRepository.save(new Rate("USD", LocalDate.of(2023, 1, 1), new BigDecimal(1)));
        rateRepository.save(new Rate("CHF", LocalDate.of(2023, 1, 1), new BigDecimal(1)));

        when()
            .get("/currencies?page=1&size=2")
        .then()
            .status(HttpStatus.OK)
            .body("_embedded.currencyList.size()", is(1))
            .body("_links.first", hasKey("href"))
            .body("_links.prev", hasKey("href"))
            .body("_links.self", hasKey("href"))
            .body("_links.last", hasKey("href"));
    }

    @Test
    void getCurrencyByIdShouldReturnCurrency() {
        rateRepository.save(new Rate("USD", LocalDate.of(2023, 1, 1), new BigDecimal(1)));

        when()
            .get("/currencies/USD")
        .then()
            .status(HttpStatus.OK)
            .body("id", is("USD"));
    }

    @Test
    void getCurrenciesShouldReturn404WhenNotFound() {
        when()
            .get("/currencies/XYZ")
        .then()
            .status(HttpStatus.NOT_FOUND)
            .body("detail", is("Could not find currency 'XYZ'"));
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
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .body("detail", is("Method 'POST' is not supported."));
    }
}
