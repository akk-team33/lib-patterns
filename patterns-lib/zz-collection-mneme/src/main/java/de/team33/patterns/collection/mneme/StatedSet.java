package de.team33.patterns.collection.mneme;

import java.util.*;

public class StatedSet<E> extends AbstractSet<E> {

    private final List<E> core;

    private StatedSet(final List<E> core) {
        this.core = List.copyOf(core);
    }

    public static <E> Stage<E> stage(final E head) {
        return new Stage<>(head);
    }

    public static <E> Stage<E> stage(final Streamable<E> streamable) {
        return streamable.stream()
                         .map(StatedSet::stage)
                         .reduce(Stage::append)
                         .orElseGet(Stage::new);
    }

    @Override
    public final Iterator<E> iterator() {
        return core.iterator();
    }

    @Override
    public final int size() {
        return core.size();
    }

    public static class Stage<E> {

        private final List<E> coreList;
        private final Set<E> coreSet;

        private Stage(final E head) {
            final List<E> start = List.of(head);
            coreList = new ArrayList<>(start);
            coreSet = new HashSet<>(start);
        }

        private Stage() {
            coreList = new ArrayList<>();
            coreSet = new HashSet<>();
        }

        public final Stage<E> append(final Stage<? extends E> tail) {
            for (final E element : tail.coreList) {
                if (coreSet.add(element)) {
                    coreList.add(element);
                }
            }
            return this;
        }

        final StatedSet<E> stated() {
            return new StatedSet<E>(coreList);
        }
    }
}
