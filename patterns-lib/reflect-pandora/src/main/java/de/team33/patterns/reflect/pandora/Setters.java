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

public class Setters<T> {

    private final Map<String, List<Setter<T>>> backing;

    private Setters(final Map<String, List<Setter<T>>> backing) {
        this.backing = unmodifiableMap(backing);
    }

    public static <T> Setters<T> of(final Class<T> subjectClass) {
        return new Setters<>(Methods.classicSettersOf(subjectClass)
                                    .map(method -> new Setter<T>(method))
                                    .collect(TreeMap::new,
                                             Setters::put,
                                             Map::putAll));
    }

    private static <T> void put(final Map<String, List<Setter<T>>> map, final Setter<T> setter) {
        map.computeIfAbsent(setter.name(), name -> new LinkedList<>())
           .add(setter);
    }

    public final Set<String> names() {
        return backing.keySet();
    }

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
