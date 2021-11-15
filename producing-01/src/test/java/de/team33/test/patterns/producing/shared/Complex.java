package de.team33.test.patterns.producing.shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class Complex<T> extends SuperComplex {

    private final T simple;
    private final List<T> list;
    private final Map<T, Set<T>> map;

    private Complex(final Builder<T> builder) {
        super(builder.collector);
        this.simple = builder.simple;
        this.list = builder.list;
        this.map = builder.map;
    }

    private static List<Object> toList(final Complex<?> c) {
        return Arrays.asList(c.getIntValue(), c.getStringValue(), c.simple, c.list, c.map);
    }

    public static <T> Complex<T> empty() {
        return new Builder<T>().build();
    }

    public final T getSimple() {
        return simple;
    }

    public final List<T> getList() {
        return list;
    }

    public final Map<T, Set<T>> getMap() {
        return map;
    }

    @Override
    public final int hashCode() {
        return toList(this).hashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof Complex) && toList(this).equals(toList((Complex<?>) obj)));
    }

    @Override
    public final String toString() {
        return toList(this).toString();
    }

    @SuppressWarnings("FieldHasSetterButNoGetter")
    public static class Builder<T> {

        private final Collector collector = new Collector();
        private T simple;
        private List<T> list;
        private Map<T, Set<T>> map;

        public final Complex<T> build() {
            return new Complex<>(this);
        }

        public final Builder<T> collect(final Consumer<Collector> consumer) {
            consumer.accept(collector);
            return this;
        }

        public final Builder<T> setSimple(final T simple) {
            this.simple = simple;
            return this;
        }

        public final Builder<T> setList(final List<T> list) {
            this.list = new ArrayList<>(list);
            return this;
        }

        public final Builder<T> setMap(final Map<T, Set<T>> map) {
            this.map = new HashMap<>(map);
            return this;
        }
    }
}
