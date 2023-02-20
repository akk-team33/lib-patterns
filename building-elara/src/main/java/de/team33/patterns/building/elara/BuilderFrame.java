package de.team33.patterns.building.elara;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Serves as the base class for builder implementations and as such provides a model that separates basic
 * builder concepts from the actual data model.
 *
 * @param <C> The container type: an instance of that type is associated with the builder instance
 *            to hold the data to be collected during the build process.
 *            That type is mutable, at least in the scope of the concrete builder implementation.
 * @param <B> The builder type: the effective type of the derived builder implementation,
 *            at least this type itself.
 */
public abstract class BuilderFrame<C, B extends BuilderFrame<C, B>> {

    private static final String ILLEGAL_BUILDER_CLASS =
            "<builderClass> is expected to represent <this> (%s) - but was %s";

    private final C container;
    private final Class<B> builderClass;

    /**
     * Initializes a new instance.
     *
     * @param container    The container instance to be associated with the builder.
     *                     The implementation assumes that it is exclusively available to the builder,
     *                     at least for the course of the build process.
     * @param builderClass The {@linkplain Class class representation} of the builder type.
     */
    protected BuilderFrame(final C container, final Class<B> builderClass) {
        if (builderClass.isAssignableFrom(getClass())) {
            this.container = container;
            this.builderClass = builderClass;
        } else {
            throw new IllegalArgumentException(String.format(ILLEGAL_BUILDER_CLASS, getClass(), builderClass));
        }
    }

    /**
     * Returns the result of the given {@code function} on which the associated container was applied.
     * <p>
     * Be careful with the {@linkplain Function#identity() identity function} if the builder doesn't have a typical,
     * narrowly defined lifecycle.
     *
     * @param <R> The result type.
     */
    protected final <R> R build(final Function<C, R> function) {
        return function.apply(container);
    }

    /**
     * Applies the associated <em>container</em> to a given {@link Consumer} and returns {@code this}.
     */
    public final B setup(final Consumer<C> consumer) {
        consumer.accept(container);
        return builderClass.cast(this);
    }
}
