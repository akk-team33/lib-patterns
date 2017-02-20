package net.team33.patterns.reflect;

/**
 * A {@link Mapper} that is bound to a primary target class and offers some convenience methods to deal with that class.
 */
public abstract class Copier<P> implements Mapper {

    private final Class<P> prime;

    protected Copier(final Class<P> prime) {
        this.prime = prime;
    }

    /**
     * Copies properties from an origin to a new instance, both of the primary class.
     */
    public final P copy(final P origin) {
        return map(origin, prime);
    }

    /**
     * Maps properties from an origin to a new instance the primary class.
     */
    public final P map(final Object origin) {
        return map(origin, prime);
    }
}
