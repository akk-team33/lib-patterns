package de.team33.patterns.json.charon;

import java.math.BigDecimal;
import java.util.Objects;

@SuppressWarnings("StaticInheritance")
public record JsonNumber(BigDecimal value) implements JsonValue {

    public JsonNumber {
        Objects.requireNonNull(value);
    }
}