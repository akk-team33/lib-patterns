package de.team33.patterns.properties.e2;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public class Getters<T> {

    private Map<String, Function<T, ?>> getters;

    private Getters(final Builder<T> builder) {
        getters = Collections.unmodifiableMap(new TreeMap<>(builder.getters));
    }

    public static <T> Builder<T> add(final String name, final Function<T, ?> getter) {
        return new Builder<T>().add(name, getter);
    }

    public final <M extends Map<String, Object>> TargetOperation<M> map(final T origin) {
        return target -> {
            getters.forEach((name, getter) -> target.put(name, getter.apply(origin)));
            return target;
        };
    }

    public final Map<String, Object> toMap(final T origin) {
        return map(origin).to(new TreeMap<>());
    }

    public static class Builder<T> {

        private Map<String, Function<T, ?>> getters;

        public final Builder<T> add(final String name, final Function<T, ?> getter) {
            getters.put(name, getter);
            return this;
        }

        public final Getters<T> build() {
            return new Getters<>(this);
        }
    }
}
