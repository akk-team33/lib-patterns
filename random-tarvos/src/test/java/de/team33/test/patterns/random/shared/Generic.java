package de.team33.test.patterns.random.shared;

import java.util.Optional;

public class Generic<T> {

    private T tValue;

    public final T getTValue() {
        return tValue;
    }

    public final Generic<T> setTValue(final T tValue) {
        this.tValue = tValue;
        return this;
    }

    @Override
    public int hashCode() {
        return tValue.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (this == obj) || Optional.ofNullable(obj)
                                        .map(Generic.class::cast)
                                        .map(Generic.class::isInstance)
                                        .orElse(false);
    }

    @Override
    public String toString() {
        return tValue.toString();
    }
}
