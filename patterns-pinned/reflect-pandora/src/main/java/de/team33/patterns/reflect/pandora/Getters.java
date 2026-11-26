package de.team33.patterns.reflect.pandora;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * Represents a view of the getters of a data object class, whose properties are mapped via getters and setters.
 *
 * @param <T> The data object class in question.
 */
public final class Getters<T> {

    private final Map<String, Getter<T>> backing;

    private Getters(final Map<String, Getter<T>> backing) {
        this.backing = Collections.unmodifiableMap(backing);
    }

    /**
     * Returns a view of the getters of a given data object class.
     * <p>
     * Getters in this sense are all public, parameterless instance methods that actually return a result
     * (not {@code void} or {@link Void}) that have not already been defined by {@link Object} and whose name begins
     * with one of the prefixes "get" or "is".
     *
     * @param <T> The data object class in question.
     */
    public static <T> Getters<T> of(final Class<T> subjectClass) {
        return new Getters<>(Methods.classicGettersOf(subjectClass)
                                    .map(method -> new Getter<T>(method))
                                    .collect(TreeMap::new,
                                             (map, getter) -> map.put(getter.name(), getter),
                                             Map::putAll));
    }

    /**
     * Returns the names of the represented getters.
     */
    public final Set<String> names() {
        return backing.keySet();
    }

    /**
     * Returns the result type of the getter with the given name.
     */
    public final Class<?> type(final String name) {
        return Optional.ofNullable(backing.get(name))
                       .map(Getter::type)
                       .orElseThrow(() -> new NoSuchElementException("no getter found for name <" + name + ">"));
    }

    /**
     * Returns the getter with the given name as a {@link Function} that takes an instance of the underlying type as
     * a parameter and returns the value of the represented property.
     *
     * @throws NoSuchElementException if no suitable getter can be found.
     */
    public final Function<T, Object> getter(final String name) {
        return Optional.ofNullable(backing.get(name))
                       .orElseThrow(() -> new NoSuchElementException("no getter found for name <" + name + ">"));
    }

    /**
     * Results in a {@link Map} that maps the names of the represented properties to their values.
     */
    public final Map<String, Object> toMap(final T subject) {
        return names().stream()
                      .collect(TreeMap::new,
                               (map, name) -> map.put(name, getter(name).apply(subject)),
                               Map::putAll);
    }
}
