package de.team33.patterns.serial.charon;

import java.util.stream.Stream;

/**
 * @deprecated It proved to be unnecessary.
 */
@Deprecated
public interface Streamable<E> {

    /**
     * Returns a sequential {@code Stream} with this {@link Streamable} as its source.
     */
    Stream<E> stream();
}
