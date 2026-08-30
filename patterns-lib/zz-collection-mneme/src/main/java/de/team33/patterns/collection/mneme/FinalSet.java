package de.team33.patterns.collection.mneme;

import de.team33.patterns.streamable.naiad.Streamable;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.List;

public class FinalSet<E> extends AbstractSet<E> {

    private final List<E> core;

    @SuppressWarnings({"rawtypes", "unchecked"})
    private FinalSet(final List<? extends E> core) {
        this.core = (List) core;
    }

    public FinalSet() {
        this(List.of());
    }

    public FinalSet(final Iterable<? extends E> source) {
        this(Streamable.of(source));
    }

    public FinalSet(final Streamable<? extends E> source) {
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
