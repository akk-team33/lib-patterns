package de.team33.patterns.json.charon;

import java.math.BigDecimal;

public class JsonNumber extends JsonEntity<BigDecimal> {

    public JsonNumber(final BigDecimal value) {
        super(value);
    }

    @Override
    public final Json.Type type() {
        return Json.Type.NUMBER;
    }
}