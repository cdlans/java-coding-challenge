package com.crewmeister.cmcodingchallenge.bundesbank;

import com.crewmeister.cmcodingchallenge.currency.Currency;
import com.crewmeister.cmcodingchallenge.currency.CurrencyRepository;
import com.crewmeister.cmcodingchallenge.rate.Rate;
import com.crewmeister.cmcodingchallenge.rate.RateRepository;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class BundesbankService {
    private final Logger log = LoggerFactory.getLogger(BundesbankService.class);
    private final BundesbankClient bundesbankClient;
    private final CurrencyRepository currencyRepository;
    private final RateRepository rateRepository;

    BundesbankService(BundesbankClient bundesbankClient, CurrencyRepository currencyRepository,
                      RateRepository rateRepository) {
        this.bundesbankClient = bundesbankClient;
        this.currencyRepository = currencyRepository;
        this.rateRepository = rateRepository;

        Configuration.setDefaults(new Configuration.Defaults() {
            private final JsonProvider jsonProvider = new JacksonJsonProvider();
            private final MappingProvider mappingProvider = new JacksonMappingProvider();
            @Override public JsonProvider jsonProvider() {
                return jsonProvider;
            }
            @Override public MappingProvider mappingProvider() {
                return mappingProvider;
            }
            @Override public Set<Option> options() {
                return EnumSet.noneOf(Option.class);
            }
        });
    }

    public void initializeRates() {
        String jsonString = bundesbankClient.retrieveRatesJson();
        DocumentContext jsonContext = JsonPath.parse(jsonString);

        List<Currency> currencies = extractCurrencies(jsonContext);
        List<LocalDate> dates = extractDates(jsonContext);
        extractRates(jsonContext, currencies, dates);
    }

    private List<Currency> extractCurrencies(DocumentContext jsonContext) {
        TypeRef<List<Currency>> currencyListType = new TypeRef<>() { };
        List<Currency> currencies =
                jsonContext.read("$.data.structure.dimensions.series[1].values[*].id", currencyListType);

        currencyRepository.saveAll(currencies);

        log.info("Extracted and stored {} currencies", currencies.size());
        return currencies;
    }

    private List<LocalDate> extractDates(DocumentContext jsonContext) {
        TypeRef<List<String>> stringListType = new TypeRef<>() { };
        List<LocalDate> dates = jsonContext.read("$.data.structure.dimensions.observation[0].values[*].id", stringListType)
                .stream()
                .map(LocalDate::parse)
                .toList();

        log.info("Extracted {} dates", dates.size());
        return dates;
    }

    private void extractRates(DocumentContext jsonContext, List<Currency> currencies, List<LocalDate> dates) {
        log.info("Extracting and storing...");
        int rateCount = 0;

        for (int i = 0; i < currencies.size(); i++) {
            List<Rate> rates = new ArrayList<>();
            String currency = currencies.get(i).getId();
            Map<String, List<Object>> dateIndexToRate =
                    jsonContext.read("$.data.dataSets[0].series.0:" + i + ":0:0:0:0.observations");

            for (Map.Entry<String, List<Object>> entry : dateIndexToRate.entrySet()) {
                int dateIndex = Integer.parseInt(entry.getKey());
                LocalDate date = dates.get(dateIndex);
                Object rateObject = entry.getValue().get(0);

                if (rateObject instanceof String rateString) {
                    BigDecimal rateBigDecimal = new BigDecimal(rateString);
                    rates.add(new Rate(currency, date, rateBigDecimal));
                    rateCount++;
                }
            }
            rateRepository.saveAll(rates);
            log.info("Extracted and stored {} rates", rateCount);
        }

        log.info("Finished extracting and storing rates");
    }
}
