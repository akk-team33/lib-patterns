package de.team33.patterns.properties.e1;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class Properties<T> {

    private Properties(final Builder<T> builder) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public static <T> Builder<T> add(final String name, final Function<T, Object> getter) {
        return new Builder<T>().add(name, getter);
    }

    public static <T> Builder<T> add(final String name,
                                     final Function<T, Object> getter,
                                     final BiConsumer<T, Object> setter) {
        return new Builder<T>().add(name, getter, setter);
    }

    public boolean equals(final T subject, final T other) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public static class Builder<T> {

        public Builder<T> add(final String name, final Function<T, Object> getter) {
            throw new UnsupportedOperationException("not yet implemented");
        }

        public Builder<T> add(final String name, final Function<T, Object> getter, final BiConsumer<T, Object> setter) {
            throw new UnsupportedOperationException("not yet implemented");
        }

        public Properties<T> build() {
            return new Properties<>(this);
        }
    }
}
