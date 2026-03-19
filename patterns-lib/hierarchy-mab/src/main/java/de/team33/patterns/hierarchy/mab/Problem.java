package de.team33.patterns.hierarchy.mab;

/**
 * Represents a problem that will be reported to a suitable consumer if a node cannot be listed due to an exception.
 *
 * @param <N> The node type.
 */
public interface Problem<N> {

    /**
     * Returns the <em>node</em> that cannot be listed.
     */
    N node();

    /**
     * Returns the exception that causes <em>this</em> {@link Problem}.
     */
    Exception cause();
}
