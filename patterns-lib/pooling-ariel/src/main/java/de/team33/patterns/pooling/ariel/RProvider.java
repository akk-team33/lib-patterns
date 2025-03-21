package de.team33.patterns.pooling.ariel;

import de.team33.patterns.expiry.tethys.Recent;

import java.util.function.Supplier;

/**
 * A tool that makes instances of a certain type available for the course of operations that require such an instance.
 * The instances provided are referred to as <em>item</em> in the following.
 * <p>
 * The tool maintains a number of such <em>items</em> and only creates as many as are actually required at most at
 * the same time in its application context. The <em>items</em> are retained and reused for subsequent operations.
 * <p>
 * In this respect, this tool is suitable for providing <em>item</em> types whose instantiation is relatively
 * "expensive", which are rather unsuitable for concurrent access, but are designed for multiple or permanent use.
 * Database or other client-server connections may be an example.
 * <p>
 * Note: this implementation cannot detect when an internal operation is taking place in the course of an operation to
 * which the same <em>item</em> could be made available.
 * <p>
 * This implementation supports expiry and reinitialisation of provided <em>items</em>.
 * <p>
 * This implementation does not support checked exceptions to occur while creating new <em>item</em> instances.
 *
 * @param <I> The type of provided instances <em>(items)</em>.
 * @see Provider
 * @see XProvider
 * @see XRProvider
 */
public class RProvider<I> extends ProviderBase<I> {

    /**
     * Initializes a new instance giving a {@link Supplier} that defines the intended initialization of a
     * new <em>item</em>.
     * <p>
     * Once an instance <em>item</em> is initialized it will expire and be renewed after a maximum idle time
     * or at least after a maximum lifetime.
     */
    public RProvider(final Supplier<? extends I> newItem, final long maxIdle, final long maxLiving) {
        super(() -> new Recent<>(newItem, maxIdle, maxLiving)::get);
    }
}
