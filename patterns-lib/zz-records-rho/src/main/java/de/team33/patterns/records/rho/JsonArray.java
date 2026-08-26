package de.team33.patterns.records.rho;

import de.team33.patterns.value.sinope.Equation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("StaticInheritance")
final class JsonArray implements JsonValue {

    private static final Equation<JsonArray> EQUATION = Equation.of(JsonArray.class, jsonArray -> jsonArray.values);

    private final List<JsonValue> values;

    private JsonArray(final List<JsonValue> values) {
        this.values = List.copyOf(values);
    }

    static Builder builder() {
        return new Builder();
    }

    final JsonValue get(final int index) {
        return values.get(index);
    }

    final int size() {
        return values.size();
    }

    final Stream<JsonValue> stream() {
        return values.stream();
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

    static final class Builder {

        private final List<JsonValue> values = new ArrayList<>();

        final Builder add(final JsonValue value) {
            values.add(requireNonNull(value));
            return this;
        }

        @SuppressWarnings({"MethodMayBeStatic", "UnusedReturnValue", "unused"})
        final Builder addAll(final Builder other) {
            throw new UnsupportedOperationException("should never be called");
        }

        final JsonArray build() {
            return new JsonArray(values);
        }
    }
}
