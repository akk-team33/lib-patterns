package de.team33.patterns.random.tarvos;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * A utility for the blanket initialization of instances composed of properties that can be set using typical setters.
 * <p>
 * The values for this are supplied by a source instance of a specific type.
 */
public interface Charger {

    /**
     * Initializes the properties of a given {@code target} instance with values from a given {@code source} instance
     * and returns the initialized {@code target}.
     * <p>
     * In order for a property to be initialized, the {@code target}'s class must provide a corresponding setter
     * that is actually accessible from this {@link Charger}. In addition, the class of the {@code source} must
     * provide a parameterless method that can return an appropriate value for the property in question.
     */
    default <T> T charge(final T target, final String... ignore) {
        return Charging.charge(target, this, Arrays.asList(ignore));
    }

    default void chargerLog(final Supplier<String> message, final Exception cause) {
        Charging.log(message, cause);
    }
}
