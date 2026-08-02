package de.team33.patterns.records.triton;

import java.math.BigDecimal;
import java.util.Objects;

record JsonNumber(BigDecimal value) implements JsonValue {

    JsonNumber {
        Objects.requireNonNull(value);
    }
}