package de.team33.patterns.records.rho;

import java.util.*;

@SuppressWarnings("StaticInheritance")
final class JsonObject extends AbstractList<JsonObject.Entry> implements JsonValue {

    private final List<Entry> entries;

    private JsonObject(final List<Entry> entries) {
        this.entries = List.copyOf(entries);
    }

    static Builder builder() {
        return new Builder();
    }

    @Override
    public final Entry get(final int index) {
        return entries.get(index);
    }

    @Override
    public int size() {
        return entries.size();
    }

    record Entry(String name, JsonValue value) {
    }

    static final class Builder {

        private final Map<String, Integer> indices = new HashMap<>();
        private final List<Entry> entries = new ArrayList<>();

        final Builder put(final String name, final JsonValue value) {
            final Entry entry = new Entry(name, value);
            final int index = indices.computeIfAbsent(name, any -> entries.size());
            if (index < entries.size()) {
                entries.set(index, entry);
            } else {
                entries.add(entry);
            }
            return this;
        }

        @SuppressWarnings({"MethodMayBeStatic", "UnusedReturnValue", "unused"})
        final Builder putAll(final Builder other) {
            throw new UnsupportedOperationException("should never be called");
        }

        final JsonObject build() {
            return new JsonObject(entries);
        }
    }
}
