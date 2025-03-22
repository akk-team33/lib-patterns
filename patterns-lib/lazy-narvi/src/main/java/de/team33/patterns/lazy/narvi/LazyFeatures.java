package de.team33.patterns.lazy.narvi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A tool for managing properties of a host instance, which typically result from other properties of the host
 * and are only actually determined when needed. Once determined, properties are retained until reset.
 * <p>
 * <b>Example:</b>
 * <pre>
 * public class Sample {
 *
 *     // to manage some "lazy" features ...
 *     // ----------------------------------
 *     private final transient Features features = new Features();
 *
 *     // some "normal" properties (with corresponding getters and setters) ...
 *     // ---------------------------------------------------------------------
 *     private int intValue;
 *     private String stringValue;
 *     private Instant instantValue;
 *
 *     public final int getIntValue() {
 *         return intValue;
 *     }
 *
 *     public final Sample setIntValue(final int intValue) {
 *         // when a "normal" property is modified some "lazy" features will expire ...
 *         // -------------------------------------------------------------------------
 *         features.reset();
 *
 *         this.intValue = intValue;
 *         return this;
 *     }
 *
 *     public final String getStringValue() {
 *         return stringValue;
 *     }
 *
 *     public final Sample setStringValue(final String stringValue) {
 *         features.reset(); // s.a. - features will expire
 *         this.stringValue = stringValue;
 *         return this;
 *     }
 *
 *     public final Instant getInstantValue() {
 *         return instantValue;
 *     }
 *
 *     public final Sample setInstantValue(final Instant instantValue) {
 *         features.reset(); // s.a. - features will expire
 *         this.instantValue = instantValue;
 *         return this;
 *     }
 *
 *     // A List representation of this instance - a "lazy" feature ...
 *     // -------------------------------------------------------------
 *     public final List&lt;Object&gt; toList() {
 *         return features.get(Key.LIST);
 *     }
 *
 *     &#064;Override
 *     public final boolean equals(final Object obj) {
 *         return (this == obj) || ((obj instanceof final Sample other) &amp;&amp; toList().equals(other.toList()));
 *     }
 *
 *     // Also provided as "lazy" feature ...
 *     // -----------------------------------
 *     &#064;Override
 *     public final int hashCode() {
 *         return features.get(Key.HASH);
 *     }
 *
 *     // Also provided as "lazy" feature ...
 *     // -----------------------------------
 *     &#064;Override
 *     public final String toString() {
 *         return features.get(Key.STRING);
 *     }
 *
 *     // A local <em>Features</em> derivative (also works as <em>host</em> {@code <H>} here) ...
 *     // -------------------------------------------------------------
 *     private class Features extends LazyFeatures&lt;Features&gt; {
 *
 *         &#064;Override
 *         protected final Features host() {
 *             return this;
 *         }
 *
 *         // --------------------------------------------------------------
 *         // ... and provides factory methods for the "lazy" features ...
 *         // --------------------------------------------------------------
 *
 *         private List&lt;Object&gt; newList() {
 *             return Arrays.asList(intValue, stringValue, instantValue);
 *         }
 *
 *         private Integer newHash() {
 *             return toList().hashCode();
 *         }
 *
 *         private String newString() {
 *             return Sample.class.getSimpleName() + toList().toString();
 *         }
 *     }
 *
 *     // A local <em>Key</em> derivative ...
 *     // --------------------------
 *     &#064;FunctionalInterface
 *     private interface Key&lt;R&gt; extends LazyFeatures.Key&lt;Features, R&gt; {
 *
 *         // ... to simplify the local <em>Key</em> definitions ...
 *         // ---------------------------------------------
 *         Key&lt;List&lt;Object&gt;&gt; LIST = Features::newList;
 *         Key&lt;Integer&gt; HASH = Features::newHash;
 *         Key&lt;String&gt; STRING = Features::newString;
 *     }
 * }
 * </pre>
 *
 * @param <H> The host type.
 * @see #get(Key)
 * @see #reset(Key)
 * @see #reset()
 */
@SuppressWarnings({"WeakerAccess", "AbstractClassWithOnlyOneDirectInheritor"})
public abstract class LazyFeatures<H> {

    @SuppressWarnings("rawtypes")
    private final Map<Key, Lazy> backing = Collections.synchronizedMap(new HashMap<>(0));

    /**
     * Returns the actual host {@code <H>}.
     * This is the instance that finally provides the initialization code referred by the <em>keys</em>.
     */
    protected abstract H host();

    /**
     * Determines whether the initialization code provided by the given <em>key</em> has already been executed
     * and its result stored.
     * <p>
     * Returns an {@link Optional} containing the stored result if any, otherwise {@link Optional#empty()}.
     * <p>
     * <b>CAUTION:</b> returns {@link Optional#empty()} even if a stored result exists but is {@code null}!
     *
     * @param <R> The result type.
     */
    public final <R> Optional<R> peek(final Key<? super H, ? extends R> key) {
        @SuppressWarnings("unchecked")
        final Lazy<R> lazy = backing.get(key);
        return Optional.ofNullable(lazy).map(Lazy::get);
    }

    /**
     * On the first call after initialization or reset, executes the initialization code
     * provided by the given <em>key</em> and stores the result.
     * <p>
     * Returns the stored result.
     *
     * @param <R> The result type.
     * @see #reset(Key)
     * @see #reset()
     */
    public final <R> R get(final Key<? super H, ? extends R> key) {
        @SuppressWarnings("unchecked")
        final Lazy<R> lazy = backing.computeIfAbsent(key, this::newLazy);
        return lazy.get();
    }

    private <R> Lazy<R> newLazy(final Key<? super H, ? extends R> key) {
        return Lazy.init(() -> key.init(host()));
    }

    /**
     * Resets (discards) the stored result for the given <em>key</em>.
     */
    public final void reset(final Key<?, ?> key) {
        backing.remove(key);
    }

    /**
     * Resets (discards) all the stored results.
     */
    public final void reset() {
        backing.clear();
    }

    /**
     * Abstracts the <em>keys</em> needed to access values managed by a {@link LazyFeatures} instance.
     *
     * @param <H> The host type - the type of instance that finally provides the initialization code referred by the
     *            <em>keys</em>.
     * @param <R> The result type.
     */
    @SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
    @FunctionalInterface
    public interface Key<H, R> {

        R init(H host);
    }
}
