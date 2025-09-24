package de.team33.patterns.building.elara;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Serves as a base class for builder implementations {@code <B>} and as such provides a model that separates basic
 * builder concepts from the actual core data model {@code <C>} that holds the actual values for the final result.
 * <p>
 * This implementation can be used as a base if an instance of the core type should be linked to the builder
 * instance from the start, but the result of the build process shouldn't necessarily be of the core type,
 * but instead a mapping exists that converts the core type to the final result type.
 * <p>
 * Core type and result type can also be identical,
 * in particular the final result can be identical to the originally associated core instance
 * (if e.g. {@link Function#identity()} is used as mapping).
 * But then you have to be careful with the life cycle of the builder instance!
 *
 * @param <C> The core type: an instance of that type is associated with the builder instance
 *            to hold the data to be collected during the build process.
 *            That type is expected to be mutable, at least in the scope of the concrete builder implementation.
 * @param <B> The builder type: the intended effective type of the concrete builder implementation.
 */
@SuppressWarnings("deprecation")
public class ProtoBuilder<C, B extends ProtoBuilder<C, B>> extends BuilderBase<B> implements Setup<C, B> {

    private final C core;
    private final Lifecycle lifecycle;

    /**
     * Initializes a new instance.
     *
     * @param core         The core instance to be associated with the builder.
     *                     The implementation assumes that it is exclusively available to the builder,
     *                     at least for the course of the build process.
     * @param builderClass The {@link Class} representation of the intended effective builder type.
     * @throws IllegalArgumentException if the given builder class does not represent <em>this</em> instance.
     */
    protected ProtoBuilder(final C core, final Class<B> builderClass) {
        this(core, Lifecycle.INFINITE, builderClass);
    }

    /**
     * Initializes a new instance.
     *
     * @param core         The core instance to be associated with the builder.
     *                     The implementation assumes that it is exclusively available to the builder,
     *                     at least for the course of the build process.
     * @param lifecycle    A specific lifecycle controller for this instance.
     * @param builderClass The {@link Class} representation of the intended effective builder type.
     * @throws IllegalArgumentException if the given builder class does not represent <em>this</em> instance.
     */
    protected ProtoBuilder(final C core, final Lifecycle lifecycle, final Class<B> builderClass) {
        super(builderClass);
        this.core = core;
        this.lifecycle = lifecycle;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation applies the given consumer directly to the associated core instance.
     */
    @Override
    public final B setup(final Consumer<? super C> consumer) {
        lifecycle.check();
        consumer.accept(core);
        return THIS();
    }

    /**
     * Applies the <em>core</em> of <em>this</em> builder to the given {@link Function mapping} and returns the result.
     * <p>
     * Be careful with the {@linkplain Function#identity() identity function} if the builder doesn't have a
     * narrowly defined lifecycle.
     *
     * @param <R> The result type.
     */
    protected final <R> R build(final Function<? super C, R> mapping) {
        lifecycle.increment();
        return mapping.apply(core);
    }

    /**
     * Abstracts a lifecycle controller for a {@link ProtoBuilder} implementation.
     */
    public interface Lifecycle {

        /**
         * Defines a simple lifecycle controller for implementations that don't actually need one.
         */
        Lifecycle INFINITE = new Lifecycle() {
            @Override
            public void check() {
            }

            @Override
            public void increment() {
            }
        };

        /**
         * Will be called on any setup step.
         *
         * @throws IllegalStateException if the lifecycle does not allow further setup on the associated <em>core</em>
         *                               instance.
         */
        void check();

        /**
         * Will be called on any final build step.
         */
        void increment();
    }
}
