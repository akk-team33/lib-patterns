package de.team33.patterns.collection.ceres;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * {@linkplain Collections Additional} convenience methods to deal with Collections.
 */
@SuppressWarnings({"ProhibitedExceptionCaught", "unused"})
public final class Mapping {

    private Mapping() {
    }

    /**
     * Just like {@link Map#put(Object, Object) subject.put(key, value)} for a given {@code subject},
     * but returns the {@code subject}.
     *
     * @throws UnsupportedOperationException if {@link Map#put(Object, Object)} is not supported by the {@code subject}.
     * @throws NullPointerException          if {@code subject} is {@code null} or if the specified <em>key</em> or
     *                                       <em>value</em> is {@code null} and the {@code subject} does not permit
     *                                       {@code null} <em>keys</em> or <em>values</em>
     * @throws ClassCastException            if the class of the specified <em>key</em> or <em>value</em> prevents it
     *                                       from being put into the {@code subject}
     *                                       (may occur only if used raw or forced in a mismatched class context).
     * @throws IllegalArgumentException      if some property of the specified <em>key</em> or <em>value</em> prevents
     *                                       it from being stored in the {@code subject}.
     * @see Map#put(Object, Object)
     */
    public static <K, V, M extends Map<? super K, ? super V>> M put(final M subject, final K key, final V value) {
        subject.put(key, value);
        return subject;
    }

    /**
     * Just like {@link Map#putAll(Map)} for a given {@code subject}, but ...
     *
     * @return The {@code subject}.
     * @throws UnsupportedOperationException if the <tt>putAll</tt> operation is not supported by the {@code subject}.
     * @throws ClassCastException            if the class of a key or value in the specified map prevents it from being
     *                                       stored in the {@code subject}.
     * @throws NullPointerException          if the specified map is null, or if the {@code subject} does not permit
     *                                       null keys or values, and the specified map contains null keys or values.
     * @throws IllegalArgumentException      if some property of a key or value in the specified map prevents it from
     *                                       being stored in the {@code subject}.
     */
    public static <K, V, M extends Map<? super K, ? super V>> M putAll(final M subject,
                                                                       final Map<? extends K, ? extends V> origin) {
        subject.putAll(origin);
        return subject;
    }

    /**
     * Just like {@link Map#clear()} for a given {@code subject}, but ...
     *
     * @return The {@code subject}.
     * @throws NullPointerException          if {@code subject} is {@code null}.
     * @throws UnsupportedOperationException if {@link Collection#clear()} is not supported by the {@code subject}.
     * @see Map#clear()
     */
    public static <M extends Map<?, ?>> M clear(final M subject) {
        subject.clear();
        return subject;
    }

    /**
     * Just like {@link Map#remove(Object)} for a given {@code subject}, but avoids an unnecessary
     * {@link ClassCastException} or {@link NullPointerException} which might be caused by {@link Map#remove(Object)}
     * when the {@code subject} does not support the requested {@code key}.
     *
     * @return The {@code subject}.
     * @throws NullPointerException          if {@code subject} is {@code null}.
     * @throws UnsupportedOperationException if {@link Map#remove(Object)} is not supported by the {@code subject}.
     */
    public static <M extends Map<?, ?>> M remove(final M subject, final Object key) {
        try {
            subject.remove(key);

        } catch (final NullPointerException | ClassCastException caught) {
            if (null == subject) {
                throw caught; // expected to be a NullPointerException
            }

            // --> <subject> can not contain <key>
            // --> <subject> simply does not contain <key>
            // --> Nothing else to do.
        }
        return subject;
    }

    /**
     * Just like {@link Map#containsKey(Object)} for a given {@code subject}.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Map#containsKey(Object)} when the {@code subject} does not support the requested {@code key}.
     *
     * @throws NullPointerException if {@code subject} is {@code null}.
     */
    public static boolean containsKey(final Map<?, ?> subject, final Object key) {
        try {
            return subject.containsKey(key);

        } catch (final NullPointerException | ClassCastException caught) {
            if (null == subject) {
                throw caught; // expected to be a NullPointerException

            } else {
                // --> <subject> can not contain <key>
                // --> <subject> simply does not contain <key> ...
                return false;
            }
        }
    }

    /**
     * Just like {@link Map#containsValue(Object)} for a given {@code subject}.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Map#containsValue(Object)} when the {@code subject} does not support the requested {@code value}.
     *
     * @throws NullPointerException if {@code subject} is {@code null}.
     */
    public static boolean containsValue(final Map<?, ?> subject, final Object value) {
        try {
            return subject.containsValue(value);

        } catch (final NullPointerException | ClassCastException caught) {
            if (null == subject) {
                throw caught; // expected to be a NullPointerException

            } else {
                // --> <subject> can not contain <value>
                // --> <subject> simply does not contain <value> ...
                return false;
            }
        }
    }

    /**
     * Just like {@link Map#get(Object)} for a given {@code subject}.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Map#get(Object)} when the {@code subject} does not support the requested {@code key}.
     *
     * @return The value or {@code null} if the {@code subject} doesn't contain (an entry for) the {@code key}.
     * @throws NullPointerException if {@code subject} is {@code null}.
     */
    public static <V> V get(final Map<?, V> subject, final Object key) {
        try {
            return subject.get(key);

        } catch (final NullPointerException | ClassCastException caught) {
            if (null == subject) {
                throw caught; // expected to be a NullPointerException

            } else {
                // --> <subject> can not contain <key>
                // --> <subject> simply does not contain <key>
                // --> as specified for Map ...
                // noinspection ReturnOfNull
                return null;
            }
        }
    }

    /**
     * Supplies a proxy for a given {@link Map subject} that may be used to implement some {@link Map}-specific
     * methods, e.g.:
     * <ul>
     * <li>{@link Object#toString()}</li>
     * <li>{@link Map#equals(Object)}</li>
     * <li>{@link Map#hashCode()}</li>
     * <li>...</li>
     * </ul>
     *
     * @param subject A {@link Map}, that at least provides independently ...
     *                <ul>
     *                <li>{@link Map#entrySet()}</li>
     *                </ul>
     */
    @SuppressWarnings("AnonymousInnerClass")
    public static <K, V> Map<K, V> proxy(final Map<K, V> subject) {
        //noinspection ReturnOfInnerClass
        return new AbstractMap<K, V>() {
            @Override
            public Set<Entry<K, V>> entrySet() {
                return subject.entrySet();
            }
        };
    }
}
