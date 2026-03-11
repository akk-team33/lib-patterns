package de.team33.patterns.hierarchy.mab;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A utility class for handling hierarchical organized data.
 */
public final class Hierarchy {

    private Hierarchy() {
    }

    /**
     * Returns a new {@link Streamer}.
     *
     * @param <I> The type of data item.
     */
    public static <I extends Item<I>> Streamer<I> streamer() {
        return new Streamer<>(any -> false);
    }

    /**
     * Represents an item within a hierarchical structure.
     *
     * @param <I> The final type of item.
     */
    @FunctionalInterface
    public interface Item<I extends Item<I>> {

        /**
         * Returns the immediate content of <em>this</em> {@link Item}.
         */
        List<I> list();
    }

    /**
     * Represents a tool that serves to stream hierarchical organized data.
     *
     * @param <I> The type of data item.
     */
    public static class Streamer<I extends Item<I>> {

        private final Predicate<I> skipCondition;

        private Streamer(final Predicate<I> skipCondition) {
            this.skipCondition = skipCondition;
        }

        private Stream<I> stream(final List<I> items) {
            return items.isEmpty() ? Stream.empty()
                                   : items.stream().flatMap(this::stream);
        }

        /**
         * Streams a given <em>item</em> of type {@code <I>}.
         */
        public final Stream<I> stream(final I item) {
            return skipCondition.test(item) ? Stream.empty()
                                            : Stream.concat(Stream.of(item), stream(item.list()));
        }

        /**
         * Returns a new {@link Streamer} that (additionally) skips items that meet the given <em>condition</em>,
         * including all their child items.
         */
        public final Streamer<I> skip(final Predicate<? super I> condition) {
            return new Streamer<>(skipCondition.or(condition));
        }
    }
}
