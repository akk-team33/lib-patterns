package de.team33.patterns.serial.charon;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * Abstracts a series of elements of a certain type and as such represents an alternative view of a {@link Collection}.
 * <p>
 * In this view, a series essentially consists of two components:
 * <ul>
 *     <li>The first element ("head", an element that may be missing)</li>
 *     <li>The rest of the sequence ("tail", again a sequence)</li>
 * </ul>
 * <p>
 * If the first component ("head") is missing, the sequence is empty.
 * Then the rest of the sequence ("tail") is also empty.
 * <p>
 * A sequence according to this definition is immutable and cannot contain null elements.
 * In addition to the components mentioned above, it has a certain size (number of items contained),
 * can be streamed and presented as a "normal" {@link List}.
 *
 * @param <E> The element type.
 */
public interface Series<E> {

    /**
     * Returns an empty {@link Series}.
     */
    static <E> Series<E> empty() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Returns a {@link Series} composed of the given elements in the given order.
     * Any {@code null} elements given are ignored and are not included in the resulting {@link Sequence}.
     */
    @SafeVarargs
    static <E> Series<E> of(final E... elements) {
        return of(List.of(elements));
    }

    /**
     * Returns a {@link Series} composed of the given {@link Collection}'s elements in the given order.
     * Any {@code null} elements given are ignored and are not included in the resulting {@link Series}.
     */
    public static <E> Series<E> of(final Collection<? extends E> elements) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    static <E> Series<E> collecting(final Stream<? extends E> elements) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
