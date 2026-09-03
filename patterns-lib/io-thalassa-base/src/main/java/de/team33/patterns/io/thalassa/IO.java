package de.team33.patterns.io.thalassa;

/**
 * Combines {@link Input} and {@link Output} for values of the same type.
 *
 * @param <T> the type of values read and written
 */
@SuppressWarnings("WeakerAccess")
public interface IO<T> extends Input<T>, Output<T> {
}
