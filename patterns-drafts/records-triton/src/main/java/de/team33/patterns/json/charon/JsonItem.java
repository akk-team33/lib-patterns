package de.team33.patterns.json.charon;

import java.util.Optional;

public interface JsonItem {

    /**
     * Determines whether <em>this</em> is an instance of the given <em>type</em>.
     */
    default boolean is(final Class<? extends JsonItem> type) {
        return type.isInstance(this);
    }

    /**
     * Returns <em>this</em> as an instance of the given <em>type</em>,
     * provided that it is indeed an instance of that type.
     *
     * @param <T> The expected type.
     * @throws ClassCastException if <em>this</em> is not an instance of the given <em>type</em>.
     * @see #is(Class)
     */
    default <T extends JsonItem> T as(final Class<T> type) {
        return type.cast(this);
    }

    /**
     * Returns <em>this</em> as an {@linkplain Optional optional} instance of the given <em>type</em>.
     * <p>
     * Returns {@link Optional#empty()} if <em>this</em> is not an instance of the given <em>type</em>.
     *
     * @param <T> The expected type.
     */
    @SuppressWarnings("BoundedWildcard")
    default <T extends JsonItem> Optional<T> asOptional(final Class<T> type) {
        return Optional.of(this)
                       .filter(type::isInstance)
                       .map(type::cast);
    }
}
