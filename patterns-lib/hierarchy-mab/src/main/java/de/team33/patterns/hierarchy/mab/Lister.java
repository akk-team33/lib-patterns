package de.team33.patterns.hierarchy.mab;

import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a tool that serves to list the immediate contents of any hierarchical structure
 * represented by an abstract <em>node</em>.
 *
 * @param <N> The type of <em>node</em>.
 * @param <P> A type of {@link Problem} that will be reported to a suitable {@link Consumer}
 *            if a node cannot be listed due to an exception.
 */
public interface Lister<N, P extends Problem<N>> {

    /**
     * Returns a {@link List} of the immediate contents of a given <em>node</em> within a hierarchical structure.
     * <p>
     * Returns an empty {@link List} if the given <em>node</em> represents a <em>'leaf'</em> within the respective
     * hierarchical structure and thus cannot have any contents.
     * <p>
     * Also returns an empty {@link List} if the given <em>node</em> refuses access to its contents
     * and throws an exception. In that case, the problem will be logged to a {@link System.Logger}.
     */
    default List<N> list(final N node) {
        return list(node, Util::log);
    }

    /**
     * Returns a {@link List} of the immediate contents of a given <em>node</em> within a hierarchical structure.
     * <p>
     * Returns an empty {@link List} if the given <em>node</em> represents a <em>'leaf'</em> within the respective
     * hierarchical structure and thus cannot have any contents.
     * <p>
     * Also returns an empty {@link List} if the given <em>node</em> refuses access to its contents
     * and throws an exception. In that case, a corresponding {@link Problem} will be reported to the given
     * {@link Consumer}.
     */
    List<N> list(N node, Consumer<? super P> onProblem);
}
