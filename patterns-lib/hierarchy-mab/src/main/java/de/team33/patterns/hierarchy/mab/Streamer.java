package de.team33.patterns.hierarchy.mab;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A tool that serves to stream the recursive contents of any hierarchical structure
 * represented by an abstract <em>node</em>.
 *
 * @param <N> The type of <em>node</em>.
 * @param <P> A type of {@link Problem} that will be reported to a suitable {@link Consumer}
 *            if a node cannot be listed due to an exception.
 * @param <L> A type of {@link Lister} that will be used to list the immediate contents of a <em>node</em>.
 */
@SuppressWarnings("unused")
public class Streamer<N, P extends Problem<N>, L extends Lister<N, P>> {

    private final L lister;
    private final Predicate<N> skipCondition;

    /**
     * Initializes a new instance.
     *
     * @param lister        A {@link Lister} of type {@code <L>}
     *                      that will be used to list the immediate contents of a <em>node</em>.
     * @param skipCondition A {@link Predicate} that determines if a <em>node</em> (and its entire contents)
     *                      will be skipped and thus excluded from a resulting stream.
     */
    public Streamer(final L lister, final Predicate<? super N> skipCondition) {
        this.lister = lister;
        this.skipCondition = skipCondition::test;
    }

    protected final L lister() {
        return lister;
    }

    protected final Predicate<N> skipCondition() {
        return skipCondition;
    }

    /**
     * Returns a {@link Stream} starting with the given <em>node</em> followed by its recursive contents.
     * <p>
     * If an involved <em>node</em> refuses access to its contents and throws an exception,
     * the problem will be logged to a {@link System.Logger}.
     */
    public final Stream<N> stream(final N node) {
        return stream(node, Util::log);
    }

    /**
     * Returns a {@link Stream} starting with the given <em>node</em> followed by its recursive contents.
     * <p>
     * If an involved <em>node</em> refuses access to its contents and throws an exception,
     * a corresponding {@link Problem} will be reported to the given {@link Consumer}.
     */
    public final Stream<N> stream(final N node, final Consumer<? super P> onProblem) {
        return new Actor(onProblem).stream(node);
    }

    private class Actor {

        private final Consumer<? super P> onProblem;

        private Actor(final Consumer<? super P> onProblem) {
            this.onProblem = onProblem;
        }

        private Stream<N> stream(final N node) {
            if (skipCondition.test(node)) {
                return Stream.empty();
            } else {
                return stream(Stream.of(node), lister.list(node, onProblem));
            }
        }

        private Stream<N> stream(final Stream<N> head, final List<N> tail) {
            return tail.isEmpty() ? head : Stream.concat(head, tail.stream().flatMap(this::stream));
        }
    }
}
