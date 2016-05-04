package net.team33.patterns;

import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;

/**
 * {@linkplain Collections Additional} convenience methods to deal with Collections.
 */
public final class Collecting {
    private Collecting() {
    }

    /**
     * Just like {@link Collection#add(Object)} for a given {@code subject}, but ...
     *
     * @return The {@code subject}.
     * @throws UnsupportedOperationException if {@link Collection#add(Object)} is not supported by the {@code subject}.
     * @throws ClassCastException            if the class of the specified {@code element} prevents it from being
     *                                       added to the {@code subject}
     *                                       (may occur only if used raw or forced in a mismatched class context).
     * @throws NullPointerException          <ul>
     *                                       <li>if {@code subject} is {@code null}.</li>
     *                                       <li>if the specified {@code element} is {@code null}
     *                                       and the {@code subject} does not permit {@code null} elements.</li>
     *                                       </ul>
     * @throws IllegalArgumentException      if some property of the {@code element} prevents it from being added to
     *                                       the {@code subject}.
     * @throws IllegalStateException         if the {@code element} cannot be added at this time due to
     *                                       the {@code subject}'s insertion restrictions (if any).
     */
    @SuppressWarnings("OverloadedVarargsMethod")
    public static <E, C extends Collection<? super E>> C add(final C subject, final E element) {
        subject.add(element);
        return subject;
    }

    /**
     * Similar to {@link Collection#add(Object)}
     * or rather {@link Collection#addAll(Collection)} for a given {@code subject}.
     * Allows to add a variable number of elements.
     *
     * @return The {@code subject}.
     * @throws UnsupportedOperationException if {@link Collection#addAll(Collection)} is not supported by the
     *                                       {@code subject}.
     * @throws ClassCastException            if the class of the specified {@code elements} prevents them from being
     *                                       added to the {@code subject}
     *                                       (may occur only if used raw or forced in a mismatched class context).
     * @throws NullPointerException          <ul>
     *                                       <li>if {@code subject} or the {@code array} of {@code elements} is
     *                                       {@code null}.</li>
     *                                       <li>if some of the specified {@code elements} are {@code null}
     *                                       and the {@code subject} does not permit {@code null} elements.</li>
     *                                       </ul>
     * @throws IllegalArgumentException      if some property of some {@code elements} prevents them from being
     *                                       added to the {@code subject}.
     * @throws IllegalStateException         if the {@code elements} cannot be added at this time due to
     *                                       the {@code subject}'s insertion restrictions (if any).
     */
    @SuppressWarnings("OverloadedVarargsMethod")
    @SafeVarargs
    public static <E, C extends Collection<? super E>> C add(final C subject, final E... elements) {
        return addAll(subject, asList(elements));
    }

    /**
     * Just like {@link Collection#addAll(Collection)} for a given {@code subject}, but ...
     *
     * @return The {@code subject}.
     * @throws UnsupportedOperationException if {@link Collection#addAll(Collection)} is not supported by the
     *                                       {@code subject}.
     * @throws ClassCastException            if the class of the {@code elements} prevents them from being added to the
     *                                       {@code subject}
     *                                       (may occur only if used raw or forced in a mismatched class context).
     * @throws NullPointerException          <ul>
     *                                       <li>if {@code subject} or the {@link Collection} of {@code elements} is
     *                                       {@code null}</li>
     *                                       <li>if some {@code elements} are {@code null} and the {@code subject}
     *                                       does not permit {@code null} elements.</li>
     *                                       </ul>
     * @throws IllegalArgumentException      if some property of some {@code elements} prevents them from being added
     *                                       to the {@code subject}.
     * @throws IllegalStateException         if the {@code elements} cannot be added at this time due to the
     *                                       {@code subject}'s insertion restrictions (if any).
     */
    public static <E, C extends Collection<? super E>> C addAll(final C subject, final Collection<E> elements) {
        subject.addAll(elements);
        return subject;
    }

    /**
     * Just like {@link Collection#clear()} for a given {@code subject}, but ...
     *
     * @return The {@code subject}.
     * @throws NullPointerException          if {@code subject} is {@code null}.
     * @throws UnsupportedOperationException if {@link Collection#clear()} is not supported by the {@code subject}.
     */
    public static <C extends Collection<?>> C clear(final C subject) {
        subject.clear();
        return subject;
    }

    /**
     * Just like {@link Collection#remove(Object)} for a given {@code subject}, but if {@code subject} contains the
     * {@code element} several times, each occurrence will be removed!
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#remove(Object)} when the {@code subject} does not support the requested {@code element}.
     *
     * @return The {@code subject}.
     * @throws NullPointerException          if {@code subject} is {@code null}.
     * @throws UnsupportedOperationException if {@link Collection#remove(Object)} is not supported by the
     *                                       {@code subject}.
     */
    public static <C extends Collection<?>> C remove(final C subject, final Object element) {
        try {
            //noinspection StatementWithEmptyBody
            while (subject.remove(element)) {
            }

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
     * Similar to {@link Collection#remove(Object)}
     * or rather {@link Collection#removeAll(Collection)} for a given {@code subject}.
     * Allows to remove a variable number of elements.
     * <p>
     * If {@code subject} contains some of the {@code elements} several times, each occurrence will be removed!
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#remove(Object)} or {@link Collection#removeAll(Collection)} when the {@code subject} does not
     * support some of the requested {@code elements}.
     *
     * @return The {@code subject}.
     * @throws NullPointerException          if {@code subject} or the {@code array} of {@code elements} is
     *                                       {@code null}.
     * @throws UnsupportedOperationException if {@link Collection#remove(Object)} or
     *                                       {@link Collection#removeAll(Collection)} is not supported by the
     *                                       {@code subject}.
     * @see Collection#remove(Object)
     * @see Collection#removeAll(Collection)
     */
    @SuppressWarnings("OverloadedVarargsMethod")
    public static <C extends Collection<?>> C remove(final C subject, final Object... elements) {
        return removeAll(subject, asList(elements));
    }

    /**
     * Just like {@link Collection#removeAll(Collection)} for a given {@code subject}.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#removeAll(Collection)} when the {@code subject} does not support some of the requested
     * {@code elements}.
     *
     * @return The {@code subject}.
     * @throws NullPointerException          if {@code subject} or the {@link Collection} of {@code elements}
     *                                       is {@code null}.
     * @throws UnsupportedOperationException if {@link Collection#removeAll(Collection)} is not supported by the
     *                                       {@code subject}.
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
     * Similar to {@link Collection#retainAll(Collection)} for a given {@code subject}.
     * Allows to retain a variable number of elements.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#retainAll(Collection)} when the {@code subject} does not support some of the requested
     * {@code elements}.
     *
     * @return The {@code subject}.
     * @throws NullPointerException          if {@code subject} or the {@code array} of {@code elements} is
     *                                       {@code null}.
     * @throws UnsupportedOperationException if {@link Collection#retainAll(Collection)} is not supported by the
     *                                       {@code subject}.
     */
    public static <C extends Collection<?>> C retain(final C subject, final Object... elements) {
        return retainAll(subject, asList(elements));
    }

    /**
     * Just like {@link Collection#retainAll(Collection)} for a given {@code subject}.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#retainAll(Collection)} when the {@code subject} does not support some of the requested
     * {@code elements}.
     *
     * @return The {@code subject}.
     * @throws UnsupportedOperationException if {@link Collection#retainAll(Collection)} is not supported by the
     *                                       {@code subject}.
     * @throws NullPointerException          if {@code subject} or the {@link Collection} of {@code elements}
     *                                       is {@code null}.
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
     * Just like {@link Collection#contains(Object)} for a given {@code subject}.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#contains(Object)} when the {@code subject} does not support the requested {@code element}.
     *
     * @throws NullPointerException if {@code subject} is {@code null}.
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
     * Similar to {@link Collection#contains(Object)}
     * or rather {@link Collection#containsAll(Collection)} for a given {@code subject}.
     * Allows to test for a variable number of elements.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#containsAll(Collection)} when the {@code subject} does not support some of the requested
     * {@code elements}.
     *
     * @throws NullPointerException if {@code subject} or the {@code array} of {@code elements} is {@code null}.
     * @see Collection#containsAll(Collection)
     */
    @SuppressWarnings("OverloadedVarargsMethod")
    public static boolean contains(final Collection<?> subject, final Object... elements) {
        return containsAll(subject, asList(elements));
    }

    /**
     * Indicates if a given {@code subject} contains specific {@code elements}.
     * <p/>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Collection#containsAll(Collection)} when the {@code subject} does not support some of the requested
     * {@code elements}.
     *
     * @throws NullPointerException if {@code subject} or the {@link Collection} of {@code elements} is {@code null}.
     * @see Collection#containsAll(Collection)
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
     * Supplies a proxy for a given {@code subject} that may be used to implement some {@link Collection}-specific
     * methods, e.g.:
     * <ul>
     * <li>{@link Collection#toArray()}</li>
     * <li>{@link Collection#toArray(Object[])}</li>
     * <li>{@link Collection#toString()}</li>
     * <li>...</li>
     * </ul>
     *
     * @param subject A {@link Collection}, that at least provides independently ...
     *                <ul>
     *                <li>{@link Collection#iterator()}</li>
     *                <li>{@link Collection#size()}</li>
     *                </ul>
     */
    @SuppressWarnings({"AnonymousInnerClassWithTooManyMethods", "AnonymousInnerClass"})
    public static <E> Collection<E> proxy(final Collection<E> subject) {
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
     * <li>{@link List#toString()}</li>
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
    @SuppressWarnings({"AnonymousInnerClassWithTooManyMethods", "AnonymousInnerClass"})
    public static <E> List<E> proxy(final List<E> subject) {
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
     * <li>{@link Set#toString()}</li>
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
    @SuppressWarnings({"AnonymousInnerClassWithTooManyMethods", "AnonymousInnerClass", "TypeMayBeWeakened"})
    public static <E> Set<E> proxy(final Set<E> subject) {
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
}