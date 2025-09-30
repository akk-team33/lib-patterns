package de.team33.patterns.stream.galatea;

import java.util.stream.Stream;

@FunctionalInterface
public interface Streamable<E> {

    Stream<E> stream();
}
