package de.team33.patterns.random.tarvos;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * A utility interface. Can extend generator classes with the functionality to fill POJO* instances with (typically)
 * random values. Within certain limits, this also works with instances according to the builder pattern.
 * <p>
 * *A POJO (plain old java object) is an instance consisting (essentially) of properties that can be accessed using
 * appropriate getters and setters.
 * <p>
 * The values for this are supplied by a source instance of a specific type.
 */
public interface Charger {

    /**
     * Initializes the properties of a given {@code target} instance with values provided by this Charger instance
     * itself and returns the initialized target.
     * <p>
     * In order for a property to be initialized, the {@code target}'s class must provide a corresponding setter
     * that is actually accessible from this {@link Charger}. In addition, this {@link Charger}'s implementation class
     * must provide a parameterless method that can return an appropriate value for the property in question.
     * <p>
     * The default implementation provides the intended functionality.
     * It makes little sense to override this method or then even use this interface at all.
     *
     * @param target The target of the operation.
     * @param ignore The names of methods that shell be ignored.
     * @param <T> The target type.
     * @return The target.
     */
    default <T> T charge(final T target, final String... ignore) {
        return new Charging<>(this, target, Arrays.asList(ignore)).result();
    }

    /**
     * The logging method used by this {@link Charger}. It is used if errors or discrepancies occur during
     * {@linkplain Charger#charge(Object, String...) charging}.
     * <p>
     * The default implementation uses java logging. It can and should be overwritten if no java logging is desired.
     */
    default void chargerLog(final Supplier<String> message, final Exception cause) {
        Charging.defaultLog(message, cause);
    }
}