package de.team33.patterns.building.elara;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Serves as a base class for builder-like charger implementations {@code <C>} and as such provides a model that
 * separates basic builder concepts from the actual target data model {@code <T>}.
 * <p>
 * This implementation can be used as a base if an instance of the target type should be associated with the charger
 * instance from the start and also be used as the result of the charging process.
 * <p>
 * An instance of this charger type is limited to single use.
 * Once the terminating {@link #charged()} method has been used, subsequent calls to {@link Setup#setup(Consumer)} throw an
 * {@link IllegalStateException}.
 *
 * @param <T> The target type: an instance of that type is associated with the charger instance
 *            to hold the data to be collected during the build process.
 *            That type is expected to be mutable, at least in the scope of the concrete charger implementation.
 * @param <C> The charger type: the intended effective type of the concrete charger implementation.
 */
public class Charger<T, C extends Charger<T, C>> extends ProtoBuilder<T, C> {

    /**
     * Initializes a new instance.
     *
     * @param target       The target instance to be associated with the charger.
     *                     The implementation assumes that it is exclusively available to the charger
     *                     for the course of the build process.
     * @param chargerClass The {@link Class} representation of the intended effective charger type.
     * @throws IllegalArgumentException if the given charger class does not represent <em>this</em> instance.
     */
    protected Charger(final T target, final Class<C> chargerClass) {
        super(target, newLifecycle(), chargerClass);
    }

    private static ProtoBuilder.Lifecycle newLifecycle() {
        return new ProtoBuilder.Lifecycle() {

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
     * Subsequent calls of {@link Setup#setup(Consumer)} will lead to an {@link IllegalStateException}!
     */
    public final T charged() {
        return build(Function.identity());
    }
}
