package de.team33.patterns.collection.ceres;

import de.team33.patterns.building.elara.LateBuilder;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Some convenience methods to deal with {@link Map}s.
 */
@SuppressWarnings({"ProhibitedExceptionCaught", "unused"})
public final class Mapping {

    private Mapping() {
    }

    /**
     * Just like {@link Map#put(Object, Object) subject.put(key, value)} for a given <em>subject</em>,
     * but returns the <em>subject</em>.
     *
     * @throws UnsupportedOperationException if {@link Map#put(Object, Object)} is not supported by the
     *                                       <em>subject</em>.
     * @throws NullPointerException          if <em>subject</em> is {@code null} or ...
     * @throws NullPointerException          if the specified <em>key</em> or <em>value</em> is {@code null}
     *                                       and the <em>subject</em> does not permit {@code null}
     *                                       <em>keys</em> or <em>values</em>
     * @throws ClassCastException            if the class of the specified <em>key</em> or <em>value</em> prevents it
     *                                       from being put into the <em>subject</em>
     *                                       (may occur only if used raw or forced in a mismatched class context).
     * @throws IllegalArgumentException      if some property of the specified <em>key</em> or <em>value</em> prevents
     *                                       it from being stored in the <em>subject</em>.
     * @see Map#put(Object, Object)
     */
    public static <K, V, M extends Map<K, V>> M put(final M subject, final K key, final V value) {
        subject.put(key, value);
        return subject;
    }

    /**
     * Just like {@link Map#putAll(Map) subject.putAll(origin)} for a given <em>subject</em>,
     * but returns the <em>subject</em>.
     *
     * @throws UnsupportedOperationException if {@link Map#putAll(Map)} is not supported by the <em>subject</em>.
     * @throws NullPointerException          if <em>subject</em> or <em>origin</em> is {@code null} or ...
     * @throws NullPointerException          if any of the specified <em>keys</em> or <em>values</em> are
     *                                       {@code null} and the {@code subject} does not permit {@code null}
     *                                       <em>keys</em> or <em>values</em>.
     * @throws ClassCastException            if the class of any specified <em>key</em> or <em>value</em> prevents it
     *                                       from being put into the <em>subject</em>
     *                                       (may occur only if used raw or forced in a mismatched class context).
     * @throws IllegalArgumentException      if some property of any <em>key</em> or <em>value</em> in the specified
     *                                       <em>origin</em> prevents it from being stored in the <em>subject</em>.
     * @see Map#putAll(Map)
     */
    public static <K, V, M extends Map<? super K, ? super V>> M putAll(final M subject,
                                                                       final Map<? extends K, ? extends V> origin) {
        subject.putAll(origin);
        return subject;
    }

    /**
     * Just like {@link Map#clear() subject.clear()} for a given <em>subject</em>
     * but returns the <em>subject</em>.
     *
     * @throws UnsupportedOperationException if {@link Collection#clear()} is not supported by the <em>subject</em>.
     * @throws NullPointerException          if <em>subject</em> is {@code null}.
     * @see Map#clear()
     */
    public static <M extends Map<?, ?>> M clear(final M subject) {
        subject.clear();
        return subject;
    }

    /**
     * Just like {@link Map#remove(Object) subject.remove(key)} for a given <em>subject</em>
     * but returns the <em>subject</em>.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Map#remove(Object)} when the <em>subject</em> does not support the requested <em>key</em>.
     *
     * @throws UnsupportedOperationException if {@link Map#remove(Object)} is not supported by the <em>subject</em>.
     * @throws NullPointerException          if <em>subject</em> is {@code null}.
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
     * Just like {@link Map#containsKey(Object)} for a given <em>subject</em>.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Map#containsKey(Object)} when the <em>subject</em> does not support the requested <em>key</em>.
     *
     * @throws NullPointerException if <em>subject</em> is {@code null}.
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
     * Just like {@link Map#containsValue(Object)} for a given <em>subject</em>.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Map#containsValue(Object)} when the <em>subject</em> does not support the requested <em>value</em>.
     *
     * @throws NullPointerException if <em>subject</em> is {@code null}.
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
     * Just like {@link Map#get(Object)} for a given <em>subject</em>.
     * <p>
     * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
     * {@link Map#get(Object)} when the <em>subject</em> does not support the requested <em>key</em>.
     *
     * @return The value or {@code null} if the <em>subject</em> doesn't contain (an entry for) the <em>key</em>.
     * @throws NullPointerException if <em>subject</em> is {@code null}.
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

    /**
     * Returns a new {@link Builder} for target instances as supplied by the given {@link Supplier}.
     *
     * @param <K> The key type of the target instance.
     * @param <V> The value type of the target instance.
     * @param <M> The final type of the target instance, at least {@link Map}.
     */
    public static <K, V, M extends Map<K, V>> Builder<K, V, M> builder(final Supplier<M> newTarget) {
        return new Builder<>(newTarget, Builder.class);
    }

    /**
     * Returns a new {@link Charger} for a given target instance.
     *
     * @param <K> The key type of the target instance.
     * @param <V> The value type of the target instance.
     * @param <M> The final type of the target instance, at least {@link Map}.
     */
    public static <K, V, M extends Map<K, V>> Charger<K, V, M> charger(final M target) {
        return new Charger<>(target, Charger.class);
    }

    /**
     * Utility interface to set up a target instance of {@link Map}.
     *
     * @param <K> The key type of the target instance.
     * @param <V> The value type of the target instance.
     * @param <M> The final type of the target instance, at least {@link Map}.
     * @param <S> The final type of the Setup implementation.
     */
    @SuppressWarnings("ClassNameSameAsAncestorName")
    @FunctionalInterface
    public interface Setup<K, V, M extends Map<K, V>, S extends Setup<K, V, M, S>>
            extends de.team33.patterns.building.elara.Setup<M, S> {

        /**
         * Puts a pair of <em>key / value</em> to the instance to be set up.
         *
         * @throws UnsupportedOperationException if {@link Map#put(Object, Object)} is not supported by the instance
         *                                       to be set up.
         * @throws NullPointerException          if the specified <em>key</em> or <em>value</em> is {@code null}
         *                                       and the instance to be set up does not permit {@code null}
         *                                       <em>keys</em> or <em>values</em>
         * @throws ClassCastException            if the class of the specified <em>key</em> or <em>value</em> prevents
         *                                       it from being put into the instance to be set up
         *                                       (may occur only if used raw or forced in a mismatched class context).
         * @throws IllegalArgumentException      if some property of the specified <em>key</em> or <em>value</em>
         *                                       prevents it from being stored in the instance to be set up.
         * @see Map#put(Object, Object)
         * @see Mapping#put(Map, Object, Object)
         */
        default S put(final K key, final V value) {
            return setup(target -> Mapping.put(target, key, value));
        }

        /**
         * Removes a pair of a given <em>key</em> and its associated <em>value</em> from the instance to be set up.
         * <p>
         * Avoids an unnecessary {@link ClassCastException} or {@link NullPointerException} which might be caused by
         * {@link Map#remove(Object)} when the instance to be set up does not support the requested <em>key</em>.
         *
         * @throws UnsupportedOperationException if {@link Map#remove(Object)} is not supported by the
         *                                       <em>subject</em>.
         * @see Map#remove(Object)
         * @see Mapping#remove(Map, Object)
         */
        default S remove(final Object key) {
            return setup(target -> Mapping.remove(target, key));
        }

        /**
         * Puts multiple pairs of <em>key / value</em> to the instance to be set up.
         * <p>
         * If <em>origin</em> is {@code null} it will be treated as an empty {@link Map}.
         *
         * @throws UnsupportedOperationException if {@link Map#putAll(Map)} is not supported
         *                                       by the instance to be set up.
         * @throws NullPointerException          if any of the specified <em>keys</em> or <em>values</em> are
         *                                       {@code null} and the instance to be set up does not permit
         *                                       {@code null} <em>keys</em> or <em>values</em>.
         * @throws ClassCastException            if the class of any specified <em>key</em> or <em>value</em> prevents it
         *                                       from being put into the <em>subject</em>
         *                                       (may occur only if used raw or forced in a mismatched class context).
         * @throws IllegalArgumentException      if some property of any <em>key</em> or <em>value</em> in the specified
         *                                       <em>origin</em> prevents it from being stored in the <em>subject</em>.
         * @see Map#putAll(Map)
         * @see Mapping#putAll(Map, Map)
         */
        default S putAll(final Map<? extends K, ? extends V> origin) {
            return setup(target -> Mapping.putAll(target, (null == origin) ? Collections.emptyMap() : origin));
        }

        /**
         * Removes all pairs of <em>key / value</em> from the instance to be set up.
         *
         * @throws UnsupportedOperationException if {@link Collection#clear()} is not supported by the <em>subject</em>.
         * @see Map#clear()
         * @see Mapping#clear(Map)
         */
        default S clear() {
            return setup(Mapping::clear);
        }
    }

    /**
     * Builder implementation to build target instances of {@link Map}.
     * <p>
     * Use {@link #builder(Supplier)} to get an instance.
     *
     * @param <K> The key type of the target instance.
     * @param <V> The value type of the target instance.
     * @param <M> The final type of the target instance, at least {@link Map}.
     */
    public static class Builder<K, V, M extends Map<K, V>>
            extends LateBuilder<M, Builder<K, V, M>>
            implements Setup<K, V, M, Builder<K, V, M>> {

        @SuppressWarnings({"rawtypes", "unchecked"})
        private Builder(final Supplier<M> newResult, final Class builderClass) {
            super(newResult, builderClass);
        }
    }

    /**
     * Charger implementation to charge target instances of {@link Map}.
     * <p>
     * Use {@link #charger(Map)} to get an instance.
     *
     * @param <K> The key type of the target instance.
     * @param <V> The value type of the target instance.
     * @param <M> The final type of the target instance, at least {@link Map}.
     */
    @SuppressWarnings("ClassNameSameAsAncestorName")
    public static class Charger<K, V, M extends Map<K, V>>
            extends de.team33.patterns.building.elara.Charger<M, Charger<K, V, M>>
            implements Setup<K, V, M, Charger<K, V, M>> {

        @SuppressWarnings({"rawtypes", "unchecked"})
        private Charger(final M target, final Class builderClass) {
            super(target, builderClass);
        }
    }
}
