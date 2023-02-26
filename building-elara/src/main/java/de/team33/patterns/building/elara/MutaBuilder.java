package de.team33.patterns.building.elara;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Serves as a base class for builder implementations {@code <B>} and as such provides a model that separates basic
 * builder concepts from the resulting data model {@code <R>}.
 *
 * @param <R> The result type: an instance of that type is finally built.
 *            That type is expected to be mutable.
 * @param <B> The builder type: the effective type of the derived builder implementation,
 *            at least this type itself.
 */
public class MutaBuilder<R, B extends MutaBuilder<R, B>> implements Setup<R, B> {

    private final List<Consumer<R>> consumers;
    private final Supplier<R> newResult;

    /**
     * Initializes a new instance.
     *
     * @param newResult    A {@link Supplier} method to retrieve a new instance of the result type.
     * @param builderClass The {@linkplain Class class representation} of the final builder type.
     * @throws IllegalArgumentException if the specified builder class representation does not represent
     *                                  the instance to be created.
     */
    protected MutaBuilder(final Supplier<R> newResult, final Class<B> builderClass) {
        Building.ensureAssignable(builderClass, getClass());
        this.consumers = new LinkedList<>();
        this.newResult = newResult;
    }

    /**
     * Accepts a {@link Consumer} as modifying operation to be performed on a new result instance
     * while {@link #build()} and returns {@code this} builder itself.
     */
    @Override
    @SuppressWarnings("unchecked") // is actually checked in constructor
    public final B setup(final Consumer<R> consumer) {
        consumers.add(consumer);
        return (B) this;
    }

    /**
     * Returns the build result.
     */
    public final R build() {
        return build(newResult.get());
    }

    private R build(final R result) {
        for (final Consumer<R> consumer : consumers) {
            consumer.accept(result);
        }
        return result;
    }
}
