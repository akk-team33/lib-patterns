package de.team33.patterns.properties.e1;

/**
 * Abstracts a <em>procedural</em> operation that can be performed on a given target of a particular type and that
 * returns that target as a <em>functional</em> result.
 *
 * @param <T> The target type.
 * @deprecated Further development is discontinued due to relatively high complexity with little benefit and this
 * package/module may be removed in a future release.
 */
@Deprecated
public interface TargetOperation<T> {

    /**
     * Performs this operation on the given target.
     *
     * @return The target.
     */
    <TX extends T> TX to(TX target);
}
