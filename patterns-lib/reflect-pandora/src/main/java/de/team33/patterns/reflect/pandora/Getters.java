package de.team33.patterns.reflect.pandora;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;

public class Getters<T> {

    private final Map<String, Getter<T>> backing;

    private Getters(final Map<String, Getter<T>> backing) {
        this.backing = Collections.unmodifiableMap(backing);
    }

    public static <T> Getters<T> of(final Class<T> subjectClass) {
        return new Getters<>(Methods.classicGettersOf(subjectClass)
                                    .map(method -> new Getter<T>(method))
                                    .collect(TreeMap::new,
                                             (map, getter) -> map.put(getter.name(), getter),
                                             Map::putAll));
    }

    public final Set<String> names() {
        return backing.keySet();
    }

    public final Class<?> type(final String name) {
        return Optional.ofNullable(backing.get(name))
                       .map(Getter::type)
                       .orElseThrow(() -> new NoSuchElementException("no getter found for name <" + name + ">"));
    }

    public final Function<T, Object> getter(final String name) {
        return Optional.ofNullable(backing.get(name))
                       .orElseThrow(() -> new NoSuchElementException("no getter found for name <" + name + ">"));
    }

    public final Map<String, Object> toMap(final T subject) {
        return names().stream()
                      .collect(TreeMap::new,
                               (map, name) -> map.put(name, getter(name).apply(subject)),
                               Map::putAll);
    }
}
