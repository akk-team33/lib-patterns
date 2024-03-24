package de.team33.patterns.building.elara;

import java.util.function.Function;

/**
 * Serves as a base class for builder implementations {@code <B>} and as such provides a model that separates basic
 * builder concepts from the actual core data model {@code <C>}.
 * <p>
 * This implementation can be used as a base if an instance of the core type {@code <C>} should be linked to the
 * builder instance from the start, but the result of the build process shouldn't necessarily be of the core type,
 * but instead a mapping exists that converts the core type {@code <C>} to the final target type {@code <T>}.
 * <p>
 * Core type {@code <C>} and target type {@code <T>} may also be identical, but avoid the final result to be identical
 * to the originally associated target instance (e.g. when using {@link Function#identity()} as mapping).
 * You should prefer {@link Charger} in this particular case!
 *
 * @param <T> The target type: an instance of that type will be returned by {@link #build()}.
 * @param <C> The core type: an instance of that type is associated with the builder instance
 *            to hold the data to be collected during the build process.
 *            That type is expected to be mutable, at least in the scope of the concrete builder implementation.
 * @param <B> The builder type: the intended effective type of the concrete builder implementation.
 */
public class POJOBuilder<C, T, B extends ProtoBuilder<C, B>> extends ProtoBuilder<C, B> {

    private final Function<C, T> mapping;

    /**
     * Initializes a new instance.
     *
     * @param core         The core instance to be associated with the builder.
     *                     The implementation assumes that it is exclusively available to the builder,
     *                     at least for the course of the build process.
     * @param mapping      A {@link Function} to map the associated core to the final target.
     *                     Even if the core type {@code <C>} matches the target type {@code <T>} that {@link Function}
     *                     should create a new target instance (otherwise prefer {@link Charger}).
     * @param builderClass The {@link Class} representation of the intended effective builder type.
     * @throws IllegalArgumentException if the given builder class does not represent <em>this</em> instance.
     */
    protected POJOBuilder(final C core, final Function<C, T> mapping, final Class<B> builderClass) {
        super(core, builderClass);
        this.mapping = mapping;
    }

    /**
     * Applies the <em>core</em> of <em>this</em> builder to the given {@link Function} and returns the result.
     * <p>
     * While the {@link Function} is being called, the <em>core</em> is exclusively available to it,
     * but must not be "hijacked" from the context of the call or the executing thread!
     */
    public final <R> R peek(final Function<C, R> function) {
        return build(function);
    }

    public final T build() {
        return build(mapping);
    }
}
