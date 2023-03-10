package de.team33.patterns.building.elara;

import java.util.function.Consumer;

/**
 * Serves as a base class for builder-like implementations {@code <B>} and as such provides a model that separates
 * basic builder concepts from the actual target data model {@code <T>}.
 * <p>
 * This implementation can be used as a base if an instance of the target type should be associated with the builder
 * instance from the start and also be used as the result of the build process.
 * <p>
 * An instance of this builder type is limited to single use.
 * Once the terminating {@link #release} method has been used, subsequent calls to setup() throw an
 * IllegalStateException.
 *
 * @param <T> The target type: an instance of that type is associated with the builder instance
 *            to hold the data to be collected during the build process.
 *            That type is expected to be mutable, at least in the scope of the concrete builder implementation.
 * @param <B> The builder type: the intended effective type of the concrete builder implementation.
 */
public class Charger<T, B extends Charger<T, B>> extends BuilderBase<B> implements Setup<T, B> {

    private final T target;
    private boolean released = false;

    /**
     * Initializes a new instance.
     *
     * @param target       The target instance to be associated with the builder.
     *                     The implementation assumes that it is exclusively available to the builder
     *                     for the course of the build process.
     * @param builderClass The {@link Class} representation of the intended effective builder type.
     * @throws IllegalArgumentException if the specified builder class does not represent the instance to create.
     */
    protected Charger(final T target, final Class<B> builderClass) {
        super(builderClass);
        this.target = target;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation applies the given consumer directly to the associated target.
     */
    @Override
    public final B setup(final Consumer<T> consumer) {
        if (released) {
            throw new IllegalStateException("the associated target is already released");
        }
        consumer.accept(target);
        return THIS();
    }

    /**
     * Returns the associated target instance.
     * <p>
     * Subsequent calls of {@link #setup(Consumer)} will lead to an {@link IllegalStateException}.
     */
    public final T release() {
        released = true;
        return target;
    }
}
