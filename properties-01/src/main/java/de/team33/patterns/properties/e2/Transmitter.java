package de.team33.patterns.properties.e2;

/**
 * Abstracts a tool that serves to transmit the properties of instances of a certain type to other instances of the
 * same type.
 *
 * @param <T> The type whose instances can be transmitted.
 */
public interface Transmitter<T> {

    /**
     * Transmits the properties of an instance of the underlying type to another instance of the same type.
     *
     * @param origin The original instance.
     * @param target The target instance.
     * @return The target instance.
     */
    T transmit(T origin, T target);
}
