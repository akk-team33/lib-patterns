package de.team33.patterns.building.elara;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Serves as a base class for builder-like implementations {@code <B>} and as such provides a model that separates
 * basic builder concepts from the actual target data model {@code <T>}.
 * <p>
 * This implementation can be used as a base if an instance of the target type should be associated with the builder
 * instance from the start and also be used as the result of the build process.
 * <p>
 * An instance of this builder type is limited to single use.
 * Once the terminating {@link #charged()} method has been used, subsequent calls to {@link #setup(Consumer)} throw an
 * {@link IllegalStateException}.
 *
 * @param <T> The target type: an instance of that type is associated with the builder instance
 *            to hold the data to be collected during the build process.
 *            That type is expected to be mutable, at least in the scope of the concrete builder implementation.
 * @param <B> The builder type: the intended effective type of the concrete builder implementation.
 */
public class Charger<T, B extends Charger<T, B>> extends ProtoBuilder<T, B> implements Setup<T, B> {

    /**
     * Initializes a new instance.
     *
     * @param target       The target instance to be associated with the builder.
     *                     The implementation assumes that it is exclusively available to the builder
     *                     for the course of the build process.
     * @param builderClass The {@link Class} representation of the intended effective builder type.
     * @throws IllegalArgumentException if the given builder class does not represent <em>this</em> instance.
     */
    protected Charger(final T target, final Class<B> builderClass) {
        super(target, newLifecycle(), builderClass);
    }

    private static Lifecycle newLifecycle() {
        return new Lifecycle() {

            private boolean charged = false;

            @Override
            public void check() {
                if (charged) {
                    throw new IllegalStateException("the associated target is already released");
                }
            }

            @Override
            public void increment() {
                charged = true;
            }
        };
    }

    /**
     * Returns the associated target instance.
     * <p>
     * Subsequent calls of {@link #setup(Consumer)} will lead to an {@link IllegalStateException}!
     */
    public final T charged() {
        return build(Function.identity());
    }
}
