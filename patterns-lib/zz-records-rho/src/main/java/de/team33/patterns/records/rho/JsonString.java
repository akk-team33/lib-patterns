package de.team33.patterns.records.rho;

import java.util.Objects;

@SuppressWarnings("StaticInheritance")
record JsonString(String value) implements JsonValue {

    JsonString {
        Objects.requireNonNull(value);
    }
}
