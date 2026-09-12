package de.team33.patterns.streamable.naiad;

import de.team33.patterns.streamable.galatea.Buffer;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.function.Predicate.not;

/**
 * @deprecated use {@link de.team33.patterns.streamable.galatea.Streamable}
 * or {@link de.team33.patterns.streamable.galatea.Buffer} instead.
 */
@SuppressWarnings("OverlyStrongTypeCast")
@Deprecated
@FunctionalInterface
public interface Streamable<E> extends de.team33.patterns.streamable.galatea.Streamable<E> {

    /**
     * @deprecated use {@link de.team33.patterns.streamable.galatea.Streamable#empty()}
     * or {@link Buffer#empty()} instead.
     */
    @Deprecated
    static <E> Streamable<E> empty() {
        return Stream::empty;
    }

    /**
     * @deprecated use {@link de.team33.patterns.streamable.galatea.Streamable#of(Object)}
     * or {@link de.team33.patterns.streamable.galatea.Buffer#of(Object)} instead.
     */
    @Deprecated
    static <E> Streamable<E> of(final E element) {
        return () -> Stream.of(element);
    }

    /**
     * @deprecated use {@link de.team33.patterns.streamable.galatea.Streamable#of(Object, Object, Object[])}
     * or {@link de.team33.patterns.streamable.galatea.Buffer#of(Object, Object, Object[])} instead.
     */
    @Deprecated
    @SafeVarargs
    static <E> Streamable<E> of(final E element0, final E element1, final E... more) {
        return () -> Stream.concat(Stream.of(element0, element1), Stream.of(more));
    }

    /**
     * @deprecated use {@link de.team33.patterns.streamable.galatea.Streamable#of(Object[])}
     * or {@link de.team33.patterns.streamable.galatea.Buffer#of(Object[])} instead.
     */
    @Deprecated
    static <E> Streamable<E> of(final E[] elements) {
        return () -> Stream.of(elements);
    }

    /**
     * @deprecated use {@link de.team33.patterns.streamable.galatea.Streamable#of(Iterable)}
     * or {@link de.team33.patterns.streamable.galatea.Buffer#of(Iterable)} instead.
     */
    @Deprecated
    static <E> Streamable<E> of(final Iterable<E> iterable) {
        if (iterable instanceof final Collection<E> collection) {
            return collection::stream;
        } else {
            return () -> StreamSupport.stream(iterable.spliterator(), false);
        }
    }

    /**
     * @deprecated use
     * {@link de.team33.patterns.streamable.galatea.Streamable#cast(de.team33.patterns.streamable.galatea.Streamable)}
     * instead.
     */
    @Deprecated
    @SuppressWarnings({"unchecked", "FunctionalExpressionCanBeFolded", "LambdaUnfriendlyMethodOverload"})
    static <E, X extends E> Streamable<E> cast(final Streamable<X> streamable) {
        return ((Streamable<E>) streamable)::stream;
    }

    /**
     * @deprecated use {@link de.team33.patterns.streamable.galatea.Buffer#add(Object)} instead.
     */
    @Deprecated
    default Streamable<E> add(final E element) {
        return addAll(of(element));
    }

    /**
     * @deprecated use
     * {@link de.team33.patterns.streamable.galatea.Buffer#addAll(de.team33.patterns.streamable.galatea.Streamable)}
     * instead.
     */
    @Deprecated
    default <X extends E> Streamable<E> addAll(final Streamable<X> other) {
        return () -> Stream.concat(this.stream(), other.stream());
    }

    /**
     * @deprecated use {@link de.team33.patterns.streamable.galatea.Buffer#remove(Object)} instead.
     */
    @Deprecated
    default Streamable<E> remove(final Object candidate) {
        return removeAll(of(candidate));
    }

    /**
     * @deprecated use
     * {@link de.team33.patterns.streamable.galatea.Buffer#removeAll(de.team33.patterns.streamable.galatea.Streamable)}
     * instead.
     */
    @Deprecated
    default <X> Streamable<E> removeAll(final Streamable<X> other) {
        return removeIf(other::contains);
    }

    /**
     * @deprecated use {@link de.team33.patterns.streamable.galatea.Buffer#removeIf(Predicate)} instead.
     */
    @Deprecated
    default Streamable<E> removeIf(final Predicate<? super E> condition) {
        return retainIf(not(condition));
    }

    /**
     * @deprecated use
     * {@link de.team33.patterns.streamable.galatea.Buffer#retainAll(de.team33.patterns.streamable.galatea.Streamable)}
     * instead.
     */
    @Deprecated
    default <X> Streamable<E> retainAll(final Streamable<X> other) {
        return retainIf(other::contains);
    }

    /**
     * @deprecated use {@link de.team33.patterns.streamable.galatea.Streamable}
     * or {@link de.team33.patterns.streamable.galatea.Buffer} instead.
     */
    @Deprecated
    default Streamable<E> retainIf(final Predicate<? super E> condition) {
        return () -> stream().filter(condition);
    }

    /**
     * @deprecated use {@link de.team33.patterns.streamable.galatea.Streamable#toList()} instead.
     */
    @Deprecated
    default List<E> toList() {
        return stream().toList();
    }

    /**
     * @deprecated use {@link de.team33.patterns.streamable.galatea.Streamable#toSet()} instead.
     */
    @Deprecated
    default Set<E> toSet() {
        return stream().collect(Collectors.toUnmodifiableSet());
    }

    /**
     * @deprecated use {@link de.team33.patterns.streamable.galatea.Buffer#map(Function)} instead.
     */
    @Deprecated
    default <T> T map(final Function<? super Streamable<E>, T> method) {
        return method.apply(this);
    }
}
