package de.team33.patterns.reflect.pandora;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class Getters<T> {

    private final Map<String, Method> backing;

    private Getters(final Map<String, Method> map) {
        this.backing = Collections.unmodifiableMap(map);
    }

    public static <T> Getters<T> of(final Class<T> subjectClass) {
        final Map<String, Method> map = Methods.publicGetters(subjectClass)
                                               .collect(TreeMap::new, Getters::put, Map::putAll);
        return new Getters<>(map);
    }

    private static void put(final Map<String, Method> map, final Method method) {
        map.put(Methods.normalName(method), method);
    }

    public final Map<String, Object> map(final T subject) {
        return backing.entrySet()
                      .stream()
                      .collect(TreeMap::new, (map, entry) -> put(map, entry, subject), Map::putAll);
    }

    private void put(final Map<String, Object> map, final Map.Entry<String, Method> entry, final T subject) {
        try {
            map.put(entry.getKey(), entry.getValue().invoke(subject));
        } catch (final IllegalAccessException | InvocationTargetException | RuntimeException e) {
            throw new IllegalStateException("could not get <" + entry.getKey() + "> via " + entry.getValue(), e);
        }
    }
}
