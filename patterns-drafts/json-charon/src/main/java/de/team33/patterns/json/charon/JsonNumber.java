package de.team33.patterns.json.charon;

import java.math.BigDecimal;

public final class JsonNumber extends JsonBase<BigDecimal> {

    public JsonNumber(final BigDecimal value) {
        super(value);
    }
}
