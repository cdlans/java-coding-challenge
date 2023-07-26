package com.crewmeister.cmcodingchallenge.bundesbank;

import com.jayway.jsonpath.JsonPath;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BundesbankService {

    private final BundesbankClient bundesbankClient;

    BundesbankService(BundesbankClient bundesbankClient) {
        this.bundesbankClient = bundesbankClient;
    }

    public List<String> getCurrencyStrings() {
        String json = bundesbankClient.getCurrenciesJson();
        return JsonPath.read(json, "$.data.structure.dimensions.series[1].values[*].id");
    }
}
