package de.team33.patterns.arbitrary.mimas;

import java.util.Arrays;

/**
 * A utility interface: can extend producer classes with the functionality to create instances of types composed
 * (essentially) of properties and constructed like a <b><i>record</i></b> (as defined with Java 17).
 * This means that the type in question must provide a public constructor that takes all properties as parameters.
 * It may or may not actually be a <b><i>record</i></b>.
 * <p>
 * If there are multiple constructors, it is assumed that the public constructor with the largest number of parameters
 * should be used.
 *
 * @see de.team33.patterns.arbitrary.mimas package
 */
public interface Initiator {

    /**
     * Creates and returns a new instance of a given target type.
     * The target type is assumed to have a constructor that must be parameterized with property values.
     * If the type has multiple constructors, it is assumed that the constructor with the largest number of parameters
     * should be used.
     *
     * @param targetType The {@link Class} representation of the target type.
     * @param ignore The names of methods or constructor arguments that shell be ignored.
     * @param <T> The target type.
     */
    default <T> T initiate(final Class<T> targetType, final String... ignore) {
        return new Initiating<>(this, targetType, Arrays.asList(ignore)).result();
    }
}
