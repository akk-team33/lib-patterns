package de.team33.patterns.reflect.pandora;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
}
