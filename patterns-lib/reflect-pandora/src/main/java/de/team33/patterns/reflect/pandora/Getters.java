package de.team33.patterns.reflect.pandora;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;

public class Getters<T> {

    private final Map<String, Method> backing;

    private Getters(final Map<String, Method> backing) {
        this.backing = Collections.unmodifiableMap(backing);
    }

    public static <T> Getters<T> of(final Class<T> subjectClass) {
        final Map<String, Method> map = Methods.classicGettersOf(subjectClass)
                                               .collect(TreeMap::new, Getters::put, Map::putAll);
        return new Getters<>(map);
    }

    private static void put(final Map<String, Method> map, final Method method) {
        map.put(Methods.normalName(method), method);
    }

    public final Set<String> names() {
        return backing.keySet();
    }

    public final Function<T, Object> getter(final String name) {
        return Optional.ofNullable(backing.get(name))
                       .map(this::getter)
                       .orElseThrow(() -> new NoSuchElementException("no getter found for name <" + name + ">"));
    }

    private Function<T, Object> getter(final Method method) {
        return subject -> {
            try {
                return method.invoke(subject);
            } catch (final IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException("could not apply <subject> to method <" + method + ">", e);
            }
        };
    }
}
