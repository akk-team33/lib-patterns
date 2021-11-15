package de.team33.test.patterns.producing.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@SuppressWarnings("unused")
public class Mappable<T> {

    @SuppressWarnings("rawtypes")
    private static final Mapping<Mappable> MAPPING = Fields.mapping(Mappable.class);

    private T simple;
    private List<T> list;
    private Map<T, Set<T>> map;

    @SuppressWarnings("ThisEscapedInObjectConstruction")
    public Mappable(final Map<String, ?> origin) {
        MAPPING.remap(origin, this);
    }

    public Mappable() {
    }

    public final Map<String, Object> toMap() {
        return MAPPING.map(this, new TreeMap<>());
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
}
