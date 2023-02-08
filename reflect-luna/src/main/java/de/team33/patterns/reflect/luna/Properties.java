package de.team33.patterns.reflect.luna;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a tool for typical data objects whose value is composed of properties.
 * In particular, it simplifies the implementation of {@code equals()}, {@code hashCode()} and {@code toString()}
 * as well as that of a copy constructor if they are to operate on the properties of an instance.
 * <p>
 * A typical implementation is based on reflection.
 *
 * @param <T> The type of data objects to support.
 */
public interface Properties<T> {

    /**
     * Copies the properties of a source instance to a target instance.
     * The individual properties are not cloned in this process!
     */
    default void copy(final T source, final T target) {
        copy(source, target, Cloning.NOTHING);
    }

    /**
     * Copies the properties of a source instance to a target instance.
     * The individual properties are cloned according to the given {@link Cloning} policy!
     */
    void copy(T source, T target, Cloning cloning);

    /**
     * Returns a {@link Stream} of the values of all significant properties of an instance of the associated type.
     */
    Stream<Object> stream(T origin);

    /**
     * Returns a {@link List} of the values of all significant properties of an instance of the associated type.
     */
    default List<Object> list(T origin) {
        return stream(origin).collect(Collectors.toList());
    }
}
