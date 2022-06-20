package de.team33.patterns.refreshing.e1;

import java.util.function.Supplier;

public interface Rule<T> {

    static <T> Rule<T> using(final Supplier<T> newInstance, final long lifeSpan) {
        return new Rule<T>() {
            @Override
            public long lifeSpan() {
                return lifeSpan;
            }

            @Override
            public T newInstance() {
                return newInstance.get();
            }
        };
    }

    long lifeSpan();

    T newInstance();

    default void oldInstance(final T instance) {
        // default does nothing
    }
}
