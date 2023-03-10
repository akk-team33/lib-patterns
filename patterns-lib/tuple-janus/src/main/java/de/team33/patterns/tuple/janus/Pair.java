package de.team33.patterns.tuple.janus;

import java.util.Arrays;
import java.util.List;

public class Pair<L, R> extends Tuple {

    private final L left;
    private final R right;

    public Pair(final L left, final R right) {
        this.left = left;
        this.right = right;
    }

    public static <L, R> Pair<L, R> of(final L left, final R right) {
        return new Pair<>(left, right);
    }

    public final L left() {
        return left;
    }

    public final R right() {
        return right;
    }

    @Override
    public final List<Object> toList() {
        return Arrays.asList(left, right);
    }
}
