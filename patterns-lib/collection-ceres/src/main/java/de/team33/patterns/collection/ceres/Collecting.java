package de.team33.patterns.collection.ceres;

import de.team33.patterns.building.elara.LateBuilder;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

/**
 * {@linkplain Collections Additional} convenience methods to deal with Collections.
 */
@SuppressWarnings({
        "ClassWithTooManyMethods", "WeakerAccess",
        "ProhibitedExceptionCaught", "SuspiciousMethodCalls",
        "OverloadedMethodsWithSameNumberOfParameters"})
public final class Collecting {

    private static final Object[] EMPTY_ARRAY = {};

    private Collecting() {
    }

    /**
     * Just like {@link Collection#add(Object) subject.add(element)}, but returns the <em>subject</em>.
     *
     * @throws UnsupportedOperationException if {@link Collection#add(Object)} is not supported by the <em>subject</em>.
     * @throws NullPointerException          if <em>subject</em> is {@code null} or ...
     * @throws NullPointerException          if the specified <em>element</em> is {@code null} and the <em>subject</em>
     *                                       does not permit {@code null} elements.
     * @throws ClassCastException            if the class of the specified <em>element</em> prevents it from being
     *                                       added to the <em>subject</em>
     *                                       (may occur only if used raw or forced in a mismatched class context).
     * @throws IllegalArgumentException      if some property of the <em>element</em> prevents it from being added to
     *                                       the <em>subject</em>.
     * @throws IllegalStateException         if the <em>element</em> cannot be added at this time due to
     *                                       the <em>subject</em>'s insertion restrictions (if any).
     * @see Collection#add(Object)
     * @see Collecting#add(Collection, Object, Object, Object[])
     * @see Collecting#addAll(Collection, Collection)
     * @see Collecting#addAll(Collection, Iterable)
     * @see Collecting#addAll(Collection, Iterator)
     * @see Collecting#addAll(Collection, Stream)
     * @see Collecting#addAll(Collection, Object[])
     */
    public static <E, C extends Collection<E>> C add(final C subject, final E element) {
        subject.add(element);
        return subject;
    }

    /**
     * Similar to {@link Collecting#add(Collection, Object)}, but allows to add two or more elements at once.
     *
     * @throws UnsupportedOperationException if {@link Collection#add(Object)} is not supported by the <em>subject</em>.
     * @throws NullPointerException          if <em>subject</em> is {@code null} or ...
     * @throws NullPointerException          if the {@code array} of <em>more</em> elements is {@code null} or ...
     * @throws NullPointerException          if any of the specified <em>elements</em> is {@code null} and the
     *                                       <em>subject</em> does not permit {@code null} elements.
     * @throws ClassCastException            if the class of the specified <em>elements</em> prevents them from being
     *                                       added to the <em>subject</em>
     *                                       (may occur only if used raw or forced in a mismatched class context).
     * @throws IllegalArgumentException      if some property of some <em>elements</em> prevents them from being
     *                                       added to the <em>subject</em>.
     * @throws IllegalStateException         if the <em>elements</em> cannot be added at this time due to
     *                                       the <em>subject</em>'s insertion restrictions (if any).
     * @see Collection#add(Object)
     * @see Collecting#add(Collection, Object)
     * @see Collecting#addAll(Collection, Collection)
     * @see Collecting#addAll(Collection, Iterable)
     * @see Collecting#addAll(Collection, Iterator)
     * @see Collecting#addAll(Collection, Stream)
     * @see Collecting#addAll(Collection, Object[])
     */
    @SafeVarargs
    public static <E, C extends Collection<E>> C add(final C subject,
                                                     final E element0, final E element1, final E... more) {
        return addAll(subject, Stream.concat(Stream.of(element0, element1), Stream.of(more)));
    }

    /**
     * Just like {@link Collection#addAll(Collection) subject.addAll(elements)}, but returns the <em>subject</em>.
     *
     * @throws UnsupportedOperationException if {@link Collection#addAll(Collection)} is not supported by the
     *                                       <em>subject</em>.
     * @throws NullPointerException          if <em>subject</em> is {@code null} or ...
     * @throws NullPointerException          if the {@link Collection} of <em>elements</em> is {@code null} or ...
     * @throws NullPointerException          if any of the specified <em>elements</em> is {@code null} and the
     *                                       <em>subject</em> does not permit {@code null} elements.
     * @throws ClassCastException            if the class of the <em>elements</em> prevents them from being added to the
     *                                       <em>subject</em>
     *                                       (may occur only if used raw or forced in a mismatched class context).
     * @throws IllegalArgumentException      if some property of some <em>elements</em> prevents them from being added
     *                                       to the <em>subject</em>.
     * @throws IllegalStateException         if the <em>elements</em> cannot be added at this time due to the
     *                                       <em>subject</em>'s insertion restrictions (if any).
     * @see Collection#addAll(Collection)
     * @see Collecting#add(Collection, Object)
     * @see Collecting#add(Collection, Object, Object, Object[])
     * @see Collecting#addAll(Collection, Stream)
     * @see Collecting#addAll(Collection, Iterable)
     * @see Collecting#addAll(Collection, Iterator)
     * @see Collecting#addAll(Collection, Object[])
     */
    public static <E, C extends Collection<E>> C addAll(final C subject, final Collection<? extends E> elements) {
        subject.addAll(elements);
        return subject;
    }

    /**
     * Similar to {@link Collecting#addAll(Collection, Collection)}, but takes a {@link Stream} as second argument.
     *
     * @throws UnsupportedOperationException if {@link Collection#add(Object)} is not supported by the <em>subject</em>.
     * @throws NullPointerException          if <em>subject</em> is {@code null} or ...
     * @throws NullPointerException          if the {@link Stream} of <em>elements</em> is {@code null} or ...
     * @throws NullPointerException          if any of the streamed <em>elements</em> is {@code null} and the
     *                                       <em>subject</em> does not permit {@code null} elements.
     * @throws ClassCastException            if the class of the <em>elements</em> prevents them from being added to the
     *                                       <em>subject</em>
     *                                       (may occur only if used raw or forced in a mismatched class context).
     * @throws IllegalArgumentException      if some property of some <em>elements</em> prevents them from being added
     *                                       to the <em>subject</em>.
     * @throws IllegalStateException         if the <em>elements</em> cannot be added at this time due to the
     *                                       <em>subject</em>'s insertion restrictions (if any).
     * @see Collection#add(Object)
     * @see Collecting#add(Collection, Object)
     * @see Collecting#add(Collection, Object, Object, Object[])
     * @see Collecting#addAll(Collection, Collection)
     * @see Collecting#addAll(Collection, Iterable)
     * @see Collecting#addAll(Collection, Iterator)
     * @see Collecting#addAll(Collection, Object[])
     */
    public static <E, C extends Collection<E>> C addAll(final C subject, final Stream<? extends E> elements) {
        elements.forEach(subject::add);
        return subject;
    }

    /**
     * Similar to {@link Collecting#addAll(Collection, Collection)}, but takes an {@link Iterable} as second argument.
     *
     * @throws UnsupportedOperationException if {@link Collection#add(Object)} or {@link Collection#addAll(Collection)}
     *                                       is not supported by the <em>subject</em>.
     * @throws NullPointerException          if <em>subject</em> is {@code null} or ...
     * @throws NullPointerException          if the {@link Iterable} of <em>elements</em> is {@code null} or ...
     * @throws NullPointerException          if any of the specified <em>elements</em> is {@code null} and the
     *                                       <em>subject</em> does not permit {@code null} elements.
     * @throws ClassCastException            if the class of the <em>elements</em> prevents them from being added to the
     *                                       <em>subject</em>
     *                                       (may occur only if used raw or forced in a mismatched class context).
     * @throws IllegalArgumentException      if some property of some <em>elements</em> prevents them from being added
     *                                       to the <em>subject</em>.
     * @throws IllegalStateException         if the <em>elements</em> cannot be added at this time due to the
     *                                       <em>subject</em>'s insertion restrictions (if any).
     * @see Collection#add(Object)
     * @see Collection#addAll(Collection)
     * @see Collecting#add(Collection, Object)
     * @see Collecting#add(Collection, Object, Object, Object[])
     * @see Collecting#addAll(Collection, Collection)
     * @see Collecting#addAll(Collection, Stream)
     * @see Collecting#addAll(Collection, Iterator)
     * @see Collecting#addAll(Collection, Object[])
     */
    public static <E, C extends Collection<E>> C addAll(final C subject, final Iterable<? extends E> elements) {
        return (elements instanceof final Collection<? extends E> collection)
               ? addAll(subject, collection)
               : addAll(subject, elements.iterator());
    }

    /**
     * Similar to {@link Collecting#addAll(Collection, Collection)}, but takes an {@link Iterator} as second argument.
     *
     * @throws UnsupportedOperationException if {@link Collection#add(Object)} is not supported by the <em>subject</em>.
     * @throws NullPointerException          if <em>subject</em> is {@code null} or ...
     * @throws NullPointerException          if the {@link Iterator} of <em>elements</em> is {@code null} or ...
     * @throws NullPointerException          if any of the iterated <em>elements</em> is {@code null} and the
     *                                       <em>subject</em> does not permit {@code null} elements.
     * @throws ClassCastException            if the class of the <em>elements</em> prevents them from being added to the
     *                                       <em>subject</em>
     *                                       (may occur only if used raw or forced in a mismatched class context).
     * @throws IllegalArgumentException      if some property of some <em>elements</em> prevents them from being added
     *                                       to the <em>subject</em>.
     * @throws IllegalStateException         if the <em>elements</em> cannot be added at this time due to the
     *                                       <em>subject</em>'s insertion restrictions (if any).
     * @see Collection#add(Object)
     * @see Collecting#add(Collection, Object)
     * @see Collecting#add(Collection, Object, Object, Object[])
     * @see Collecting#addAll(Collection, Collection)
     * @see Collecting#addAll(Collection, Stream)
     * @see Collecting#addAll(Collection, Iterable)
     * @see Collecting#addAll(Collection, Object[])
     */
    public static <E, C extends Collection<E>> C addAll(final C subject, final Iterator<? extends E> elements) {
        while (elements.hasNext()) {
            subject.add(elements.next());
        }
        return subject;
    }

    /**
     * Similar to {@link Collecting#addAll(Collection, Collection)}, but takes an {@code array} as second argument.
     *
     * @throws UnsupportedOperationException if {@link Collection#addAll(Collection)} is not supported by the
     *                                       <em>subject</em>.
     * @throws NullPointerException          if <em>subject</em> is {@code null} or ...
     * @throws NullPointerException          if the {@code array} of <em>elements</em> is {@code null} or ...
     * @throws NullPointerException          if any of the specified <em>elements</em> is {@code null} and the
     *                                       <em>subject</em> does not permit {@code null} elements.
     * @throws ClassCastException            if the class of the <em>elements</em> prevents them from being added to the
     *                                       <em>subject</em>
     *                                       (may occur only if used raw or forced in a mismatched class context).
     * @throws IllegalArgumentException      if some property of some <em>elements</em> prevents them from being added
     *                                       to the <em>subject</em>.
     * @throws IllegalStateException         if the <em>elements</em> cannot be added at this time due to the
     *                                       <em>subject</em>'s insertion restrictions (if any).
     * @see Collection#add(Object)
     * @see Collecting#add(Collection, Object)
     * @see Collecting#add(Collection, Object, Object, Object[])
     * @see Collecting#addAll(Collection, Collection)
     * @see Collecting#addAll(Collection, Stream)
     * @see Collecting#addAll(Collection, Iterable)
     * @see Collecting#addAll(Collection, Iterator)
     */
    public static <E, C extends Collection<E>> C addAll(final C subject, final E[] elements) {
        return addAll(subject, asList(elements));
    }

    /**
     * Just like {@link Collection#clear() subject.clear()}, but returns the <em>subject</em>.
     *
     * @throws NullPointerException          if <em>subject</em> is {@code null}.
     * @throws UnsupportedOperationException if {@link Collection#clear()} is not supported by the
     *                                       <em>subject</em>.
     * @see Collection#clear()
     */
    public static <C extends Collection<?>> C clear(final C subject) {
        subject.clear();
        return subject;
    }

    /**
     * Similar to {@link Collection#remove(Object) subject.remove(element)}, but returns the <em>subject</em>.
     * <p>
     * If <em>subject</em> contains the <em>element</em> several times, each occurrence will be removed!
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#remove(Object)} when the <em>subject</em> does not support the requested <em>element</em>.
     *
     * @throws UnsupportedOperationException if {@link Collection#removeAll(Collection)} is not supported by the
     *                                       <em>subject</em>.
     * @throws NullPointerException          if <em>subject</em> is {@code null}.
     * @see Collection#remove(Object)
     * @see Collection#removeAll(Collection)
     * @see Collecting#remove(Collection, Object, Object, Object...)
     * @see Collecting#removeAll(Collection, Collection)
     * @see Collecting#removeAll(Collection, Stream)
     * @see Collecting#removeAll(Collection, Iterable)
     * @see Collecting#removeAll(Collection, Iterator)
     * @see Collecting#removeAll(Collection, Object[])
     */
    public static <C extends Collection<?>> C remove(final C subject, final Object element) {
        try {
            subject.removeAll(Collections.singleton(element));
        } catch (final NullPointerException | ClassCastException caught) {
            if (null == subject) {
                throw caught; // expected to be a NullPointerException
            }
            // --> <subject> can not contain <element>
            // --> <subject> simply does not contain <element>
            // --> Nothing else to do.
        }
        return subject;
    }

    /**
     * Similar to {@link Collecting#remove(Collection, Object)}, but allows to remove two or more elements.
     * <p>
     * If <em>subject</em> contains some of the <em>elements</em> several times, each occurrence will be removed!
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#remove(Object)} or {@link Collection#removeAll(Collection)} when the <em>subject</em> does not
     * support some requested <em>elements</em>.
     *
     * @throws NullPointerException          if <em>subject</em> is {@code null} or if the {@code array} of {@code more}
     *                                       elements is {@code null}.
     * @throws UnsupportedOperationException if {@link Collection#removeAll(Collection)} is not supported by the
     *                                       <em>subject</em>.
     * @see Collection#remove(Object)
     * @see Collection#removeAll(Collection)
     * @see Collecting#remove(Collection, Object)
     * @see Collecting#removeAll(Collection, Collection)
     * @see Collecting#removeAll(Collection, Stream)
     * @see Collecting#removeAll(Collection, Iterable)
     * @see Collecting#removeAll(Collection, Iterator)
     * @see Collecting#removeAll(Collection, Object[])
     */
    public static <C extends Collection<?>> C remove(final C subject,
                                                     final Object element0,
                                                     final Object element1,
                                                     final Object... more) {
        return removeAll(subject, Stream.concat(Stream.of(element0, element1), Stream.of(more)));
    }

    /**
     * Just like {@link Collection#removeAll(Collection) subject.removeAll(elements)}, but returns the <em>subject</em>.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#removeAll(Collection)} when the <em>subject</em> does not support some requested
     * <em>elements</em>.
     *
     * @throws UnsupportedOperationException if {@link Collection#removeAll(Collection)} is not supported by the
     *                                       <em>subject</em>.
     * @throws NullPointerException          if <em>subject</em> is {@code null} or ...
     * @throws NullPointerException          if the {@link Collection} of <em>elements</em> is {@code null}.
     * @see Collection#removeAll(Collection)
     * @see Collecting#remove(Collection, Object)
     * @see Collecting#remove(Collection, Object, Object, Object...)
     * @see Collecting#removeAll(Collection, Stream)
     * @see Collecting#removeAll(Collection, Iterable)
     * @see Collecting#removeAll(Collection, Iterator)
     * @see Collecting#removeAll(Collection, Object[])
     */
    public static <C extends Collection<?>> C removeAll(final C subject, final Collection<?> elements) {
        try {
            subject.removeAll(elements);
        } catch (final NullPointerException | ClassCastException caught) {
            if ((null == subject) || (null == elements)) {
                throw caught; // expected to be a NullPointerException
            } else {
                // --> <subject> or <elements> can not contain an element
                // --> <subject> or <elements> simply does not contain that element
                // --> removal may be incomplete, retry in a more secure way ...
                removeAll(subject, retainAll(new HashSet<>(elements), subject));
            }
        }
        return subject;
    }

    /**
     * Similar to {@link Collecting#removeAll(Collection, Collection)}, but takes a {@link Stream} as second argument.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#removeAll(Collection)} when the <em>subject</em> does not support some requested
     * <em>elements</em>.
     *
     * @throws UnsupportedOperationException if {@link Collection#removeAll(Collection)} is not supported by the
     *                                       <em>subject</em>.
     * @throws NullPointerException          if <em>subject</em> is {@code null} or ...
     * @throws NullPointerException          if the {@link Stream} of <em>elements</em> is {@code null}.
     * @see Collection#removeAll(Collection)
     * @see Collecting#remove(Collection, Object)
     * @see Collecting#remove(Collection, Object, Object, Object...)
     * @see Collecting#removeAll(Collection, Collection)
     * @see Collecting#removeAll(Collection, Iterable)
     * @see Collecting#removeAll(Collection, Iterator)
     * @see Collecting#removeAll(Collection, Object[])
     */
    public static <C extends Collection<?>> C removeAll(final C subject, final Stream<?> elements) {
        final Set<?> collected = elements.collect(Collectors.toCollection(HashSet::new));
        return removeAll(subject, collected);
    }

    /**
     * Similar to {@link Collecting#removeAll(Collection, Collection)}, but takes an {@link Iterable} as second
     * argument.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#removeAll(Collection)} when the <em>subject</em> does not support some requested
     * <em>elements</em>.
     *
     * @throws UnsupportedOperationException if {@link Collection#removeAll(Collection)} is not supported by the
     *                                       <em>subject</em>.
     * @throws NullPointerException          if <em>subject</em> is {@code null} or ...
     * @throws NullPointerException          if the {@link Iterable} of <em>elements</em> is {@code null}.
     * @see Collection#removeAll(Collection)
     * @see Collecting#remove(Collection, Object)
     * @see Collecting#remove(Collection, Object, Object, Object...)
     * @see Collecting#removeAll(Collection, Collection)
     * @see Collecting#removeAll(Collection, Stream)
     * @see Collecting#removeAll(Collection, Iterator)
     * @see Collecting#removeAll(Collection, Object[])
     */
    public static <C extends Collection<?>> C removeAll(final C subject, final Iterable<?> elements) {
        return (elements instanceof final Collection<?> collection)
               ? removeAll(subject, collection)
               : removeAll(subject, elements.iterator());
    }

    /**
     * Similar to {@link Collecting#removeAll(Collection, Collection)}, but takes an {@link Iterator} as second
     * argument.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#removeAll(Collection)} when the <em>subject</em> does not support some requested
     * <em>elements</em>.
     *
     * @throws UnsupportedOperationException if {@link Collection#removeAll(Collection)} is not supported by the
     *                                       <em>subject</em>.
     * @throws NullPointerException          if <em>subject</em> is {@code null} or ...
     * @throws NullPointerException          if the {@link Iterator} of <em>elements</em> is {@code null}.
     * @see Collection#removeAll(Collection)
     * @see Collecting#remove(Collection, Object)
     * @see Collecting#remove(Collection, Object, Object, Object...)
     * @see Collecting#removeAll(Collection, Collection)
     * @see Collecting#removeAll(Collection, Iterable)
     * @see Collecting#removeAll(Collection, Stream)
     * @see Collecting#removeAll(Collection, Object[])
     */
    public static <C extends Collection<?>> C removeAll(final C subject, final Iterator<?> elements) {
        return removeAll(subject, addAll(new HashSet<>(0), elements));
    }

    /**
     * Similar to {@link Collecting#removeAll(Collection, Collection)}, but takes an {@code array} as second
     * argument.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#removeAll(Collection)} when the <em>subject</em> does not support some requested
     * <em>elements</em>.
     *
     * @throws UnsupportedOperationException if {@link Collection#removeAll(Collection)} is not supported by the
     *                                       <em>subject</em>.
     * @throws NullPointerException          if <em>subject</em> is {@code null} or ...
     * @throws NullPointerException          if the {@code array} of <em>elements</em> is {@code null}.
     * @see Collection#removeAll(Collection)
     * @see Collecting#remove(Collection, Object)
     * @see Collecting#remove(Collection, Object, Object, Object...)
     * @see Collecting#removeAll(Collection, Collection)
     * @see Collecting#removeAll(Collection, Stream)
     * @see Collecting#removeAll(Collection, Iterable)
     * @see Collecting#removeAll(Collection, Iterator)
     */
    public static <C extends Collection<?>> C removeAll(final C subject, final Object[] elements) {
        return removeAll(subject, asList(elements));
    }

    /**
     * Just like {@link Collection#removeIf(Predicate) subject.removeIf(filter)}, but returns the <em>subject</em>.
     *
     * @throws UnsupportedOperationException if {@link Collection#removeIf(Predicate)} is not supported by the
     *                                       <em>subject</em>.
     * @throws NullPointerException          if <em>subject</em> is {@code null} or ...
     * @throws NullPointerException          if the <em>filter</em> is {@code null}.
     * @see Collection#removeIf(Predicate)
     */
    public static <E, C extends Collection<E>> C removeIf(final C subject, final Predicate<? super E> filter) {
        subject.removeIf(filter);
        return subject;
    }

    /**
     * Similar to {@link Collection#retainAll(Collection) subject.retainAll(List.of(element))},
     * but returns the <em>subject</em>.
     * <p>
     * If <em>subject</em> contains the <em>element</em> several times, each occurrence will be retained.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#retainAll(Collection)} when the <em>subject</em> does not support the requested
     * <em>element</em>.
     *
     * @throws UnsupportedOperationException if {@link Collection#retainAll(Collection)} is not supported by the
     *                                       <em>subject</em>.
     * @throws NullPointerException          if <em>subject</em> is {@code null}.
     * @see Collection#retainAll(Collection)
     * @see Collecting#retain(Collection, Object, Object, Object...)
     * @see Collecting#retainAll(Collection, Collection)
     * @see Collecting#retainAll(Collection, Stream)
     * @see Collecting#retainAll(Collection, Iterable)
     * @see Collecting#retainAll(Collection, Iterator)
     * @see Collecting#retainAll(Collection, Object[])
     */
    public static <C extends Collection<?>> C retain(final C subject, final Object element) {
        return retainAll(subject, Collections.singleton(element));
    }

    /**
     * Similar to {@link Collecting#retain(Collection, Object)}, but allows to retain two or more elements.
     * <p>
     * If <em>subject</em> contains some of the <em>elements</em> several times, each occurrence will be retained.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#retainAll(Collection)} when the <em>subject</em> does not support the requested
     * <em>elements</em>.
     *
     * @throws NullPointerException          if <em>subject</em> is {@code null} or if the {@code array} of
     *                                       {@code more} elements is {@code null}.
     * @throws UnsupportedOperationException if {@link Collection#removeAll(Collection)} is not supported by the
     *                                       <em>subject</em>.
     * @see Collection#retainAll(Collection)
     * @see Collecting#retain(Collection, Object)
     * @see Collecting#retainAll(Collection, Collection)
     * @see Collecting#retainAll(Collection, Stream)
     * @see Collecting#retainAll(Collection, Iterable)
     * @see Collecting#retainAll(Collection, Iterator)
     * @see Collecting#retainAll(Collection, Object[])
     */
    public static <C extends Collection<?>> C retain(final C subject,
                                                     final Object element0,
                                                     final Object element1,
                                                     final Object... more) {
        return retainAll(subject, Stream.concat(Stream.of(element0, element1), Stream.of(more)));
    }

    /**
     * Just like {@link Collection#retainAll(Collection) subject.retainAll(elements)}, but returns the <em>subject</em>.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#retainAll(Collection)} when the <em>subject</em> does not support some requested
     * <em>elements</em>.
     *
     * @throws UnsupportedOperationException if {@link Collection#retainAll(Collection)} is not supported by the
     *                                       <em>subject</em>.
     * @throws NullPointerException          if <em>subject</em> is {@code null} or ...
     * @throws NullPointerException          if the {@link Collection} of <em>elements</em> is {@code null}.
     * @see Collection#retainAll(Collection)
     * @see Collecting#retainAll(Collection, Stream)
     * @see Collecting#retainAll(Collection, Iterable)
     * @see Collecting#retainAll(Collection, Iterator)
     * @see Collecting#retainAll(Collection, Object[])
     */
    public static <C extends Collection<?>> C retainAll(final C subject, final Collection<?> elements) {
        try {
            subject.retainAll(elements);
        } catch (final NullPointerException | ClassCastException caught) {
            if ((null == subject) || (null == elements)) {
                throw caught; // expected to be a NullPointerException

            } else {
                // --> <subject> or <elements> can not contain an element
                // --> <subject> or <elements> simply does not contain that element
                // --> removal may be incomplete, retry in a more secure way ...
                retainAll(subject, retainAll(new HashSet<>(elements), subject));
            }
        }
        return subject;
    }

    /**
     * Similar to {@link Collecting#retainAll(Collection, Collection)}, but takes a {@link Stream} as second argument.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#retainAll(Collection)} when the <em>subject</em> does not support some requested
     * <em>elements</em>.
     *
     * @throws UnsupportedOperationException if {@link Collection#retainAll(Collection)} is not supported by the
     *                                       <em>subject</em>.
     * @throws NullPointerException          if <em>subject</em> is {@code null} or ...
     * @throws NullPointerException          if the {@link Stream} of <em>elements</em> is {@code null}.
     * @see Collection#retainAll(Collection)
     * @see Collecting#retainAll(Collection, Collection)
     * @see Collecting#retainAll(Collection, Iterable)
     * @see Collecting#retainAll(Collection, Iterator)
     * @see Collecting#retainAll(Collection, Object[])
     */
    public static <C extends Collection<?>> C retainAll(final C subject, final Stream<?> elements) {
        final Set<?> collected = elements.collect(Collectors.toCollection(HashSet::new));
        return retainAll(subject, collected);
    }

    /**
     * Similar to {@link Collecting#retainAll(Collection, Collection)},
     * but takes an {@link Iterable} as second argument.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#retainAll(Collection)} when the <em>subject</em> does not support some requested
     * <em>elements</em>.
     *
     * @throws UnsupportedOperationException if {@link Collection#retainAll(Collection)} is not supported by the
     *                                       <em>subject</em>.
     * @throws NullPointerException          if <em>subject</em> is {@code null} or ...
     * @throws NullPointerException          if the {@link Iterable} of <em>elements</em> is {@code null}.
     * @see Collection#retainAll(Collection)
     * @see Collecting#retainAll(Collection, Collection)
     * @see Collecting#retainAll(Collection, Stream)
     * @see Collecting#retainAll(Collection, Iterator)
     * @see Collecting#retainAll(Collection, Object[])
     */
    public static <C extends Collection<?>> C retainAll(final C subject, final Iterable<?> elements) {
        return (elements instanceof final Collection<?> collection)
               ? retainAll(subject, collection)
               : retainAll(subject, elements.iterator());
    }

    /**
     * Similar to {@link Collecting#retainAll(Collection, Collection)},
     * but takes an {@link Iterator} as second argument.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#retainAll(Collection)} when the <em>subject</em> does not support some requested
     * <em>elements</em>.
     *
     * @throws UnsupportedOperationException if {@link Collection#retainAll(Collection)} is not supported by the
     *                                       <em>subject</em>.
     * @throws NullPointerException          if <em>subject</em> is {@code null} or ...
     * @throws NullPointerException          if the {@link Iterator} of <em>elements</em> is {@code null}.
     * @see Collection#retainAll(Collection)
     * @see Collecting#retainAll(Collection, Collection)
     * @see Collecting#retainAll(Collection, Stream)
     * @see Collecting#retainAll(Collection, Iterable)
     * @see Collecting#retainAll(Collection, Object[])
     */
    public static <C extends Collection<?>> C retainAll(final C subject, final Iterator<?> elements) {
        return retainAll(subject, addAll(new HashSet<>(0), elements));
    }

    /**
     * Similar to {@link Collecting#retainAll(Collection, Collection)}, but takes an {@code array} as second
     * argument.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#retainAll(Collection)} when the <em>subject</em> does not support some requested
     * <em>elements</em>.
     *
     * @throws UnsupportedOperationException if {@link Collection#retainAll(Collection)} is not supported by the
     *                                       <em>subject</em>.
     * @throws NullPointerException          if <em>subject</em> is {@code null} or ...
     * @throws NullPointerException          if the {@code array} of <em>elements</em> is {@code null}.
     * @see Collection#retainAll(Collection)
     * @see Collecting#retainAll(Collection, Collection)
     * @see Collecting#retainAll(Collection, Stream)
     * @see Collecting#retainAll(Collection, Iterable)
     * @see Collecting#retainAll(Collection, Iterator)
     */
    public static <C extends Collection<?>> C retainAll(final C subject, final Object[] elements) {
        return retainAll(subject, asList(elements));
    }

    /**
     * Just like {@link Collection#contains(Object) subject.contains(element)}.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#contains(Object)} when the <em>subject</em> does not support the requested <em>element</em>.
     *
     * @throws NullPointerException if <em>subject</em> is {@code null}.
     * @see Collection#contains(Object)
     * @see Collection#containsAll(Collection)
     * @see Collecting#contains(Collection, Object, Object, Object...)
     * @see Collecting#containsAll(Collection, Collection)
     * @see Collecting#containsAll(Collection, Stream)
     * @see Collecting#containsAll(Collection, Iterable)
     * @see Collecting#containsAll(Collection, Iterator)
     * @see Collecting#containsAll(Collection, Object[])
     */
    public static boolean contains(final Collection<?> subject, final Object element) {
        try {
            return subject.contains(element);
        } catch (final NullPointerException | ClassCastException caught) {
            if (null == subject) {
                throw caught; // expected to be a NullPointerException
            } else {
                // --> <subject> can not contain <element>
                // --> <subject> simply does not contain <element> ...
                return false;
            }
        }
    }

    /**
     * Similar to {@link Collecting#contains(Collection, Object)}, but allows to test two or more elements.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#containsAll(Collection)} when the <em>subject</em> does not support some requested
     * <em>elements</em>.
     *
     * @throws NullPointerException if <em>subject</em> is {@code null} or ...
     * @throws NullPointerException if the {@code array} of {@code more} elements is {@code null}.
     * @see Collection#contains(Object)
     * @see Collection#containsAll(Collection)
     * @see Collecting#contains(Collection, Object)
     * @see Collecting#containsAll(Collection, Collection)
     * @see Collecting#containsAll(Collection, Stream)
     * @see Collecting#containsAll(Collection, Iterable)
     * @see Collecting#containsAll(Collection, Iterator)
     * @see Collecting#containsAll(Collection, Object[])
     */
    public static boolean contains(final Collection<?> subject,
                                   final Object element0, final Object element1, final Object... more) {
        return containsAll(subject, Stream.concat(Stream.of(element0, element1), Stream.of(more)));
    }

    /**
     * Just like {@link Collection#containsAll(Collection) subject.containsAll(elements)},
     * but returns the <em>subject</em>.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#containsAll(Collection)} when the <em>subject</em> does not support some requested
     * <em>elements</em>.
     *
     * @throws NullPointerException if <em>subject</em> is {@code null} or ...
     * @throws NullPointerException if the {@link Collection} of <em>elements</em> is {@code null}.
     * @see Collection#contains(Object)
     * @see Collection#containsAll(Collection)
     * @see Collecting#contains(Collection, Object)
     * @see Collecting#contains(Collection, Object, Object, Object...)
     * @see Collecting#containsAll(Collection, Stream)
     * @see Collecting#containsAll(Collection, Iterable)
     * @see Collecting#containsAll(Collection, Iterator)
     * @see Collecting#containsAll(Collection, Object[])
     */
    public static boolean containsAll(final Collection<?> subject, final Collection<?> elements) {
        try {
            return subject.containsAll(elements);
        } catch (final NullPointerException | ClassCastException caught) {
            if ((null == subject) || (null == elements)) {
                // <caught> is expected to be a NullPointerException ...
                throw caught;
            } else {
                // --> <subject> can not contain all <elements>
                // --> <subject> simply does not contain all <elements> ...
                return false;
            }
        }
    }

    /**
     * Similar to {@link Collecting#containsAll(Collection, Collection)}, but takes a {@link Stream} as second argument.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#containsAll(Collection)} when the <em>subject</em> does not support some requested
     * <em>elements</em>.
     *
     * @throws NullPointerException if <em>subject</em> is {@code null} or ...
     * @throws NullPointerException if the {@link Collection} of <em>elements</em> is {@code null}.
     * @see Collection#contains(Object)
     * @see Collection#containsAll(Collection)
     * @see Collecting#contains(Collection, Object)
     * @see Collecting#contains(Collection, Object, Object, Object...)
     * @see Collecting#containsAll(Collection, Collection)
     * @see Collecting#containsAll(Collection, Iterable)
     * @see Collecting#containsAll(Collection, Iterator)
     * @see Collecting#containsAll(Collection, Object[])
     */
    public static boolean containsAll(final Collection<?> subject, final Stream<?> elements) {
        final Set<?> collected = elements.collect(Collectors.toCollection(HashSet::new));
        return containsAll(subject, collected);
    }

    /**
     * Similar to {@link Collecting#containsAll(Collection, Collection)},
     * but takes an {@link Iterable} as second argument.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#containsAll(Collection)} when the <em>subject</em> does not support some requested
     * <em>elements</em>.
     *
     * @throws NullPointerException if <em>subject</em> is {@code null} or ...
     * @throws NullPointerException if the {@link Collection} of <em>elements</em> is {@code null}.
     * @see Collection#contains(Object)
     * @see Collection#containsAll(Collection)
     * @see Collecting#contains(Collection, Object)
     * @see Collecting#contains(Collection, Object, Object, Object...)
     * @see Collecting#containsAll(Collection, Collection)
     * @see Collecting#containsAll(Collection, Stream)
     * @see Collecting#containsAll(Collection, Iterator)
     * @see Collecting#containsAll(Collection, Object[])
     */
    public static boolean containsAll(final Collection<?> subject, final Iterable<?> elements) {
        return (elements instanceof final Collection<?> collection)
               ? containsAll(subject, collection)
               : containsAll(subject, elements.iterator());
    }

    /**
     * Similar to {@link Collecting#containsAll(Collection, Collection)},
     * but takes an {@link Iterator} as second argument.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#containsAll(Collection)} when the <em>subject</em> does not support some requested
     * <em>elements</em>.
     *
     * @throws NullPointerException if <em>subject</em> is {@code null} or ...
     * @throws NullPointerException if the {@link Collection} of <em>elements</em> is {@code null}.
     * @see Collection#contains(Object)
     * @see Collection#containsAll(Collection)
     * @see Collecting#contains(Collection, Object)
     * @see Collecting#contains(Collection, Object, Object, Object...)
     * @see Collecting#containsAll(Collection, Collection)
     * @see Collecting#containsAll(Collection, Stream)
     * @see Collecting#containsAll(Collection, Iterable)
     * @see Collecting#containsAll(Collection, Object[])
     */
    public static boolean containsAll(final Collection<?> subject, final Iterator<?> elements) {
        return containsAll(subject, addAll(new HashSet<>(0), elements));
    }

    /**
     * Similar to {@link Collecting#containsAll(Collection, Collection)}, but takes an {@code array} as second argument.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#containsAll(Collection)} when the <em>subject</em> does not support some requested
     * <em>elements</em>.
     *
     * @throws NullPointerException if <em>subject</em> is {@code null} or ...
     * @throws NullPointerException if the {@link Collection} of <em>elements</em> is {@code null}.
     * @see Collection#contains(Object)
     * @see Collection#containsAll(Collection)
     * @see Collecting#contains(Collection, Object)
     * @see Collecting#contains(Collection, Object, Object, Object...)
     * @see Collecting#containsAll(Collection, Collection)
     * @see Collecting#containsAll(Collection, Stream)
     * @see Collecting#containsAll(Collection, Iterable)
     * @see Collecting#containsAll(Collection, Iterator)
     */
    public static boolean containsAll(final Collection<?> subject, final Object[] elements) {
        return containsAll(subject, asList(elements));
    }

    /**
     * Supplies a proxy for a given <em>subject</em> that may be used to implement some {@link Collection}-specific
     * methods, e.g.:
     * <ul>
     * <li>{@link Collection#toArray()}</li>
     * <li>{@link Collection#toArray(Object[])}</li>
     * <li>{@link Object#toString()}</li>
     * <li>...</li>
     * </ul>
     *
     * @param subject A {@link Collection}, that at least provides independently ...
     *                <ul>
     *                <li>{@link Collection#iterator()}</li>
     *                <li>{@link Collection#size()}</li>
     *                </ul>
     */
    public static <E> Collection<E> proxy(final Collection<E> subject) {
        // noinspection AnonymousInnerClass
        return new AbstractCollection<>() {
            @Override
            public Iterator<E> iterator() {
                return subject.iterator();
            }

            @Override
            public int size() {
                return subject.size();
            }
        };
    }

    /**
     * Supplies a proxy for a given {@link List subject} that may be used to implement some {@link List}-specific
     * methods, e.g.:
     * <ul>
     * <li>{@link List#toArray()}</li>
     * <li>{@link List#toArray(Object[])}</li>
     * <li>{@link Object#toString()}</li>
     * <li>{@link List#equals(Object)}</li>
     * <li>{@link List#hashCode()}</li>
     * <li>...</li>
     * </ul>
     *
     * @param subject A {@link List}, that at least provides independently ...
     *                <ul>
     *                <li>{@link List#get(int)}</li>
     *                <li>{@link List#size()}</li>
     *                </ul>
     */
    @SuppressWarnings("BoundedWildcard")
    public static <E> List<E> proxy(final List<E> subject) {
        // noinspection AnonymousInnerClass
        return new AbstractList<>() {
            @Override
            public E get(final int index) {
                return subject.get(index);
            }

            @Override
            public int size() {
                return subject.size();
            }
        };
    }

    /**
     * Supplies a proxy for a given {@link Set subject} that may be used to implement some {@link Set}-specific
     * methods, e.g.:
     * <ul>
     * <li>{@link Set#toArray()}</li>
     * <li>{@link Set#toArray(Object[])}</li>
     * <li>{@link Object#toString()}</li>
     * <li>{@link Set#equals(Object)}</li>
     * <li>{@link Set#hashCode()}</li>
     * <li>...</li>
     * </ul>
     *
     * @param subject A {@link Set}, that at least provides independently ...
     *                <ul>
     *                <li>{@link Set#iterator()}</li>
     *                <li>{@link Set#size()}</li>
     *                </ul>
     */
    public static <E> Set<E> proxy(final Set<E> subject) {
        // noinspection AnonymousInnerClass
        return new AbstractSet<>() {
            @Override
            public Iterator<E> iterator() {
                return subject.iterator();
            }

            @Override
            public int size() {
                return subject.size();
            }
        };
    }

    @SuppressWarnings("MethodOnlyUsedFromInnerClass")
    private static <E> Collection<E> nullAsEmpty(final Collection<E> nullable) {
        return (null == nullable) ? Collections.emptySet() : nullable;
    }

    @SuppressWarnings("MethodOnlyUsedFromInnerClass")
    private static <E> Stream<E> nullAsEmpty(final Stream<E> nullable) {
        return (null == nullable) ? Stream.empty() : nullable;
    }

    @SuppressWarnings("MethodOnlyUsedFromInnerClass")
    private static <E> Iterable<E> nullAsEmpty(final Iterable<E> nullable) {
        return (null == nullable) ? Collections.emptySet() : nullable;
    }

    @SuppressWarnings("MethodOnlyUsedFromInnerClass")
    private static <E> Iterator<E> nullAsEmpty(final Iterator<E> nullable) {
        return (null == nullable) ? Collections.emptyIterator() : nullable;
    }

    @SuppressWarnings({"unchecked", "MethodOnlyUsedFromInnerClass", "SuspiciousArrayCast"})
    private static <E> E[] nullAsEmpty(final E[] nullable) {
        return (null == nullable) ? (E[]) EMPTY_ARRAY : nullable;
    }

    /**
     * Returns a new {@link Builder} for target instances as supplied by the given {@link Supplier}.
     *
     * @param <E> The element type.
     * @param <C> The final type of the target instances, at least {@link Collection}.
     */
    public static <E, C extends Collection<E>> Builder<E, C> builder(final Supplier<? extends C> newTarget) {
        return new Builder<>(newTarget, Builder.class);
    }

    /**
     * Returns a new {@link Charger} for a given target instance.
     *
     * @param <E> The element type.
     * @param <C> The final type of the target instances, at least {@link Collection}.
     */
    public static <E, C extends Collection<E>> Charger<E, C> charger(final C target) {
        return new Charger<>(target, Charger.class);
    }

    /**
     * Utility interface to set up a target instance of {@link Collection}.
     *
     * @param <E> The element type.
     * @param <C> The final type of the target instance, at least {@link Collection}.
     * @param <S> The final type of the Setup implementation.
     */
    @SuppressWarnings({"ClassNameSameAsAncestorName", "ClassWithTooManyMethods"})
    @FunctionalInterface
    public interface Setup<E, C extends Collection<E>, S extends Setup<E, C, S>>
            extends de.team33.patterns.building.elara.Setup<C, S> {

        /**
         * Adds an <em>element</em> to the instance to be set up.
         *
         * @throws UnsupportedOperationException if {@link Collection#add(Object)} is not supported by the instance
         *                                       to be set up.
         * @throws NullPointerException          if the specified <em>element</em> is {@code null} and the instance
         *                                       to be set up does not permit {@code null} elements.
         * @throws ClassCastException            if the class of the specified <em>element</em> prevents it from being
         *                                       added to the instance to be set up
         *                                       (may occur only if used raw or forced in a mismatched class context).
         * @throws IllegalArgumentException      if some property of the <em>element</em> prevents it from being added
         *                                       to the instance to be set up.
         * @throws IllegalStateException         if the <em>element</em> cannot be added at this time due to
         *                                       insertion restrictions on the instance to be set up (if any).
         * @see Collection#add(Object)
         * @see Collecting#add(Collection, Object)
         */
        default S add(final E element) {
            return setup(target -> Collecting.add(target, element));
        }

        /**
         * Adds two or more <em>elements</em> to the instance to be set up.
         * <p>
         * If the {@code array} of <em>more</em> elements is {@code null} it will be treated as an empty {@code array}.
         *
         * @throws UnsupportedOperationException if {@link Collection#add(Object)} is not supported by the instance
         *                                       to be set up.
         * @throws NullPointerException          if any of the specified <em>elements</em> is {@code null} and the
         *                                       instance to be set up does not permit {@code null} elements.
         * @throws ClassCastException            if the class of any specified <em>elements</em>
         *                                       prevents them from being added to the instance to be set up
         *                                       (may occur only if used raw or forced in a mismatched class context).
         * @throws IllegalArgumentException      if some property of any <em>elements</em> prevents
         *                                       them from being added to the instance to be set up.
         * @throws IllegalStateException         if any of the <em>elements</em> cannot be added at this time
         *                                       due to insertion restrictions of the instance to be set up (if any).
         * @see #add(Object)
         * @see Collecting#add(Collection, Object, Object, Object[])
         */
        @SuppressWarnings("unchecked")
        default S add(final E element0, final E element1, final E... more) {
            return setup(target -> Collecting.add(target, element0, element1, nullAsEmpty(more)));
        }

        /**
         * Adds multiple <em>elements</em> to the instance to be set up.
         * <p>
         * If the {@link Collection} of <em>elements</em> is {@code null} it will be treated as an empty
         * {@link Collection}.
         *
         * @throws UnsupportedOperationException if {@link Collection#addAll(Collection)} is not supported by the
         *                                       instance to be set up.
         * @throws NullPointerException          if any of the specified <em>elements</em> is {@code null} and the
         *                                       instance to be set up does not permit {@code null} elements.
         * @throws ClassCastException            if the class of any specified <em>elements</em>
         *                                       prevents them from being added to the instance to be set up
         *                                       (may occur only if used raw or forced in a mismatched class context).
         * @throws IllegalArgumentException      if some property of any <em>elements</em> prevents
         *                                       them from being added to the instance to be set up.
         * @throws IllegalStateException         if any of the <em>elements</em> cannot be added at this time
         *                                       due to insertion restrictions of the instance to be set up (if any).
         * @see Collection#addAll(Collection)
         * @see Collecting#addAll(Collection, Collection)
         * @see #addAll(Stream)
         * @see #addAll(Iterable)
         * @see #addAll(Iterator)
         * @see #addAll(Object[])
         */
        default S addAll(final Collection<? extends E> elements) {
            return setup(target -> Collecting.addAll(target, nullAsEmpty(elements)));
        }

        /**
         * Adds multiple <em>elements</em> to the instance to be set up.
         * <p>
         * If the {@link Stream} of <em>elements</em> is {@code null} it will be treated as an empty {@link Stream}.
         *
         * @throws UnsupportedOperationException if {@link Collection#add(Object)} is not supported by the
         *                                       instance to be set up.
         * @throws NullPointerException          if any of the specified <em>elements</em> is {@code null} and the
         *                                       instance to be set up does not permit {@code null} elements.
         * @throws ClassCastException            if the class of any specified <em>elements</em>
         *                                       prevents them from being added to the instance to be set up
         *                                       (may occur only if used raw or forced in a mismatched class context).
         * @throws IllegalArgumentException      if some property of any <em>elements</em> prevents
         *                                       them from being added to the instance to be set up.
         * @throws IllegalStateException         if any of the <em>elements</em> cannot be added at this time
         *                                       due to insertion restrictions of the instance to be set up (if any).
         * @see Collecting#addAll(Collection, Stream)
         * @see #addAll(Collection)
         * @see #addAll(Iterable)
         * @see #addAll(Iterator)
         * @see #addAll(Object[])
         */
        default S addAll(final Stream<? extends E> elements) {
            return setup(target -> Collecting.addAll(target, nullAsEmpty(elements)));
        }

        /**
         * Adds multiple <em>elements</em> to the instance to be set up.
         * <p>
         * Treats a {@code null}-argument just like an empty {@link Iterable}.
         *
         * @throws UnsupportedOperationException if {@link Collection#add(Object)} or
         *                                       if {@link Collection#addAll(Collection)} is not supported by the
         *                                       instance to be set up.
         * @throws NullPointerException          if any of the specified <em>elements</em> is {@code null} and the
         *                                       instance to be set up does not permit {@code null} elements.
         * @throws ClassCastException            if the class of any specified <em>elements</em>
         *                                       prevents them from being added to the instance to be set up
         *                                       (may occur only if used raw or forced in a mismatched class context).
         * @throws IllegalArgumentException      if some property of any <em>elements</em> prevents
         *                                       them from being added to the instance to be set up.
         * @throws IllegalStateException         if any of the <em>elements</em> cannot be added at this time
         *                                       due to insertion restrictions of the instance to be set up (if any).
         * @see Collecting#addAll(Collection, Iterable)
         * @see #addAll(Collection)
         * @see #addAll(Stream)
         * @see #addAll(Iterator)
         * @see #addAll(Object[])
         */
        default S addAll(final Iterable<? extends E> elements) {
            return setup(target -> Collecting.addAll(target, nullAsEmpty(elements)));
        }

        /**
         * Adds multiple <em>elements</em> to the instance to be set up.
         * <p>
         * Treats a {@code null}-argument just like an empty {@link Iterator}.
         *
         * @throws UnsupportedOperationException if {@link Collection#add(Object)} or
         *                                       if {@link Collection#addAll(Collection)} is not supported by the
         *                                       instance to be set up.
         * @throws NullPointerException          if any of the specified <em>elements</em> is {@code null} and the
         *                                       instance to be set up does not permit {@code null} elements.
         * @throws ClassCastException            if the class of any specified <em>elements</em>
         *                                       prevents them from being added to the instance to be set up
         *                                       (may occur only if used raw or forced in a mismatched class context).
         * @throws IllegalArgumentException      if some property of any <em>elements</em> prevents
         *                                       them from being added to the instance to be set up.
         * @throws IllegalStateException         if any of the <em>elements</em> cannot be added at this time
         *                                       due to insertion restrictions of the instance to be set up (if any).
         * @see Collecting#addAll(Collection, Iterable)
         * @see #addAll(Collection)
         * @see #addAll(Stream)
         * @see #addAll(Iterable)
         * @see #addAll(Object[])
         */
        default S addAll(final Iterator<? extends E> elements) {
            return setup(target -> Collecting.addAll(target, nullAsEmpty(elements)));
        }

        /**
         * Adds multiple <em>elements</em> to the instance to be set up.
         * <p>
         * Treats a {@code null}-argument just like an empty {@code array}.
         *
         * @throws UnsupportedOperationException if {@link Collection#add(Object)} or
         *                                       if {@link Collection#addAll(Collection)} is not supported by the
         *                                       instance to be set up.
         * @throws NullPointerException          if any of the specified <em>elements</em> is {@code null} and the
         *                                       instance to be set up does not permit {@code null} elements.
         * @throws ClassCastException            if the class of any specified <em>elements</em>
         *                                       prevents them from being added to the instance to be set up
         *                                       (may occur only if used raw or forced in a mismatched class context).
         * @throws IllegalArgumentException      if some property of any <em>elements</em> prevents
         *                                       them from being added to the instance to be set up.
         * @throws IllegalStateException         if any of the <em>elements</em> cannot be added at this time
         *                                       due to insertion restrictions of the instance to be set up (if any).
         * @see Collecting#addAll(Collection, Iterable)
         * @see #addAll(Collection)
         * @see #addAll(Stream)
         * @see #addAll(Iterable)
         * @see #addAll(Iterator)
         */
        default S addAll(final E[] elements) {
            return setup(target -> Collecting.addAll(target, nullAsEmpty(elements)));
        }

        /**
         * Removes an <em>element</em> from the instance to be set up.
         * <p>
         * If the instance to be set up contains the <em>element</em> several times, each occurrence will be removed!
         * <p>
         * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
         * {@link Collection#remove(Object)} or {@link Collection#removeAll(Collection)} when the instance to be set up
         * does not support the requested <em>element</em>.
         *
         * @throws UnsupportedOperationException if {@link Collection#remove(Object)} or
         *                                       if {@link Collection#removeAll(Collection)} is not supported by the
         *                                       instance to be set up.
         * @see Collection#remove(Object)
         * @see Collecting#remove(Collection, Object)
         */
        default S remove(final Object element) {
            return setup(target -> Collecting.remove(target, element));
        }

        /**
         * Removes two or more <em>elements</em> from the instance to be set up.
         * <p>
         * If the instance to be set up contains an <em>element</em> several times, each occurrence will be removed!
         *
         * @throws UnsupportedOperationException if {@link Collection#remove(Object)} or
         *                                       if {@link Collection#removeAll(Collection)} is not supported by the
         *                                       instance to be set up.
         * @see #remove(Object)
         * @see Collecting#remove(Collection, Object, Object, Object[])
         */
        default S remove(final Object element0, final Object element1, final Object... more) {
            return setup(target -> Collecting.remove(target, element0, element1, nullAsEmpty(more)));
        }

        /**
         * Removes multiple <em>elements</em> from the instance to be set up.
         * <p>
         * If the instance to be set up contains an <em>element</em> several times, each occurrence will be removed!
         * <p>
         * Treats a {@code null}-argument just like an empty {@link Collection}.
         * <p>
         * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
         * {@link Collection#remove(Object)} or {@link Collection#removeAll(Collection)} when the instance to be set up
         * does not support the requested <em>elements</em>.
         *
         * @throws UnsupportedOperationException if {@link Collection#remove(Object)} or
         *                                       if {@link Collection#removeAll(Collection)} is not supported by the
         *                                       instance to be set up.
         * @see Collection#removeAll(Collection)
         * @see Collecting#removeAll(Collection, Collection)
         * @see #removeAll(Stream)
         * @see #removeAll(Iterable)
         * @see #removeAll(Iterator)
         * @see #removeAll(Object[])
         */
        default S removeAll(final Collection<?> elements) {
            return setup(target -> Collecting.removeAll(target, nullAsEmpty(elements)));
        }

        /**
         * Removes multiple <em>elements</em> from the instance to be set up.
         * <p>
         * If the instance to be set up contains an <em>element</em> several times, each occurrence will be removed!
         * <p>
         * Treats a {@code null}-argument just like an empty {@link Stream}.
         *
         * @throws UnsupportedOperationException if {@link Collection#remove(Object)} or
         *                                       if {@link Collection#removeAll(Collection)} is not supported by the
         *                                       instance to be set up.
         * @see Collecting#removeAll(Collection, Stream)
         * @see #removeAll(Collection)
         * @see #removeAll(Iterable)
         * @see #removeAll(Iterator)
         * @see #removeAll(Object[])
         */
        default S removeAll(final Stream<?> elements) {
            return setup(target -> Collecting.removeAll(target, nullAsEmpty(elements)));
        }

        /**
         * Removes multiple <em>elements</em> from the instance to be set up.
         * <p>
         * If the instance to be set up contains an <em>element</em> several times, each occurrence will be removed!
         * <p>
         * Treats a {@code null}-argument just like an empty {@link Iterable}.
         *
         * @throws UnsupportedOperationException if {@link Collection#remove(Object)} or
         *                                       if {@link Collection#removeAll(Collection)} is not supported by the
         *                                       instance to be set up.
         * @see Collecting#removeAll(Collection, Iterable)
         * @see #removeAll(Collection)
         * @see #removeAll(Stream)
         * @see #removeAll(Iterator)
         * @see #removeAll(Object[])
         */
        default S removeAll(final Iterable<?> elements) {
            return setup(target -> Collecting.removeAll(target, nullAsEmpty(elements)));
        }

        /**
         * Removes multiple <em>elements</em> from the instance to be set up.
         * <p>
         * If the instance to be set up contains an <em>element</em> several times, each occurrence will be removed!
         * <p>
         * Treats a {@code null}-argument just like an empty {@link Iterator}.
         *
         * @throws UnsupportedOperationException if {@link Collection#remove(Object)} or
         *                                       if {@link Collection#removeAll(Collection)} is not supported by the
         *                                       instance to be set up.
         * @see Collecting#removeAll(Collection, Iterable)
         * @see #removeAll(Collection)
         * @see #removeAll(Stream)
         * @see #removeAll(Iterable)
         * @see #removeAll(Object[])
         */
        default S removeAll(final Iterator<?> elements) {
            return setup(target -> Collecting.removeAll(target, nullAsEmpty(elements)));
        }

        /**
         * Removes multiple <em>elements</em> from the instance to be set up.
         * <p>
         * If the instance to be set up contains an <em>element</em> several times, each occurrence will be removed!
         * <p>
         * Treats a {@code null}-argument just like an empty {@code array}.
         *
         * @throws UnsupportedOperationException if {@link Collection#remove(Object)} or
         *                                       if {@link Collection#removeAll(Collection)} is not supported by the
         *                                       instance to be set up.
         * @see Collecting#removeAll(Collection, Object[])
         * @see #removeAll(Collection)
         * @see #removeAll(Stream)
         * @see #removeAll(Iterable)
         * @see #removeAll(Iterator)
         */
        default S removeAll(final Object[] elements) {
            return setup(target -> Collecting.removeAll(target, nullAsEmpty(elements)));
        }

        /**
         * Removes multiple <em>elements</em> from the instance to be set up.
         *
         * @throws UnsupportedOperationException if {@link Collection#removeIf(Predicate)} is not supported by the
         *                                       instance to be set up.
         * @throws NullPointerException          if the <em>filter</em> is {@code null}.
         * @see Collection#removeIf(Predicate)
         * @see Collecting#removeIf(Collection, Predicate)
         */
        default S removeIf(final Predicate<? super E> filter) {
            return setup(target -> Collecting.removeIf(target, filter));
        }

        /**
         * Retains an <em>element</em> in the instance to be set up.
         * <p>
         * If the instance to be set up contains the <em>element</em> several times, each occurrence will be retained.
         * <p>
         * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
         * {@link Collection#retainAll(Collection)} when the instance to be set up does not support the requested <em>element</em>.
         *
         * @throws UnsupportedOperationException if {@link Collection#retainAll(Collection)} is not supported by the
         *                                       instance to be set up.
         * @see Collection#retainAll(Collection)
         * @see Collecting#retain(Collection, Object)
         */
        default S retain(final Object element) {
            return setup(target -> Collecting.retain(target, element));
        }

        /**
         * Retains two or more <em>elements</em> in the instance to be set up.
         * <p>
         * If the instance to be set up contains an <em>element</em> several times, each occurrence will be retaind.
         *
         * @throws UnsupportedOperationException if {@link Collection#retainAll(Collection)} is not supported by the
         *                                       instance to be set up.
         * @see #retain(Object)
         * @see Collecting#retain(Collection, Object, Object, Object[])
         */
        default S retain(final Object element0, final Object element1, final Object... more) {
            return setup(target -> Collecting.retain(target, element0, element1, nullAsEmpty(more)));
        }

        /**
         * Retains multiple <em>elements</em> by removing all others from the instance to be set up.
         * <p>
         * Treats a {@code null}-argument just like an empty {@link Collection}.
         * <p>
         * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
         * {@link Collection#retainAll(Collection)} when the instance to be set up does not support the requested
         * <em>elements</em>.
         *
         * @throws UnsupportedOperationException if {@link Collection#retainAll(Collection)} is not supported by the
         *                                       instance to be set up.
         * @see Collection#retainAll(Collection)
         * @see Collecting#retainAll(Collection, Collection)
         * @see #retainAll(Stream)
         * @see #retainAll(Iterable)
         * @see #retainAll(Iterator)
         * @see #retainAll(Object[])
         */
        default S retainAll(final Collection<?> elements) {
            return setup(target -> Collecting.retainAll(target, nullAsEmpty(elements)));
        }

        /**
         * Retains multiple <em>elements</em> by removing all others from the instance to be set up.
         * <p>
         * Treats a {@code null}-argument just like an empty {@link Stream}.
         *
         * @throws UnsupportedOperationException if {@link Collection#retainAll(Collection)} is not supported by the
         *                                       instance to be set up.
         * @see Collecting#retainAll(Collection, Stream)
         * @see #retainAll(Collection)
         * @see #retainAll(Iterable)
         * @see #retainAll(Iterator)
         * @see #retainAll(Object[])
         */
        default S retainAll(final Stream<?> elements) {
            return setup(target -> Collecting.retainAll(target, nullAsEmpty(elements)));
        }

        /**
         * Retains multiple <em>elements</em> by removing all others from the instance to be set up.
         * <p>
         * Treats a {@code null}-argument just like an empty {@link Iterable}.
         *
         * @throws UnsupportedOperationException if {@link Collection#retainAll(Collection)} is not supported by the
         *                                       instance to be set up.
         * @see Collecting#retainAll(Collection, Iterable)
         * @see #retainAll(Collection)
         * @see #retainAll(Stream)
         * @see #retainAll(Iterator)
         * @see #retainAll(Object[])
         */
        default S retainAll(final Iterable<?> elements) {
            return setup(target -> Collecting.retainAll(target, nullAsEmpty(elements)));
        }

        /**
         * Retains multiple <em>elements</em> by removing all others from the instance to be set up.
         * <p>
         * Treats a {@code null}-argument just like an empty {@link Iterator}.
         *
         * @throws UnsupportedOperationException if {@link Collection#retainAll(Collection)} is not supported by the
         *                                       instance to be set up.
         * @see Collecting#retainAll(Collection, Iterable)
         * @see #retainAll(Collection)
         * @see #retainAll(Stream)
         * @see #retainAll(Iterable)
         * @see #retainAll(Object[])
         */
        default S retainAll(final Iterator<?> elements) {
            return setup(target -> Collecting.retainAll(target, nullAsEmpty(elements)));
        }

        /**
         * Retains multiple <em>elements</em> by removing all others from the instance to be set up.
         * <p>
         * Treats a {@code null}-argument just like an empty {@code array}.
         *
         * @throws UnsupportedOperationException if {@link Collection#retainAll(Collection)} is not supported by the
         *                                       instance to be set up.
         * @see Collecting#retainAll(Collection, Object[])
         * @see #retainAll(Collection)
         * @see #retainAll(Stream)
         * @see #retainAll(Iterable)
         * @see #retainAll(Iterator)
         */
        default S retainAll(final Object[] elements) {
            return setup(target -> Collecting.retainAll(target, nullAsEmpty(elements)));
        }

        /**
         * Removes all <em>elements</em> from the instance to be set up.
         *
         * @throws UnsupportedOperationException if {@link Collection#clear()} is not supported by the instance to be
         *                                       set up.
         * @see Collection#clear()
         * @see Collecting#clear(Collection)
         */
        default S clear() {
            return setup(Collecting::clear);
        }
    }

    /**
     * Builder implementation to build target instances of {@link Collection}.
     * <p>
     * Use {@link #builder(Supplier)} to get an instance.
     *
     * @param <E> The element type.
     * @param <C> The final type of the target instances, at least {@link Collection}.
     */
    public static final class Builder<E, C extends Collection<E>>
            extends LateBuilder<C, Builder<E, C>>
            implements Setup<E, C, Builder<E, C>> {

        @SuppressWarnings({"rawtypes", "unchecked"})
        private Builder(final Supplier<? extends C> newResult, final Class builderClass) {
            super(newResult, builderClass);
        }
    }

    /**
     * Charger implementation to charge target instances of {@link Collection}.
     * <p>
     * Use {@link #charger(Collection)} to get an instance.
     *
     * @param <E> The element type.
     * @param <C> The final type of the target instances, at least {@link Collection}.
     */
    @SuppressWarnings("ClassNameSameAsAncestorName")
    public static final class Charger<E, C extends Collection<E>>
            extends de.team33.patterns.building.elara.Charger<C, Charger<E, C>>
            implements Setup<E, C, Charger<E, C>> {

        @SuppressWarnings({"rawtypes", "unchecked"})
        private Charger(final C target, final Class builderClass) {
            super(target, builderClass);
        }
    }
}
