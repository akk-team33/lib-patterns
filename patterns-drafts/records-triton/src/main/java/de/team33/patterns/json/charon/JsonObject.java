package de.team33.patterns.json.charon;

import java.util.*;

@SuppressWarnings("StaticInheritance")
public final class JsonObject extends AbstractList<JsonObject.Entry> implements JsonItem {

    private final List<Entry> entries;

    private JsonObject(final List<Entry> entries) {
        this.entries = List.copyOf(entries);
    }

    public static Builder builder() {
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

    public record Entry(String name, JsonItem value) {
    }

    public static final class Builder {

        private final Map<String, Integer> indices = new HashMap<>();
        private final List<Entry> entries = new ArrayList<>();

        public final Builder put(final String name, final JsonItem value) {
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
        public final Builder putAll(final Builder other) {
            throw new UnsupportedOperationException("should never be called");
        }

        public final JsonObject build() {
            return new JsonObject(entries);
        }
    }
}
