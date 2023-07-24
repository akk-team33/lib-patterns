package de.team33.patterns.collection.ceres;

import de.team33.patterns.building.elara.LateBuilder;

import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Arrays.asList;

/**
 * {@linkplain Collections Additional} convenience methods to deal with Collections.
 */
@SuppressWarnings({"ProhibitedExceptionCaught", "unused"})
public final class Collecting {

    private Collecting() {
    }

    /**
     * Just like {@link Collection#add(Object) subject.add(element)}, but returns the {@code subject}.
     *
     * @throws UnsupportedOperationException if {@link Collection#add(Object)} is not supported by the {@code subject}.
     * @throws NullPointerException          if {@code subject} is {@code null} or if the specified {@code element}
     *                                       is {@code null} and the {@code subject} does not permit {@code null}
     *                                       elements.
     * @throws ClassCastException            if the class of the specified {@code element} prevents it from being
     *                                       added to the {@code subject}
     *                                       (may occur only if used raw or forced in a mismatched class context).
     * @throws IllegalArgumentException      if some property of the {@code element} prevents it from being added to
     *                                       the {@code subject}.
     * @throws IllegalStateException         if the {@code element} cannot be added at this time due to
     *                                       the {@code subject}'s insertion restrictions (if any).
     * @see Collection#add(Object)
     * @see Collecting#add(Collection, Object, Object, Object[])
     * @see Collecting#addAll(Collection, Collection)
     * @see Collecting#addAll(Collection, Iterable)
     * @see Collecting#addAll(Collection, Stream)
     * @see Collecting#addAll(Collection, Object[])
     */
    @SuppressWarnings("OverloadedVarargsMethod")
    public static <E, C extends Collection<E>> C add(final C subject, final E element) {
        subject.add(element);
        return subject;
    }

    /**
     * Similar to {@link Collecting#add(Collection, Object)}, but allows to add two or more elements.
     *
     * @throws UnsupportedOperationException if {@link Collection#add(Object)} is not supported by the {@code subject}.
     * @throws NullPointerException          if {@code subject} is {@code null}, if the {@code array} of {@code more}
     *                                       elements is {@code null} or if any of the specified {@code elements}
     *                                       is {@code null} and the {@code subject} does not permit {@code null}
     *                                       elements.
     * @throws ClassCastException            if the class of the specified {@code elements} prevents them from being
     *                                       added to the {@code subject}
     *                                       (may occur only if used raw or forced in a mismatched class context).
     * @throws IllegalArgumentException      if some property of some {@code elements} prevents them from being
     *                                       added to the {@code subject}.
     * @throws IllegalStateException         if the {@code elements} cannot be added at this time due to
     *                                       the {@code subject}'s insertion restrictions (if any).
     * @see Collection#add(Object)
     * @see Collecting#add(Collection, Object)
     * @see Collecting#addAll(Collection, Collection)
     * @see Collecting#addAll(Collection, Iterable)
     * @see Collecting#addAll(Collection, Stream)
     * @see Collecting#addAll(Collection, Object[])
     */
    @SuppressWarnings("OverloadedVarargsMethod")
    @SafeVarargs
    public static <E, C extends Collection<E>> C add(final C subject,
                                                     final E element0, final E element1, final E... more) {
        return addAll(subject, Stream.concat(Stream.of(element0, element1), Stream.of(more)));
    }

    /**
     * Just like {@link Collection#addAll(Collection) subject.addAll(elements)}, but returns the {@code subject}.
     *
     * @throws UnsupportedOperationException if {@link Collection#addAll(Collection)} is not supported by the
     *                                       {@code subject}.
     * @throws NullPointerException          if {@code subject} is {@code null}, if the {@link Collection} of
     *                                       {@code elements} is {@code null} or if any of the specified
     *                                       {@code elements} is {@code null} and the {@code subject} does not permit
     *                                       {@code null} elements.
     * @throws ClassCastException            if the class of the {@code elements} prevents them from being added to the
     *                                       {@code subject}
     *                                       (may occur only if used raw or forced in a mismatched class context).
     * @throws IllegalArgumentException      if some property of some {@code elements} prevents them from being added
     *                                       to the {@code subject}.
     * @throws IllegalStateException         if the {@code elements} cannot be added at this time due to the
     *                                       {@code subject}'s insertion restrictions (if any).
     * @see Collection#addAll(Collection)
     * @see Collecting#add(Collection, Object)
     * @see Collecting#add(Collection, Object, Object, Object[])
     * @see Collecting#addAll(Collection, Iterable)
     * @see Collecting#addAll(Collection, Stream)
     * @see Collecting#addAll(Collection, Object[])
     */
    public static <E, C extends Collection<E>> C addAll(final C subject, final Collection<? extends E> elements) {
        subject.addAll(elements);
        return subject;
    }

    /**
     * Similar to {@link Collecting#addAll(Collection, Collection)}, but takes a {@link Stream} as second argument.
     *
     * @throws UnsupportedOperationException if {@link Collection#add(Object)} is not supported by the {@code subject}.
     * @throws NullPointerException          if {@code subject} is {@code null}, if the {@link Stream} of
     *                                       {@code elements} is {@code null} or if any of the streamed
     *                                       {@code elements} is {@code null} and the {@code subject} does not permit
     *                                       {@code null} elements.
     * @throws ClassCastException            if the class of the {@code elements} prevents them from being added to the
     *                                       {@code subject}
     *                                       (may occur only if used raw or forced in a mismatched class context).
     * @throws IllegalArgumentException      if some property of some {@code elements} prevents them from being added
     *                                       to the {@code subject}.
     * @throws IllegalStateException         if the {@code elements} cannot be added at this time due to the
     *                                       {@code subject}'s insertion restrictions (if any).
     * @see Collection#add(Object)
     * @see Collecting#add(Collection, Object)
     * @see Collecting#add(Collection, Object, Object, Object[])
     * @see Collecting#addAll(Collection, Collection)
     * @see Collecting#addAll(Collection, Iterable)
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
     *                                       is not supported by the {@code subject}.
     * @throws NullPointerException          if {@code subject} is {@code null}, if the {@link Iterable} of
     *                                       {@code elements} is {@code null} or if any of the specified
     *                                       {@code elements} is {@code null} and the {@code subject} does not permit
     *                                       {@code null} elements.
     * @throws ClassCastException            if the class of the {@code elements} prevents them from being added to the
     *                                       {@code subject}
     *                                       (may occur only if used raw or forced in a mismatched class context).
     * @throws IllegalArgumentException      if some property of some {@code elements} prevents them from being added
     *                                       to the {@code subject}.
     * @throws IllegalStateException         if the {@code elements} cannot be added at this time due to the
     *                                       {@code subject}'s insertion restrictions (if any).
     * @see Collection#add(Object)
     * @see Collection#addAll(Collection)
     * @see Collecting#add(Collection, Object)
     * @see Collecting#add(Collection, Object, Object, Object[])
     * @see Collecting#addAll(Collection, Collection)
     * @see Collecting#addAll(Collection, Stream)
     * @see Collecting#addAll(Collection, Object[])
     */
    @SuppressWarnings("unchecked")
    public static <E, C extends Collection<E>> C addAll(final C subject, final Iterable<? extends E> elements) {
        return (elements instanceof Collection<?>)
               ? addAll(subject, (Collection<? extends E>) elements)
               : addAll(subject, StreamSupport.stream(elements.spliterator(), false));
    }

    /**
     * Similar to {@link Collecting#addAll(Collection, Collection)}, but takes an {@code array} as second argument.
     *
     * @throws UnsupportedOperationException if {@link Collection#add(Object)} is not supported by the {@code subject}.
     * @throws NullPointerException          if {@code subject} is {@code null}, if the {@code array} of
     *                                       {@code elements} is {@code null} or if any of the specified
     *                                       {@code elements} is {@code null} and the {@code subject} does not permit
     *                                       {@code null} elements.
     * @throws ClassCastException            if the class of the {@code elements} prevents them from being added to the
     *                                       {@code subject}
     *                                       (may occur only if used raw or forced in a mismatched class context).
     * @throws IllegalArgumentException      if some property of some {@code elements} prevents them from being added
     *                                       to the {@code subject}.
     * @throws IllegalStateException         if the {@code elements} cannot be added at this time due to the
     *                                       {@code subject}'s insertion restrictions (if any).
     * @see Collection#add(Object)
     * @see Collecting#add(Collection, Object)
     * @see Collecting#add(Collection, Object, Object, Object[])
     * @see Collecting#addAll(Collection, Collection)
     * @see Collecting#addAll(Collection, Iterable)
     * @see Collecting#addAll(Collection, Stream)
     */
    public static <E, C extends Collection<E>> C addAll(final C subject, final E[] elements) {
        return addAll(subject, Stream.of(elements));
    }

    /**
     * Just like {@link Collection#clear() subject.clear()}, but returns the {@code subject}.
     *
     * @throws NullPointerException          if {@code subject} is {@code null}.
     * @throws UnsupportedOperationException if {@link Collection#clear()} is not supported by the {@code subject}.
     * @see Collection#clear()
     */
    public static <C extends Collection<?>> C clear(final C subject) {
        subject.clear();
        return subject;
    }

    /**
     * Just like {@link Collection#remove(Object) subject.remove(element)}, but returns the {@code subject}.
     * <p>
     * If {@code subject} contains the {@code element} several times, each occurrence will be removed!
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#remove(Object)} when the {@code subject} does not support the requested {@code element}.
     *
     * @return The {@code subject}.
     * @throws NullPointerException          if {@code subject} is {@code null}.
     * @throws UnsupportedOperationException if {@link Collection#removeAll(Collection)} is not supported by the
     *                                       {@code subject}.
     * @see Collection#remove(Object)
     * @see Collection#removeAll(Collection)
     * @see Collecting#remove(Collection, Object, Object, Object...)
     * @see Collecting#removeAll(Collection, Collection)
     * @see Collecting#removeAll(Collection, Stream)
     * @see Collecting#removeAll(Collection, Iterable)
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
     * If {@code subject} contains some of the {@code elements} several times, each occurrence will be removed!
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#remove(Object)} or {@link Collection#removeAll(Collection)} when the {@code subject} does not
     * support some requested {@code elements}.
     *
     * @throws NullPointerException          if {@code subject} is {@code null} or if the {@code array} of {@code more}
     *                                       elements is {@code null}.
     * @throws UnsupportedOperationException if {@link Collection#removeAll(Collection)} is not supported by the
     *                                       {@code subject}.
     * @see Collection#remove(Object)
     * @see Collection#removeAll(Collection)
     * @see Collecting#remove(Collection, Object)
     * @see Collecting#removeAll(Collection, Collection)
     * @see Collecting#removeAll(Collection, Stream)
     * @see Collecting#removeAll(Collection, Iterable)
     * @see Collecting#removeAll(Collection, Object[])
     */
    @SuppressWarnings("OverloadedVarargsMethod")
    public static <C extends Collection<?>> C remove(final C subject,
                                                     final Object element0,
                                                     final Object element1,
                                                     final Object... more) {
        return removeAll(subject, Stream.concat(Stream.of(element0, element1), Stream.of(more)));
    }

    /**
     * Just like {@link Collection#removeAll(Collection) subject.removeAll(elements)}, but returns the {@code subject}.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#removeAll(Collection)} when the {@code subject} does not support some requested
     * {@code elements}.
     *
     * @throws NullPointerException          if {@code subject} is {@code null} or if the {@link Collection} of
     *                                       {@code elements} is {@code null}.
     * @throws UnsupportedOperationException if {@link Collection#removeAll(Collection)} is not supported by the
     *                                       {@code subject}.
     * @see Collection#removeAll(Collection)
     * @see Collecting#remove(Collection, Object)
     * @see Collecting#remove(Collection, Object, Object, Object...)
     * @see Collecting#removeAll(Collection, Stream)
     * @see Collecting#removeAll(Collection, Iterable)
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
     * {@link Collection#removeAll(Collection)} when the {@code subject} does not support some requested
     * {@code elements}.
     *
     * @throws NullPointerException          if {@code subject} is {@code null} or if the {@link Stream} of
     *                                       {@code elements} is {@code null}.
     * @throws UnsupportedOperationException if {@link Collection#removeAll(Collection)} is not supported by the
     *                                       {@code subject}.
     * @see Collection#removeAll(Collection)
     * @see Collecting#remove(Collection, Object)
     * @see Collecting#remove(Collection, Object, Object, Object...)
     * @see Collecting#removeAll(Collection, Collection)
     * @see Collecting#removeAll(Collection, Iterable)
     * @see Collecting#removeAll(Collection, Object[])
     */
    public static <C extends Collection<?>> C removeAll(final C subject, final Stream<?> elements) {
        return removeAll(subject, addAll(new HashSet<>(), elements.filter(subject::contains)));
    }

    /**
     * Similar to {@link Collecting#removeAll(Collection, Collection)}, but takes an {@link Iterable} as second
     * argument.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#removeAll(Collection)} when the {@code subject} does not support some requested
     * {@code elements}.
     *
     * @throws NullPointerException          if {@code subject} is {@code null} or if the {@link Iterable} of
     *                                       {@code elements} is {@code null}.
     * @throws UnsupportedOperationException if {@link Collection#removeAll(Collection)} is not supported by the
     *                                       {@code subject}.
     * @see Collection#removeAll(Collection)
     * @see Collecting#remove(Collection, Object)
     * @see Collecting#remove(Collection, Object, Object, Object...)
     * @see Collecting#removeAll(Collection, Collection)
     * @see Collecting#removeAll(Collection, Stream)
     * @see Collecting#removeAll(Collection, Object[])
     */
    public static <C extends Collection<?>> C removeAll(final C subject, final Iterable<?> elements) {
        return (elements instanceof Collection<?>)
               ? removeAll(subject, (Collection<?>) elements)
               : removeAll(subject, StreamSupport.stream(elements.spliterator(), false));
    }

    /**
     * Similar to {@link Collecting#removeAll(Collection, Collection)}, but takes an {@code array} as second
     * argument.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#removeAll(Collection)} when the {@code subject} does not support some requested
     * {@code elements}.
     *
     * @throws NullPointerException          if {@code subject} is {@code null} or if the {@code array} of
     *                                       {@code elements} is {@code null}.
     * @throws UnsupportedOperationException if {@link Collection#removeAll(Collection)} is not supported by the
     *                                       {@code subject}.
     * @see Collection#removeAll(Collection)
     * @see Collecting#remove(Collection, Object)
     * @see Collecting#remove(Collection, Object, Object, Object...)
     * @see Collecting#removeAll(Collection, Collection)
     * @see Collecting#removeAll(Collection, Stream)
     * @see Collecting#removeAll(Collection, Iterable)
     */
    public static <C extends Collection<?>> C removeAll(final C subject, final Object[] elements) {
        return removeAll(subject, Arrays.asList(elements));
    }

    /**
     * Just like {@link Collection#retainAll(Collection) subject.retainAll(elements)}, but returns the {@code subject}.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#retainAll(Collection)} when the {@code subject} does not support some requested
     * {@code elements}.
     *
     * @return The {@code subject}.
     * @throws UnsupportedOperationException if {@link Collection#retainAll(Collection)} is not supported by the
     *                                       {@code subject}.
     * @throws NullPointerException          if {@code subject} is {@code null} or if the {@link Collection} of
     *                                       {@code elements} is {@code null}.
     * @see Collection#retainAll(Collection)
     * @see Collecting#retainAll(Collection, Stream)
     * @see Collecting#retainAll(Collection, Iterable)
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
     * {@link Collection#retainAll(Collection)} when the {@code subject} does not support some requested
     * {@code elements}.
     *
     * @return The {@code subject}.
     * @throws UnsupportedOperationException if {@link Collection#retainAll(Collection)} is not supported by the
     *                                       {@code subject}.
     * @throws NullPointerException          if {@code subject} is {@code null} or if the {@link Stream} of
     *                                       {@code elements} is {@code null}.
     * @see Collection#retainAll(Collection)
     * @see Collecting#retainAll(Collection, Collection)
     * @see Collecting#retainAll(Collection, Iterable)
     * @see Collecting#retainAll(Collection, Object[])
     */
    public static <C extends Collection<?>> C retainAll(final C subject, final Stream<?> elements) {
        return retainAll(subject, addAll(new HashSet<>(), elements.filter(subject::contains)));
    }

    /**
     * Similar to {@link Collecting#retainAll(Collection, Collection)}, but takes an {@link Iterable} as second argument.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#retainAll(Collection)} when the {@code subject} does not support some requested
     * {@code elements}.
     *
     * @return The {@code subject}.
     * @throws UnsupportedOperationException if {@link Collection#retainAll(Collection)} is not supported by the
     *                                       {@code subject}.
     * @throws NullPointerException          if {@code subject} is {@code null} or if the {@link Iterable} of
     *                                       {@code elements} is {@code null}.
     * @see Collection#retainAll(Collection)
     * @see Collecting#retainAll(Collection, Collection)
     * @see Collecting#retainAll(Collection, Stream)
     * @see Collecting#retainAll(Collection, Object[])
     */
    public static <C extends Collection<?>> C retainAll(final C subject, final Iterable<?> elements) {
        return (elements instanceof Collection<?>)
               ? retainAll(subject, (Collection<?>) elements)
               : retainAll(subject, StreamSupport.stream(elements.spliterator(), false));
    }

    /**
     * Similar to {@link Collecting#retainAll(Collection, Collection)}, but takes an {@code array} as second
     * argument.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#retainAll(Collection)} when the {@code subject} does not support some requested
     * {@code elements}.
     *
     * @throws NullPointerException          if {@code subject} is {@code null} or if the {@code array} of
     *                                       {@code elements} is {@code null}.
     * @throws UnsupportedOperationException if {@link Collection#retainAll(Collection)} is not supported by the
     *                                       {@code subject}.
     * @see Collection#retainAll(Collection)
     * @see Collecting#retainAll(Collection, Collection)
     * @see Collecting#retainAll(Collection, Stream)
     * @see Collecting#retainAll(Collection, Iterable)
     */
    public static <C extends Collection<?>> C retainAll(final C subject, final Object... elements) {
        return retainAll(subject, Arrays.asList(elements));
    }

    /**
     * Just like {@link Collection#contains(Object) subject.contains(element)}.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#contains(Object)} when the {@code subject} does not support the requested {@code element}.
     *
     * @throws NullPointerException if {@code subject} is {@code null}.
     */
    @SuppressWarnings("OverloadedMethodsWithSameNumberOfParameters")
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
     * Similar to {@link Collection#contains(Object)}
     * or rather {@link Collection#containsAll(Collection)} for a given {@code subject}.
     * Allows to test for a variable number of elements.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#containsAll(Collection)} when the {@code subject} does not support some requested
     * {@code elements}.
     *
     * @throws NullPointerException if {@code subject} or the {@code array} of {@code elements} is {@code null}.
     * @see Collection#containsAll(Collection)
     */
    @SuppressWarnings({"OverloadedVarargsMethod", "OverloadedMethodsWithSameNumberOfParameters"})
    public static boolean contains(final Collection<?> subject, final Object... elements) {
        return containsAll(subject, asList(elements));
    }

    /**
     * Indicates if a given {@code subject} contains specific {@code elements}.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#containsAll(Collection)} when the {@code subject} does not support some requested
     * {@code elements}.
     *
     * @throws NullPointerException if {@code subject} or the {@link Collection} of {@code elements} is {@code null}.
     * @see Collection#containsAll(Collection)
     */
    @SuppressWarnings("SuspiciousMethodCalls")
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
     * Supplies a proxy for a given {@code subject} that may be used to implement some {@link Collection}-specific
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
    @SuppressWarnings("ReturnOfInnerClass")
    public static <E> Collection<E> proxy(final Collection<E> subject) {
        //noinspection AnonymousInnerClass,AnonymousInnerClassWithTooManyMethods
        return new AbstractCollection<E>() {
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
    @SuppressWarnings("ReturnOfInnerClass")
    public static <E> List<E> proxy(final List<E> subject) {
        //noinspection AnonymousInnerClass,AnonymousInnerClassWithTooManyMethods
        return new AbstractList<E>() {
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
    @SuppressWarnings({"TypeMayBeWeakened", "ReturnOfInnerClass"})
    public static <E> Set<E> proxy(final Set<E> subject) {
        //noinspection AnonymousInnerClass,AnonymousInnerClassWithTooManyMethods
        return new AbstractSet<E>() {
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
     * Returns a new {@link Builder} for target instances as supplied by the given {@link Supplier}.
     *
     * @param <E> The element type.
     * @param <C> The final type of the target instances, at least {@link Collection}.
     */
    public static <E, C extends Collection<E>> Builder<E, C> builder(final Supplier<C> newTarget) {
        return new Builder<>(newTarget, Builder.class);
    }

    /**
     * Utility interface to set up a target instance of {@link Collection}.
     *
     * @param <E> The element type.
     * @param <C> The final type of the target instance, at least {@link Collection}.
     * @param <S> The final type of the Setup implementation.
     */
    public interface Setup<E, C extends Collection<E>, S extends Setup<E, C, S>>
            extends de.team33.patterns.building.elara.Setup<C, S> {

        /**
         * Adds an element to the instance to be set up.
         *
         * @throws UnsupportedOperationException if {@link Collection#add(Object)} is not supported by the instance
         *                                       to be set up.
         * @throws ClassCastException            if the class of the specified {@code element} prevents it from being
         *                                       added to the {@code subject}
         *                                       (may occur only if used raw or forced in a mismatched class context).
         * @throws NullPointerException          if the specified {@code element} is {@code null} and the instance
         *                                       to be set up does not permit {@code null} elements.
         * @throws IllegalArgumentException      if some property of the {@code element} prevents it from being added to
         *                                       the {@code subject}.
         * @throws IllegalStateException         if the {@code element} cannot be added at this time due to
         *                                       the {@code subject}'s insertion restrictions (if any).
         * @see Collection#add(Object)
         */
        default S add(final E element) {
            return setup(c -> Collecting.add(c, element));
        }

        /**
         * @see Collection#add(Object)
         */
        default S add(final E element0, final E element1, final E... more) {
            return setup(c -> Collecting.add(c, element0, element1, more));
        }

        default S addAll(final Collection<? extends E> elements) {
            return setup(c -> Collecting.addAll(c, elements));
        }

        default S addAll(final Stream<? extends E> elements) {
            return setup(c -> Collecting.addAll(c, elements));
        }

        default S addAll(final Iterable<? extends E> elements) {
            return setup(c -> Collecting.addAll(c, elements));
        }

        default S addAll(final E[] elements) {
            return setup(c -> Collecting.addAll(c, elements));
        }
    }

    /**
     * Builder implementation to build target instances of {@link Collection}.
     *
     * @param <E> The element type.
     * @param <C> The final type of the target instances, at least {@link Collection}.
     */
    public static class Builder<E, C extends Collection<E>>
            extends LateBuilder<C, Builder<E, C>>
            implements Setup<E, C, Builder<E, C>> {

        @SuppressWarnings({"rawtypes", "unchecked"})
        private Builder(final Supplier<C> newResult, final Class builderClass) {
            super(newResult, builderClass);
        }
    }
}
