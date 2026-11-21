package de.team33.patterns.json.charon;

import de.team33.patterns.value.sinope.Equation;

import java.util.Objects;

class JsonEntity<V> implements Json.Value {

    @SuppressWarnings("rawtypes")
    private static final Equation<JsonEntity> EQUATION = Equation.of(JsonEntity.class, JsonEntity::value);

    private final V value;

    JsonEntity(final V value) {
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
