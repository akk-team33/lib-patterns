package de.team33.patterns.stream.galatea;

public interface Aggregate<E> extends Streamable<E> {

    /**
     * Returns the number of elements in <em>this</em> aggregate.
     */
    long size();

    /**
     * Returns {@code true} if <em>this</em> aggregate contains no elements.
     */
    default boolean isEmpty() {
        return 1L > size();
    }

    /**
     * Returns {@code true} if <em>this</em> aggregate contains any element.
     */
    default boolean containsAny() {
        return 0L < size();
    }
}
