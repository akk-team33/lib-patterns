package de.team33.patterns.features.e1;

import java.util.function.Supplier;

/**
 * Serves for the identification and, on demand, the instantiation of <em>features</em> of a certain type.
 * <p>
 * In general, it is expected that an instance of {@link Key} has an identity semantic and is defined as a permanent
 * constant. It is not a good idea to generate a key inline as its identity is then ambiguous.
 * <p>
 * Example:
 * <pre>
 * class KeySample {
 *
 *     static final Key&lt;Random&gt; RANDOM = Random::new;
 *     static final Key&lt;Date&gt; DATE = Date::new;
 *     static final Key&lt;List&lt;Throwable&gt;&gt; PROBLEMS = LinkedList::new;
 *
 *     // ...
 * }
 * </pre>
 *
 * @param <T> The type of the <em>feature</em>.
 */
@FunctionalInterface
public interface Key<T> extends Supplier<T> {
}
