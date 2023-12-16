package de.team33.patterns.pooling.ariel;

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
 * This implementation does not support checked exceptions to occur while creating new <em>item</em> instances.
 *
 * @param <I> The type of provided instances <em>(items)</em>.
 * @see RProvider
 * @see XProvider
 * @see XRProvider
 */
public class Provider<I> extends ProviderBase<I> {

    /**
     * Initializes a new instance giving a {@link Supplier} that defines the intended initialization of a
     * new <em>item</em>.
     */
    public Provider(final Supplier<I> newItem) {
        super(() -> {
            final I item = newItem.get();
            return () -> item;
        });
    }
}
