package de.team33.patterns.json.charon;

import java.util.Objects;

@SuppressWarnings("StaticInheritance")
public record JsonString(String value) implements JsonItem {

    public JsonString {
        Objects.requireNonNull(value);
    }
}
