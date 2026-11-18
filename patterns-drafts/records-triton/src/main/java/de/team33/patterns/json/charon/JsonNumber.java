package de.team33.patterns.json.charon;

import java.math.BigDecimal;

public class JsonNumber extends JsonUnit<BigDecimal> {

    public JsonNumber(final BigDecimal value) {
        super(value);
    }
}