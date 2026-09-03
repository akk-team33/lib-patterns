package de.team33.patterns.io.thalassa;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Represents a destination that accepts values of a specific type.
 * <p>
 * Implementations write supplied values, potentially performing I/O
 * operations.
 *
 * @param <T> the type of values accepted by this output
 */
@FunctionalInterface
public interface Output<T> {

    /**
     * Writes the given <em>value</em> to this output.
     *
     * @throws IOException if an I/O error occurs while writing
     */
    void write(T value) throws IOException;

    /**
     * Writes the given <em>value</em> to this output.
     * <p>
     * This method behaves like {@link #write(Object)}, but wraps any
     * {@link IOException} in an {@link UncheckedIOException}.
     *
     * @throws UncheckedIOException if an I/O error occurs while writing
     */
    default void writeUnchecked(final T value) {
        try {
            write(value);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
