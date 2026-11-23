package de.team33.patterns.json.charon;

import java.math.BigDecimal;

class JsonNumber extends JsonEntity<BigDecimal> {

    JsonNumber(final BigDecimal value) {
        super(value);
    }

    @Override
    public final Json.Type type() {
        return Json.Type.NUMBER;
    }

    @Override
    public final BigDecimal asBigDecimal() {
        return value();
    }
}