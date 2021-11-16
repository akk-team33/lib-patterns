package de.team33.patterns.production.e1;

/**
 * Defines an initializer for Instances of a specific typ.
 *
 * @param <T> The type of instances to be initialized.
 */
@FunctionalInterface
public interface Initializer<T> {

    /**
     * Initializes a given {@code subject}.
     *
     * @return the {@code subject} itself.
     */
    T init(T subject);
}
