package de.team33.patterns.hierarchy.mab;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.WARNING;

/**
 * Utility class to handle <em>nodes</em> within a hierarchical data structure.
 */
public final class Nodes {

    private static final System.Logger LOGGER = System.getLogger(Nodes.class.getCanonicalName());

    private Nodes() {
    }

    static void log(final Problem<?> problem) {
        final Supplier<String> msgSupplier = () -> "Cannot list node <%s>".formatted(problem.node());
        LOGGER.log(WARNING, msgSupplier);
        LOGGER.log(DEBUG, msgSupplier, problem.cause());
    }

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
            return list(node, Nodes::log);
        }

        /**
         * Returns a {@link List} of the immediate contents of a given <em>node</em> within a hierarchical structure.
         * <p>
         * Returns an empty {@link List} if the given <em>node</em> represents a <em>'leaf'</em> within the respective
         * hierarchical structure and thus cannot have any contents.
         * <p>
         * Also returns an empty {@link List} if the given <em>node</em> refuses access to its contents
         * and throws an exception. In that case, a corresponding {@link Problem} of type {@code <P>}
         * will be reported to the given {@link Consumer}.
         */
        List<N> list(N node, Consumer<? super P> onProblem);
    }

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
    public static class Streamer<N, P extends Problem<N>, L extends Lister<N, P>> {

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
         * If an involved <em>node</em> refuses access to its contents and thus throws an exception,
         * the problem will be logged to a {@link System.Logger}.
         */
        public Stream<N> stream(final N node) {
            return stream(node, Nodes::log);
        }

        /**
         * Returns a {@link Stream} starting with the given <em>node</em> followed by its recursive contents.
         * <p>
         * If an involved <em>node</em> refuses access to its contents and thus throws an exception,
         * a corresponding {@link Problem} of type {@code <P>} will be reported to the given {@link Consumer}.
         */
        public Stream<N> stream(final N node, final Consumer<? super P> onProblem) {
            return new Actor(onProblem).stream(node);
        }

        private class Actor {

            private final Consumer<? super P> onProblem;

            private Actor(final Consumer<? super P> onProblem) {
                this.onProblem = onProblem;
            }

            private Stream<N> stream(final N node) {
                return skipCondition.test(node) ? Stream.empty()
                                                : stream(Stream.of(node), lister.list(node, onProblem));
            }

            private Stream<N> stream(final Stream<N> head, final List<N> tail) {
                return tail.isEmpty() ? head : Stream.concat(head, tail.stream().flatMap(this::stream));
            }
        }
    }
}
