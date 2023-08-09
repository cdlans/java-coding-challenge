package com.crewmeister.cmcodingchallenge.web;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.WebApplicationContext;

import static com.crewmeister.cmcodingchallenge.web.BasePath.BASE_PATH;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static org.hamcrest.Matchers.hasKey;

@SpringBootTest
@Transactional
class RootControllerTest {

    @MockBean private CommandLineRunner commandLineRunner; // Avoid fetching data from Bundesbank

    @BeforeEach
    void setup(WebApplicationContext context) {
        RestAssuredMockMvc.webAppContextSetup(context);
        RestAssuredMockMvc.basePath = BASE_PATH;
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void rootShouldReturnLinks() {
        when()
            .get("/")
        .then()
            .status(HttpStatus.OK)
            .body("_links.self", hasKey("href"))
            .body("_links.rates", hasKey("href"))
            .body("_links.currencies", hasKey("href"));
    }
}
