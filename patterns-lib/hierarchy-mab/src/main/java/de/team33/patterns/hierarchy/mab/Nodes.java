package de.team33.patterns.hierarchy.mab;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.WARNING;

/**
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/io-adrastea/">io-adrastea</a>
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/io-adrastea/apidocs/">io-adrastea/apidocs</a>
 * @deprecated this is basically just an implementation core for the deprecated module
 * <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/io-adrastea/apidocs/">io-adrastea</a>
 * and is largely useless on its own.
 */
@Deprecated
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
     * @deprecated see {@link Nodes}.
     */
    @Deprecated
    public interface Problem<N> {

        /**
         * @deprecated see {@link Nodes}.
         */
        @Deprecated
        N node();

        /**
         * @deprecated see {@link Nodes}.
         */
        @Deprecated
        Exception cause();
    }

    /**
     * @deprecated see {@link Nodes}.
     */
    @Deprecated
    public interface Lister<N, P extends Problem<N>> {

        /**
         * @deprecated see {@link Nodes}.
         */
        @Deprecated
        default List<N> list(final N node) {
            return list(node, Nodes::log);
        }

        /**
         * @deprecated see {@link Nodes}.
         */
        @Deprecated
        List<N> list(N node, Consumer<? super P> onProblem);
    }

    /**
     * @deprecated see {@link Nodes}.
     */
    @Deprecated
    public static class Streamer<N, P extends Problem<N>, L extends Lister<N, P>> {

        @SuppressWarnings("rawtypes")
        private static final Predicate NEVER = new Predicate() {
            @Override
            public boolean test(final Object any) {
                return false;
            }

            @Override
            public Predicate or(final Predicate other) {
                return other;
            }
        };

        private final L lister;
        private final Predicate<N> skipCondition;

        /**
         * @deprecated see {@link Nodes}.
         */
        @SuppressWarnings("unchecked")
        @Deprecated
        public Streamer(final L lister, final Predicate<N> skipCondition) {
            this.lister = lister;
            this.skipCondition = (null == skipCondition) ? NEVER : skipCondition;
        }

        protected final L lister() {
            return lister;
        }

        protected final Predicate<N> skipCondition() {
            return skipCondition;
        }

        /**
         * @deprecated see {@link Nodes}.
         */
        @Deprecated
        public Stream<N> stream(final N node) {
            return stream(node, Nodes::log);
        }

        /**
         * @deprecated see {@link Nodes}.
         */
        @Deprecated
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
