package de.team33.patterns.lazy.lambda;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Manages features of a host instance that typically derive from other host properties
 * and are only determined when needed. Once determined, these features are retained until they are reset
 * or the host reaches the end of its lifecycle.
 * <p>
 * <b>Example:</b>
 * <pre>
 * public class Sample {
 *
 *     // Features managed for this instance ...
 *     // --------------------------------------
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
 *
 *         // when a "normal" property is modified some features must expire ...
 *         // ------------------------------------------------------------------
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
 *     // A private List representation of this instance ...
 *     // --------------------------------------------------
 *     private final List&lt;Object&gt; toList() {
 *         return features.get(Key.TO_LIST, () -&gt; List.of(intValue, stringValue, instantValue));
 *     }
 *
 *     &#064;Override
 *     public final boolean equals(final Object obj) {
 *         return (this == obj) || ((obj instanceof final Sample other) && toList().equals(other.toList()));
 *     }
 *
 *     // Also provided as a feature ...
 *     // ------------------------------
 *     &#064;Override
 *     public final int hashCode() {
 *         return features.get(Key.HASH_CODE, () -&gt; toList().hashCode());
 *     }
 *
 *     // Also provided as a feature ...
 *     // ------------------------------
 *     &#064;Override
 *     public final String toString() {
 *         return features.get(Key.TO_STRING, () -&gt; toList().toString());
 *     }
 *
 *     // Local keys for the features provided by this class ...
 *     // ------------------------------------------------------
 *     private interface Key&lt;R&gt; extends Features.Key&lt;R&gt; {
 *
 *         // ... to simplify the local Key definitions ...
 *         // ---------------------------------------------
 *         Key&lt;List&lt;Object&gt;&gt; TO_LIST = named("TO_LIST");
 *         Key&lt;Integer&gt; HASH_CODE = named("HASH_CODE");
 *         Key&lt;String&gt; TO_STRING = named("TO_STRING");
 *
 *         // Convenient factory for named keys ...
 *         // -------------------------------------
 *         static &lt;R&gt; Key&lt;R&gt; named(final String name) {
 *             return new Key&lt;R&gt;() {
 *                 &#064;Override
 *                 public final String toString() {
 *                     return name;
 *                 }
 *             };
 *         }
 *     }
 * }
 * </pre>
 *
 * @see #get(Key, Supplier)
 * @see #reset()
 */
public class Features {

    @SuppressWarnings("rawtypes")
    private final Map<Key, Lazy> backing = new ConcurrentHashMap<>();

    /**
     * Returns the value of the feature identified by the given <em>key</em>.
     * <p>
     * On the first call for a given <em>key</em> after instantiation or reset,
     * the initialization code defined by the given <em>supplier</em> is executed
     * and its result is retained for subsequent calls with that <em>key</em>.
     * <p>
     * This implementation is thread safe.
     *
     * @param <T> the result type.
     * @see #reset()
     */
    @SuppressWarnings("unchecked")
    public final <T> T get(final Key<T> key, final Supplier<? extends T> supplier) {
        return (T) backing.computeIfAbsent(key, any -> Lazy.init(supplier))
                          .get();
    }

    /**
     * Discards all values currently retained for the managed features.
     * <p>
     * This implementation is thread safe.
     */
    public final void reset() {
        backing.clear();
    }

    /**
     * Identifies a specific feature of a specific type.
     * <ul>
     *     <li>An implementation should have <em>identity semantics</em>,
     *     meaning it should intentionally not have its own implementation of {@link Object#equals(Object)}
     *     and {@link Object#hashCode()}.</li>
     *     <li>An instance should be constant, i.e., declared as {@code static final}.</li>
     * </ul>
     *
     * @param <T> The type of the identified feature.
     */
    @SuppressWarnings("MarkerInterface")
    public interface Key<T> {}
}
