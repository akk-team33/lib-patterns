package de.team33.patterns.expiry.tethys;

import java.util.function.Supplier;

/**
 * Defines a container type for handling instances, which in principle can be defined globally and
 * reused over and over again, but have to be updated after a certain time.
 * <p>
 * <b>Problem to solve:</b>
 * <p>
 * There are objects that, due to their technical properties, are predestined to be initialized and made available once
 * throughout the application. In particular, they are state-free but relatively "expensive" to initialize. Due to
 * their semantics, however, they have to be renewed from time to time. An example of such an object could be an
 * authentication token.
 * <p>
 * This class serves to handle such objects and in particular their updating.
 *
 * @param <T> The type of instances to handle.
 */
public class Recent<T> extends Mutual<T, RuntimeException> implements Supplier<T> {

    /**
     * Initializes a new instance of this container type given a {@link Supplier} for the type to be handled and
     * an intended lifetime of such instances.
     * <p>
     * CAUTION: The given lifetime should be significantly smaller than the actually expected
     * life span of an instance to be handled, otherwise there may not be enough time to use a
     * {@linkplain #get() provided} instance successfully!
     *
     * @param newSubject the {@link Supplier} for the instances to handle
     * @param maxIdle    the maximum idle time in milliseconds
     * @param maxLiving  the maximum lifetime in milliseconds
     */
    public Recent(final Supplier<? extends T> newSubject, final long maxIdle, final long maxLiving) {
        super(newSubject::get, maxIdle, maxLiving);
    }

    @Override
    public final T get() {
        return super.get();
    }
}
