package de.team33.patterns.json.charon;

import java.math.BigDecimal;
import java.util.Objects;

@SuppressWarnings("StaticInheritance")
record JsonNumber(BigDecimal value) implements JsonValue {

    JsonNumber {
        Objects.requireNonNull(value);
    }
}