package de.team33.patterns.building.elara;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Serves as a base class for builder implementations {@code <B>} and as such provides a model that separates basic
 * builder concepts from the actual data container model {@code <C>}.
 *
 * @param <C> The container type: an instance of that type is associated with the builder instance
 *            to hold the data to be collected during the build process.
 *            That type is expected to be mutable, at least in the scope of the concrete builder implementation.
 * @param <B> The builder type: the effective type of the derived builder implementation,
 *            at least this type itself.
 */
public class ProtoBuilder<C, B extends ProtoBuilder<C, B>> {

    private static final String ILLEGAL_BUILDER_CLASS =
            "<builderClass> is expected to represent <this> (%s) - but was %s";

    private final C container;

    /**
     * Initializes a new instance.
     *
     * @param container    The container instance to be associated with the builder.
     *                     The implementation assumes that it is exclusively available to the builder,
     *                     at least for the course of the build process.
     * @param builderClass The {@linkplain Class class representation} of the builder type.
     * @throws IllegalArgumentException if the specified builder class does not represent the instance to create.
     */
    protected ProtoBuilder(final C container, final Class<B> builderClass) {
        if (builderClass.isAssignableFrom(getClass())) {
            this.container = container;
        } else {
            throw new IllegalArgumentException(String.format(ILLEGAL_BUILDER_CLASS, getClass(), builderClass));
        }
    }

    /**
     * Applies the associated <em>container</em> to a given {@link Consumer} and returns {@code this}.
     */
    @SuppressWarnings({"DesignForExtension", "unchecked"})
    protected B setup(final Consumer<C> consumer) {
        consumer.accept(container);
        return (B) this;
    }

    /**
     * Returns the result of the given {@code function} on which the associated container was applied.
     * <p>
     * Be careful with the {@linkplain Function#identity() identity function} if the builder doesn't have a typical,
     * narrowly defined lifecycle.
     *
     * @param <R> The result type.
     */
    @SuppressWarnings("DesignForExtension")
    protected <R> R build(final Function<C, R> function) {
        return function.apply(container);
    }
}
