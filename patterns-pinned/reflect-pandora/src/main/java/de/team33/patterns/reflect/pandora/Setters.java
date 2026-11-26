package de.team33.patterns.reflect.pandora;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableMap;

/**
 * Represents a view of the setters of a data object class, whose properties are mapped via getters and setters.
 *
 * @param <T> The data object class in question.
 */
public final class Setters<T> {

    private final Map<String, List<Setter<T>>> backing;

    private Setters(final Map<String, List<Setter<T>>> backing) {
        this.backing = unmodifiableMap(backing);
    }

    /**
     * Returns a view of the setters of a specific data object class.
     * <p>
     * Setters in this sense are all public instance methods that expect exactly one parameter, that
     * do not return a result ({@code void} or {@link Void}) or the instance in question itself (builder pattern),
     * that have not already been defined by {@link Object} and whose name is prefixed with "set".
     *
     * @param <T> The data object class in question.
     */
    public static <T> Setters<T> of(final Class<T> subjectClass) {
        return new Setters<>(Methods.classicSettersOf(subjectClass)
                                    .map(method -> new Setter<T>(method))
                                    .collect(TreeMap::new,
                                             Setters::put,
                                             Map::putAll));
    }

    private static <T> void put(final Map<? super String, List<Setter<T>>> map, final Setter<T> setter) {
        map.computeIfAbsent(setter.name(), name -> new LinkedList<>())
           .add(setter);
    }

    /**
     * Returns the names of the represented setters.
     */
    public final Set<String> names() {
        return backing.keySet();
    }

    /**
     * Returns the setter with the given name and property type as {@link BiConsumer}, which accepts an instance
     * of the underlying type and the value intended for the property as a parameter.
     *
     * @throws NoSuchElementException if no suitable setter can be found.
     */
    public final BiConsumer<T, Object> setter(final String name, final Class<?> type) {
        return Optional.ofNullable(backing.get(name))
                       .map(List::stream)
                       .orElseGet(Stream::empty)
                       .filter(setter -> setter.type().isAssignableFrom(type))
                       .reduce((left, right) -> left.type().isAssignableFrom(right.type()) ? right : left)
                       .orElseThrow(() -> new NoSuchElementException(
                               format("no setter found for name <%s> and type <%s>", name, type.getCanonicalName())));
    }
}
