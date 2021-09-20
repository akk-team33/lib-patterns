package de.team33.patterns.properties.e1;

import java.lang.reflect.Method;
import java.util.stream.Stream;

class Aggregator<T> {

    final void add(final Method method) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    final void addAll(final Aggregator<T> other) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public Stream<Property<T>> stream() {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
