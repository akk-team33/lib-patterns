package de.team33.patterns.tuple.janus;

import java.util.Arrays;
import java.util.List;

/**
 * @deprecated appears largely useless.
 */
@Deprecated
public class Pair<L, R> extends Tuple {

    private final L left;
    private final R right;

    /**
     * @deprecated see {@link Pair}.
     */
    @Deprecated
    public Pair(final L left, final R right) {
        this.left = left;
        this.right = right;
    }

    /**
     * @deprecated see {@link Pair}.
     */
    @Deprecated
    public static <L, R> Pair<L, R> of(final L left, final R right) {
        return new Pair<>(left, right);
    }

    /**
     * @deprecated see {@link Pair}.
     */
    @Deprecated
    public final L left() {
        return left;
    }

    /**
     * @deprecated see {@link Pair}.
     */
    @Deprecated
    public final R right() {
        return right;
    }

    /**
     * @deprecated see {@link Pair}.
     */
    @Deprecated
    @Override
    public final List<Object> toList() {
        return Arrays.asList(left, right);
    }
}
