package de.team33.patterns.serving.naiad;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Abstracts an <em>"aspect"</em> as a component of a service.
 * More precisely: a kind of property that can be useful as part of an interactive application's business service.
 * While a property in the common sense simply represents a value of a certain type and, in particular,
 * may be readable and/or modifiable, an <em>"aspect"</em> is additionally able to provide an interface to notify
 * an audience of changes in its content.
 * Also, unlike a normal property, an <em>"aspect"</em> has no value semantics, even if the underlying type has
 * value semantics. In other words: two aspects with different identities are never
 * {@link Object#equals(Object) equal}, even if their contained values are {@link Object#equals(Object) equal}.
 * Finally, an <em>"aspect"</em> is not to be viewed as a representation of a value,
 * but rather as a container for a value.
 *
 * @param <T> The type of the contained value.
 */
public interface Aspect<T> {

    /**
     * Abstracts a readable {@link Aspect}.
     *
     * @param <T> The type of the contained value.
     */
    @FunctionalInterface
    interface Readable<T> extends Supplier<T>, Aspect<T> {
    }

    /**
     * Abstracts a modifiable {@link Aspect}.
     *
     * @param <T> The type of the contained value.
     */
    @FunctionalInterface
    interface Modifiable<T> extends Consumer<T>, Aspect<T> {
    }

    @FunctionalInterface
    interface Connectable<T> extends Aspect<T> {

        void connect(Consumer<T> listener);
    }
}
