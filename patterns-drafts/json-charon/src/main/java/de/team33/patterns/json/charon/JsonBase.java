package de.team33.patterns.json.charon;

import de.team33.patterns.value.sinope.Equation;

import java.util.Objects;

sealed class JsonBase<T> implements JsonValue permits JsonArray, JsonBoolean, JsonNumber, JsonObject, JsonString {

    @SuppressWarnings("rawtypes")
    private static final Equation<JsonBase> EQUATION = Equation.of(JsonBase.class, JsonBase::value);

    private final T value;

    JsonBase(final T value) {
        this.value = Objects.requireNonNull(value, "value must not be null");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final T value() {
        return value;
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
        return EQUATION.toString(this);
    }
}
