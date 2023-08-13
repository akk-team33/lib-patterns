package de.team33.test.patterns.production.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@SuppressWarnings("unused")
public class Mappable<T> {

    private T simple;
    private List<T> list;
    private Map<T, Set<T>> map;

    public Mappable() {
    }

    public Mappable(final Map<?, ?> origin) {
        this.simple = cast(origin.get("simple"));
        this.list = new ArrayList<>(cast(origin.get("list")));
        this.map = new HashMap<>(cast(origin.get("map")));
    }

    @SuppressWarnings("unchecked")
    private static <R> R cast(final Object obj) {
        return (R) obj;
    }

    public final T getSimple() {
        return simple;
    }

    public final Mappable<T> setSimple(final T simple) {
        this.simple = simple;
        return this;
    }

    public final List<T> getList() {
        return Collections.unmodifiableList(list);
    }

    public final Mappable<T> setList(final List<T> list) {
        this.list = new ArrayList<>(list);
        return this;
    }

    public final Map<T, Set<T>> getMap() {
        return Collections.unmodifiableMap(map);
    }

    public final Mappable<T> setMap(final Map<T, Set<T>> map) {
        this.map = new HashMap<>(map);
        return this;
    }

    @Override
    public final int hashCode() {
        return toMap().hashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof Mappable) && toMap().equals(((Mappable<?>) obj).toMap()));
    }

    @Override
    public final String toString() {
        return toMap().toString();
    }

    public final Map<String, Object> toMap() {
        final Map<String, Object> result = new TreeMap<>();
        result.put("simple", simple);
        result.put("list", list);
        result.put("map", map);
        return result;
    }
}
