package de.team33.patterns.pooling.v1;

import java.util.function.Supplier;

/**
 * A tool that makes instances of a certain type available for the course of operations that require such an instance.
 * The instances provided are referred to as <em>subject</em> in the following.
 * <p>
 * The tool maintains a number of such <em>subjects</em> and only creates as many as are actually required at most at
 * the same time in its application context. The <em>subjects</em> are retained and reused for subsequent operations.
 * <p>
 * In this respect, this tool is suitable for providing <em>subject</em> types whose instantiation is relatively
 * "expensive", which are rather unsuitable for concurrent access, but are designed for multiple or permanent use.
 * Database or other client-server connections, but also {@link java.util.Random} instances, may be an example.
 * <p>
 * Note: this implementation cannot detect when an internal operation is taking place in the course of an operation to
 * which the same <em>subject</em> could be made available.
 * <p>
 * This implementation does not support checked exceptions to occur while creating new <em>subject</em> instances.
 *
 * @param <S> The type of provided instances <em>(subjects)</em>.
 * @see XProvider
 */
public class Provider<S> extends XProvider<S, RuntimeException> {

    public Provider(final Supplier<S> newItem) {
        super(newItem::get);
    }
}
