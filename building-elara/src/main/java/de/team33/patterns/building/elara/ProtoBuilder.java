package de.team33.patterns.building.elara;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Serves as a base class for builder implementations {@code <B>} and as such provides a model that separates basic
 * builder concepts from the actual target data model {@code <T>}.
 * <p>
 * This implementation can be used as a base if an instance of the target type should be linked to the builder
 * instance from the start, but the result of the build process shouldn't necessarily be of the target type,
 * but instead a mapping exists that converts the target type to the final result type.
 * <p>
 * Target type and result type can also be identical,
 * in particular the final result can be identical to the originally associated target instance
 * (if e.g. {@link Function#identity()} is used as mapping).
 * But then you have to be careful with the life cycle of the builder instance!
 *
 * @param <T> The target type: an instance of that type is associated with the builder instance
 *            to hold the data to be collected during the build process.
 *            That type is expected to be mutable, at least in the scope of the concrete builder implementation.
 * @param <B> The builder type: the intended effective type of the concrete builder implementation.
 */
public class ProtoBuilder<T, B extends ProtoBuilder<T, B>> extends BuilderBase<B> implements Setup<T, B> {

    private final T target;

    /**
     * Initializes a new instance.
     *
     * @param target       The target instance to be associated with the builder.
     *                     The implementation assumes that it is exclusively available to the builder,
     *                     at least for the course of the build process.
     * @param builderClass The {@link Class} representation of the intended effective builder type.
     * @throws IllegalArgumentException if the specified builder class does not represent the instance to create.
     */
    protected ProtoBuilder(final T target, final Class<B> builderClass) {
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
        consumer.accept(target);
        return THIS();
    }

    /**
     * Returns the result of the given {@code function} on which the associated target was applied.
     * <p>
     * Be careful with the {@linkplain Function#identity() identity function} if the builder doesn't have a
     * narrowly defined lifecycle.
     *
     * @param <R> The result type.
     */
    protected final <R> R build(final Function<T, R> function) {
        return function.apply(target);
    }
}
