package de.team33.patterns.tuple.janus;

import java.util.List;

abstract class Listable {

    @Override
    public final boolean equals(final Object obj) {
        return this == obj || (obj instanceof Listable && toList().equals(((Listable) obj).toList()));
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
