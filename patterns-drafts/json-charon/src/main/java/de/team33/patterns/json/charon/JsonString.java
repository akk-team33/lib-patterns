package de.team33.patterns.json.charon;

import de.team33.patterns.value.sinope.Equation;

import java.util.Objects;

@SuppressWarnings("StaticInheritance")
public final class JsonString implements JsonValue {

    private static final Equation<JsonString> EQUATION = Equation.of(JsonString.class, js -> js.value);

    private final String value;

    private JsonString(final String value) {
        this.value = value;
    }

    public static JsonValue of(final String value) {
        return new JsonString(Objects.requireNonNull(value));
    }

    @Override
    public final boolean equals(final Object obj) {
        return EQUATION.equals(this, obj);
    }

    @Override
    public final int hashCode() {
        return EQUATION.hashCode(this);
    }

    @Override
    public final String toString() {
        return value;
    }
}
