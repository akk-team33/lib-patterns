package de.team33.patterns.building.elara;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Serves as a base class for builder implementations {@code <B>} and as such provides a model that separates basic
 * builder concepts from the target data model {@code <T>}.
 * <p>
 * This implementation can be used as a base if instances of a target type are to be built that is itself mutable
 * but does not itself implement a builder pattern. The builder initially only collects the modifying operations
 * and only applies them to a newly created target instance with build().
 *
 * @param <T> The target type: an instance of that type is finally built.
 *            That type is expected to be mutable.
 * @param <B> The builder type: the intended effective type of the concrete builder implementation.
 */
public class LateBuilder<T, B extends LateBuilder<T, B>> extends BuilderBase<B> implements Setup<T, B> {

    private final List<Consumer<T>> setups;
    private final Supplier<T> newResult;

    /**
     * Initializes a new instance.
     *
     * @param newResult    A {@link Supplier} method to retrieve a new instance of the result type.
     * @param builderClass The {@link Class} representation of the intended effective builder type.
     * @throws IllegalArgumentException if the specified builder class does not represent the instance to create.
     */
    protected LateBuilder(final Supplier<T> newResult, final Class<B> builderClass) {
        super(builderClass);
        this.setups = new LinkedList<>();
        this.newResult = newResult;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation stores the given consumer in order to apply it to a newly created target during
     * {@link #build()}.
     */
    @Override
    public final B setup(final Consumer<T> consumer) {
        setups.add(consumer);
        return THIS();
    }

    /**
     * Returns the build result.
     */
    public final T build() {
        return build(newResult.get());
    }

    private T build(final T result) {
        for (final Consumer<T> consumer : setups) {
            consumer.accept(result);
        }
        return result;
    }
}
