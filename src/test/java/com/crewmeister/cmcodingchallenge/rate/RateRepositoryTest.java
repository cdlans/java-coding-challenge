package com.crewmeister.cmcodingchallenge.rate;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@SpringBootTest
@Transactional
class RateRepositoryTest {

    @MockBean
    CommandLineRunner commandLineRunner; // Avoid fetching data from Bundesbank

    @Autowired
    RateRepository rateRepository;

    @Value("${spring.data.rest.basePath}")
    String basePath;

    @BeforeEach
    void setup(WebApplicationContext context) {
        RestAssuredMockMvc.webAppContextSetup(context);
        RestAssuredMockMvc.basePath = basePath;
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
            .body("_embedded.rates.size()", is(3))
            .body("_embedded.rates", hasItem(hasEntry("exchangeRate", 1.234f)))
            .body("_embedded.rates", hasItem(hasEntry("exchangeRate", 9.876f)))
            .body("_embedded.rates", hasItem(hasEntry("exchangeRate", 42.42f)));
    }

    @Test
    void getRateByIdShouldReturnRate() {
        Rate rate = rateRepository.save(new Rate("USD", "2023-01-01", new BigDecimal("9.876")));

        when()
            .get("/rates/" + rate.getId())
        .then()
            .status(HttpStatus.OK)
            .body("date", is("2023-01-01"))
            .body("exchangeRate", is(9.876f));
    }

    @Test
    void getRatesByCurrencyShouldReturnRate() {
        rateRepository.save(new Rate("USD", "2023-01-01", new BigDecimal("9.876")));

        when()
            .get("/rates/search/findByCurrency?currency=USD")
        .then()
            .status(HttpStatus.OK)
            .body("_embedded.rates[0].date", is("2023-01-01"))
            .body("_embedded.rates[0].exchangeRate", is(9.876f));
    }

    @Test
    void getRatesByDateShouldReturnRate() {
        rateRepository.save(new Rate("USD", "2023-01-01", new BigDecimal("9.876")));

        when()
            .get("/rates/search/findByDate?date=2023-01-01")
        .then()
            .status(HttpStatus.OK)
            .body("_embedded.rates[0].date", is("2023-01-01"))
            .body("_embedded.rates[0].exchangeRate", is(9.876f));
    }

    @Test
    void getRateByCurrencyAndDateShouldReturnRate() {
        rateRepository.save(new Rate("USD", "2023-01-01", new BigDecimal("9.876")));

        when()
            .get("/rates/search/findByCurrencyAndDate?currency=USD&date=2023-01-01")
        .then()
            .status(HttpStatus.OK)
            .body("date", is("2023-01-01"))
            .body("exchangeRate", is(9.876f));
    }

    @Test
    void getConversionShouldReturnCorrectConversion() {
        rateRepository.save(new Rate("USD", "2023-08-06", new BigDecimal("1.10")));

        when()
            .get("/rates/search/conversion?currency=USD&date=2023-08-06&foreignAmount=110")
        .then()
            .status(HttpStatus.OK)
            .body("euroAmount", is(100f));
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
            .status(HttpStatus.METHOD_NOT_ALLOWED);
    }
}
