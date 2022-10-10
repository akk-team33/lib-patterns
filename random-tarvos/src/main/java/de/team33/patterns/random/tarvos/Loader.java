package de.team33.patterns.random.tarvos;

/**
 * A utility for the blanket initialization of instances composed of properties that can be set using typical setters.
 * <p>
 * The values for this are supplied by a source instance of a specific type.
 * <p>
 * TODO: rename to Charger
 *
 * @param <S> The source type
 */
public class Loader<S> {

    public Loader(final Class<S> sourceType) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public final <T> T load(final T target, final S source) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
