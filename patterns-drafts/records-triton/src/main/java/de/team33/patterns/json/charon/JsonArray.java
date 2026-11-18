package de.team33.patterns.json.charon;

import de.team33.patterns.value.sinope.Equation;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("StaticInheritance")
public final class JsonArray implements JsonItem {

    private static final Equation<JsonArray> EQUATION = Equation.of(JsonArray.class, jsonArray -> jsonArray.values);

    private final List<JsonItem> values;

    private JsonArray(final List<JsonItem> values) {
        this.values = List.copyOf(values);
    }

    public static Builder builder() {
        return new Builder();
    }

    public final JsonItem get(final int index) {
        return values.get(index);
    }

    public final int size() {
        return values.size();
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

    public static final class Builder {

        private final List<JsonItem> values = new ArrayList<>();

        public final Builder add(final JsonItem value) {
            values.add(requireNonNull(value));
            return this;
        }

        @SuppressWarnings({"MethodMayBeStatic", "UnusedReturnValue", "unused"})
        public final Builder addAll(final Builder other) {
            throw new UnsupportedOperationException("should never be called");
        }

        public final JsonArray build() {
            return new JsonArray(values);
        }
    }
}
