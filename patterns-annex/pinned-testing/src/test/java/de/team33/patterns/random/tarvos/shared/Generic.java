package de.team33.patterns.random.tarvos.shared;

import java.util.Objects;
import java.util.Optional;

@SuppressWarnings({"NonFinalFieldReferencedInHashCode", "NonFinalFieldReferenceInEquals", "unused"})
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
    public final int hashCode() {
        return tValue.hashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || Optional.ofNullable(obj)
                                        .filter(Generic.class::isInstance)
                                        .map(Generic.class::cast)
                                        .map(other -> Objects.equals(tValue, other.tValue))
                                        .orElse(false);
    }

    @Override
    public final String toString() {
        return tValue.toString();
    }
}
