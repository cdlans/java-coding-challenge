package com.crewmeister.cmcodingchallenge.rate;

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

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

@SpringBootTest
@Transactional
class RateTest {

    @MockBean
    CommandLineRunner commandLineRunner; // Avoid fetching data from Bundesbank

    @Autowired
    RateRepository rateRepository;

    @BeforeEach
    void setup(WebApplicationContext context) {
        RestAssuredMockMvc.webAppContextSetup(context);
        RestAssuredMockMvc.basePath = "/api/v1";
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void getRatesShouldReturnCollectionOfRates() {
        rateRepository.save(new Rate("EUR", "2023-01-01", new BigDecimal("1.234")));
        rateRepository.save(new Rate("USD", "2023-01-01", new BigDecimal("9.876")));
        rateRepository.save(new Rate("CHF", "2023-01-01", new BigDecimal("42.42")));

        when()
            .get("/rates")
        .then()
            .status(HttpStatus.OK)
            .body("_embedded.rateList.size()", is(3))
            .body("_embedded.rateList", hasItem(hasEntry("exchangeRate", 1.234f)))
            .body("_embedded.rateList", hasItem(hasEntry("exchangeRate", 9.876f)))
            .body("_embedded.rateList", hasItem(hasEntry("exchangeRate", 42.42f)));
    }

    @Test
    void getRateByIdShouldReturnRate() {
        Rate rate = rateRepository.save(new Rate("USD", "2023-01-01", new BigDecimal("9.876")));

        when()
            .get("/rates/{id}", rate.getId())
        .then()
            .status(HttpStatus.OK)
            .body("date", is("2023-01-01"))
            .body("exchangeRate", is(9.876f));
    }

    @Test
    void getRatesByCurrencyShouldReturnRate() {
        rateRepository.save(new Rate("CHF", "2023-01-01", new BigDecimal("1.234")));
        rateRepository.save(new Rate("USD", "2023-01-01", new BigDecimal("9.876")));

        when()
            .get("/rates?currency=USD")
        .then()
            .status(HttpStatus.OK)
            .body("_embedded.rateList.size()", is(1))
            .body("_embedded.rateList[0].date", is("2023-01-01"))
            .body("_embedded.rateList[0].exchangeRate", is(9.876f));
    }

    @Test
    void getRatesByDateShouldReturnRate() {
        rateRepository.save(new Rate("USD", "2023-01-01", new BigDecimal("9.876")));
        rateRepository.save(new Rate("USD", "2023-01-02", new BigDecimal("8.765")));

        when()
            .get("/rates?date=2023-01-02")
        .then()
            .status(HttpStatus.OK)
            .body("_embedded.rateList.size()", is(1))
            .body("_embedded.rateList[0].date", is("2023-01-02"))
            .body("_embedded.rateList[0].exchangeRate", is(8.765f));
    }

    @Test
    void getRateByCurrencyAndDateShouldReturnRate() {
        rateRepository.save(new Rate("CHF", "2023-01-01", new BigDecimal("9.876")));
        rateRepository.save(new Rate("AUD", "2023-01-02", new BigDecimal("8.765")));
        rateRepository.save(new Rate("USD", "2023-01-03", new BigDecimal("7.654")));

        when()
            .get("/rates?currency=USD&date=2023-01-03")
        .then()
            .status(HttpStatus.OK)
            .body("_embedded.rateList.size()", is(1))
            .body("_embedded.rateList[0].date", is("2023-01-03"))
            .body("_embedded.rateList[0].exchangeRate", is(7.654f));
    }

    @Test
    void getConversionShouldReturnCorrectConversion() {
        Rate rate = rateRepository.save(new Rate("USD", "2023-08-06", new BigDecimal("1.10")));

        when()
            .get("/rates/{id}/conversion?foreignAmount=110", rate.getId())
        .then()
            .status(HttpStatus.OK)
            .body("euroAmount", is(100f));
    }

    @Test
    void getConversionShouldReturnDefaultAmount() {
        Rate rate = rateRepository.save(new Rate("USD", "2023-08-06", new BigDecimal("1.25")));

        when()
            .get("/rates/{id}/conversion", rate.getId())
        .then()
            .status(HttpStatus.OK)
            .body("euroAmount", is(0.8f));
    }

    @Test
    void getConversionShouldNotAllowDenialOfServiceAttack() {
        Rate rate = rateRepository.save(new Rate("USD", "2023-08-06", new BigDecimal("1.25")));

        when()
            .get("/rates/{id}/conversion?foreignAmount=5e912345", rate.getId())
        .then()
            .status(HttpStatus.BAD_REQUEST)
            .body("detail", startsWith("conversion.foreignAmount: must be less than or equal to"));
    }

    @Test
    void shouldReturnInternalServerErrorForArithmeticException() {
        Rate rate = rateRepository.save(new Rate("USD", "2023-08-06", new BigDecimal("0")));

        when()
            .get("/rates/{id}/conversion?foreignAmount=200", rate.getId())
        .then()
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("detail", startsWith("Could not convert currency USD"));
    }

    @Test
    void postRatesShouldNotBeAllowed() {
        String json = """
                    { "currency": "EUR", "date": "2023-08-03", "exchangeRate": 1.234 }
                """;

        given()
            .body(json)
        .when()
            .post("/rates")
        .then()
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .body("detail", is("Method 'POST' is not supported."));
    }
}
