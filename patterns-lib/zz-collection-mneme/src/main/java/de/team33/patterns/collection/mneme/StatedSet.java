package de.team33.patterns.collection.mneme;

import de.team33.patterns.streamable.naiad.Streamable;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.List;

public class StatedSet<E> extends AbstractSet<E> {

    private final List<E> core;

    @SuppressWarnings({"rawtypes", "unchecked"})
    private StatedSet(final List<? extends E> core) {
        this.core = (List) core;
    }

    public StatedSet() {
        this(List.of());
    }

    public StatedSet(final Iterable<? extends E> source) {
        this(Streamable.of(source));
    }

    public StatedSet(final Streamable<? extends E> source) {
        this(source.stream().distinct().toList());
    }

    @Override
    public final Iterator<E> iterator() {
        return core.iterator();
    }

    @Override
    public final int size() {
        return core.size();
    }
}
