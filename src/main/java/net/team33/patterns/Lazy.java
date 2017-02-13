package net.team33.patterns;

import java.util.function.Supplier;

public class Lazy<T> implements Supplier<T> {

    private volatile Supplier<T> backing;

    public Lazy(final Supplier<T> origin) {
        backing = new Initial(origin);
    }

    @Override
    public final T get() {
        return backing.get();
    }

    private static class Final<T> implements Supplier<T> {
        private final T value;

        private Final(final T value) {
            this.value = value;
        }

        @Override
        public final T get() {
            return value;
        }
    }

    private class Initial implements Supplier<T> {
        private final Monitor monitor = new Monitor();
        private final Supplier<T> origin;

        private Initial(final Supplier<T> origin) {
            this.origin = origin;
        }

        @Override
        public final T get() {
            return monitor.get(() -> {
                if (backing == this) {
                    backing = new Final<>(origin.get());
                }
                return backing.get();
            });
        }
    }
}
