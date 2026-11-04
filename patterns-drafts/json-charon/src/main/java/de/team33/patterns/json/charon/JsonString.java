package de.team33.patterns.json.charon;

import java.util.Objects;

@SuppressWarnings("StaticInheritance")
record JsonString(String value) implements JsonValue {

    JsonString {
        Objects.requireNonNull(value);
    }
}
