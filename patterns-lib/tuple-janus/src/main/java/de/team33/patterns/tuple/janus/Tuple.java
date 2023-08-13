package de.team33.patterns.tuple.janus;

import java.util.List;

abstract class Tuple {

    @Override
    public final boolean equals(final Object obj) {
        return this == obj || (obj instanceof Tuple && toList().equals(((Tuple) obj).toList()));
    }

    @Override
    public final int hashCode() {
        return toList().hashCode();
    }

    @Override
    public final String toString() {
        return toList().toString();
    }

    public abstract List<Object> toList();
}
