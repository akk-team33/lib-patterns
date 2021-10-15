package de.team33.patterns.properties.e4;

/**
 * Abstracts a <em>procedural</em> operation that can be performed on a given target of a particular type and that
 * returns that target as a <em>functional</em> result.
 *
 * @param <T0> The target type.
 */
@FunctionalInterface
public interface TargetOperation<T0> {

    /**
     * Performs this operation on the given target.
     *
     * @return The target.
     */
    <T extends T0> T to(T target);
}
