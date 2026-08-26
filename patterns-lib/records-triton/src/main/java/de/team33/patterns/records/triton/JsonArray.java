package de.team33.patterns.records.triton;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("StaticInheritance")
final class JsonArray implements JsonValue {

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

    @Override
    public final boolean equals(final Object obj) {
        return this == obj || (obj instanceof final JsonArray other && values.equals(other.values));
    }

    @Override
    public final int hashCode() {
        return values.hashCode();
    }

    @Override
    public final String toString() {
        return values.toString();
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
