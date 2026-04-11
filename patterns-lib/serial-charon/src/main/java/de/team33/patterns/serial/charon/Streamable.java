package de.team33.patterns.serial.charon;

import java.util.stream.Stream;

/**
 * Represents something that may produce a {@link Stream} of elements of a specific type.
 *
 * @param <E> The element type.
 */
public interface Streamable<E> {

    /**
     * Returns a sequential {@code Stream} with this {@link Streamable} as its source.
     */
    Stream<E> stream();
}
