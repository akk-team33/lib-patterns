package de.team33.patterns.json.charon;

import de.team33.patterns.value.sinope.Equation;

import java.util.Objects;

class JsonUnit<V> implements JsonItem {

    @SuppressWarnings("rawtypes")
    private static final Equation<JsonUnit> EQUATION = Equation.of(JsonUnit.class, JsonUnit::value);

    private final V value;

    JsonUnit(final V value) {
        this.value = Objects.requireNonNull(value, "<value> must not be null - but was");
    }

    public final V value() {
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
