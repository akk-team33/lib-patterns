package net.team33.patterns;

import java.util.Collections;
import java.util.SortedMap;

public class SortedMapper<K, V, M extends SortedMap<K, V>> extends Mapper<K, V, M> {
    private SortedMapper(final M subject) {
        super(subject);
    }

    public static <K, V, M extends SortedMap<K, V>> SortedMapper<K, V, M> map(final M subject) {
        return new SortedMapper<>(subject);
    }

    @Override
    public final SortedMap<K, V> unmodifiable() {
        return Collections.unmodifiableSortedMap(getSubject());
    }
}
