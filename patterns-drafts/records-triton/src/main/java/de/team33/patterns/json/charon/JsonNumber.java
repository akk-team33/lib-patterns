package de.team33.patterns.json.charon;

import java.math.BigDecimal;

final class JsonNumber extends JsonItem<BigDecimal> {

    JsonNumber(final BigDecimal value) {
        super(value);
    }

    @Override
    public Json.Type type() {
        return Json.Type.NUMBER;
    }

    @Override
    public BigDecimal asBigDecimal() {
        return value();
    }
}