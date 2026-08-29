package de.team33.patterns.collection.mneme;

import de.team33.patterns.streamable.naiad.Streamable;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.List;

public class StatedSet<E> extends AbstractSet<E> {

    private final List<E> core;

    private StatedSet(final List<E> core) {
        this.core = core;
    }

    public static <E> StatedSet<E> empty() {
        return new StatedSet<>(List.of());
    }

    public static <E> StatedSet<E> of(final Streamable<E> source) {
        return new StatedSet<>(source.stream()
                                     .distinct()
                                     .toList());
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
