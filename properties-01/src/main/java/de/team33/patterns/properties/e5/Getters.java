package de.team33.patterns.properties.e5;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public class Getters<T> {

    private final Map<String, Function<T, Object>> backing = new TreeMap<>();

    public static <T> Getters<T> set(final String name, final Function<T, Object> getter) {
        return new Getters<T>().add(name, getter);
    }

    private Getters<T> add(final String name, final Function<T, Object> getter) {
        backing.put(name, getter);
        return this;
    }

    public final Mapping<T> mapper() {
        return () -> Collections.unmodifiableMap(new TreeMap<>(backing));
    }
}
