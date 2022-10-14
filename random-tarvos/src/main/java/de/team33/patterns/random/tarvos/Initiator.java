package de.team33.patterns.random.tarvos;

/**
 * A utility interface. Can extend generator classes with the functionality to create instances of types composed
 * (essentially) of properties and constructed like a <b><i>record</i></b> (as defined with Java 17).
 * This means that the type in question must provide a public constructor that takes all properties as parameters.
 * It may or may not actually be a <b><i>record</i></b>.
 * <p>
 * If there are multiple constructors, it is assumed that the public constructor with the largest number of parameters
 * should be used.
 */
public interface Initiator {

    default <T> T initiate(final Class<T> targetType) {
        return new Initiating<>(this, targetType).result();
    }
}
