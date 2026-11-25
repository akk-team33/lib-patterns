package de.team33.patterns.json.charon;

import de.team33.patterns.value.sinope.Equation;

import java.util.Objects;

abstract sealed class JsonItem<V> implements Json.Value
        permits JsonArray, JsonBoolean, JsonNumber, JsonObject, JsonString {

    @SuppressWarnings("rawtypes")
    private static final Equation<JsonItem> EQUATION = Equation.of(JsonItem.class, JsonItem::value);

    private final V value;

    JsonItem(final V value) {
        this.value = Objects.requireNonNull(value, "<value> must not be null - but was");
    }

    final V value() {
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
