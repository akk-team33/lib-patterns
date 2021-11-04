package de.team33.patterns.features.e1;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Used to manage <em>features</em>.
 * <p>
 * Example:
 * <pre>
 * class FeatureSetSample {
 *
 *     static final Key&lt;Random&gt; RANDOM = Random::new;
 *     static final Key&lt;Date&gt; DATE = Date::new;
 *     static final Key&lt;List&lt;Throwable&gt;&gt; PROBLEMS = LinkedList::new;
 *
 *     private final FeatureSet features = new FeatureSet();
 *
 *     final int anyInt() {
 *         try {
 *             return features.get(RANDOM).nextInt();
 *         } catch (final RuntimeException e) {
 *             features.get(PROBLEMS).add(e);
 *             return -1;
 *         }
 *     }
 *
 *     final long getTime() {
 *         try {
 *             return features.get(DATE).getTime();
 *         } catch (final RuntimeException e) {
 *             features.get(PROBLEMS).add(e);
 *             return -1;
 *         }
 *     }
 *
 *     final List&lt;Throwable&gt; getProblems() {
 *         return features.get(PROBLEMS);
 *     }
 * }
 * </pre>
 */
public class FeatureSet {

    private final Map<Key<?>, Object> backing = new ConcurrentHashMap<>(0);

    /**
     * Returns the <em>feature</em> identified by the given {@link Key}.
     * When the <em>feature</em> in question is requested for the first time, it is created.
     * Once created, the same <em>feature</em> is always returned.
     *
     * @param <T> The type of the <em>feature</em>.
     */
    @SuppressWarnings("unchecked")
    public final <T> T get(final Key<T> key) {
        return (T) backing.computeIfAbsent(key, Key::get);
    }
}
