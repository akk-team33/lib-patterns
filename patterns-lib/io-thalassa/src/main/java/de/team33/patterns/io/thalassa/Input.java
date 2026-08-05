package de.team33.patterns.io.thalassa;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Represents a source that provides values of a specific type.
 * <p>
 * Implementations read and return the next available value, potentially
 * performing I/O operations.
 *
 * @param <T> the type of values produced by this input
 */
@FunctionalInterface
public interface Input<T> {

    /**
     * Reads and returns the next value from this input.
     *
     * @throws IOException if an I/O error occurs while reading
     */
    T read() throws IOException;

    /**
     * Reads and returns the next value from this input.
     * <p>
     * This method behaves like {@link #read()}, but wraps any
     * {@link IOException} in an {@link UncheckedIOException}.
     *
     * @throws UncheckedIOException if an I/O error occurs while reading
     */
    default T readUnchecked() {
        try {
            return read();
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
