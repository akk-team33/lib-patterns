package de.team33.patterns.tuple.janus;

import java.util.Arrays;
import java.util.List;

public class Pair<L, R> extends Listable {

    private final L left;
    private final R right;

    public Pair(final L left, final R right) {
        this.left = left;
        this.right = right;
    }

    public static <L, R> Pair<L, R> of(final L left, final R right) {
        return new Pair<>(left, right);
    }

    public final L getLeft() {
        return left;
    }

    public final R getRight() {
        return right;
    }

    @Override
    public final List<Object> toList() {
        return Arrays.asList(left, right);
    }
}
