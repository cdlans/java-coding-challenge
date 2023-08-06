package com.crewmeister.cmcodingchallenge.rate;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface Conversion {
    String getCurrency();
    LocalDate getDate();
    BigDecimal getForeignAmount();
    BigDecimal getEuroAmount();
}
