package de.team33.patterns.serial.charon;

import java.util.List;

final class Charged<E> extends Series<E> {

    private final List<E> backing;
    private final int headIndex;

    private Charged(final List<E> backing, final int headIndex) {
        this.headIndex = headIndex;
        this.backing = backing;
    }

    static <E> Series<E> seriesOf(final List<E> backing, final int headIndex) {
        assert 0 <= headIndex;
        return (headIndex < backing.size()) ? new Charged<>(backing, headIndex) : empty();
    }

    @Override
    public final E head() {
        return backing.get(headIndex);
    }

    @Override
    public final Series<E> tail() {
        return seriesOf(backing, headIndex + 1);
    }

    @Override
    public final int size() {
        return backing.size() - headIndex;
    }

    @Override
    public final List<E> asList() {
        return backing.subList(headIndex, backing.size());
    }
}
