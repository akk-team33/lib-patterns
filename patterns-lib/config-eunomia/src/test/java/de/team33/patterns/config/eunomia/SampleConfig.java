package de.team33.patterns.config.eunomia;

import java.util.List;

public record SampleConfig(String string, Entry entry, Item[] items) {

    private List<?> toList() {
        return List.of(string, entry, List.of(items));
    }

    @Override
    public boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof SampleConfig other) && toList().equals(other.toList()));
    }

    @Override
    public int hashCode() {
        return toList().hashCode();
    }

    @Override
    public String toString() {
        return toList().toString();
    }

    public record Entry(Long creation, long update) {
    }

    public record Item(int start, int limit) {
    }
}
