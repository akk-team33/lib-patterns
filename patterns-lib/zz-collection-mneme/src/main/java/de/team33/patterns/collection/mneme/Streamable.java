package de.team33.patterns.collection.mneme;

import java.util.stream.Stream;

@FunctionalInterface
public interface Streamable<E> {

    Stream<E> stream();
}
