package de.team33.patterns.config.eunomia;

import de.team33.patterns.records.metis.Metis;
import de.team33.patterns.typing.proteus.Type;

import java.util.HashMap;
import java.util.Map;

final class Merger<T extends Record> {

    private final Type<T> recordType;
    private final Map<String, Type<?>> description;

    private Merger(final Type<T> recordType) {
        this.recordType = recordType;
        this.description = Metis.description(recordType);
    }

    static <T extends Record> Merger<T> by(final Type<T> recordType) {
        return new Merger<>(recordType);
    }

    final T merge(final T left, final T right) {
        if (null == right) {
            return left;
        } else if (null == left) {
            return right;
        } else {
            return merge(Metis.toMap(left), Metis.toMap(right));
        }
    }

    private T merge(final Map<String, Object> leftMap, final Map<String, Object> rightMap) {
        final var map = description.keySet()
                                   .stream()
                                   .map(name -> merge(name, leftMap.get(name), rightMap.get(name)))
                                   .collect(HashMap::new, this::put, Map::putAll);
        return Metis.toRecord(recordType, map);
    }

    private void put(final Map<String, Object> map, Entry entry) {
        map.put(entry.name, entry.value);
    }

    private Entry merge(final String name, final Object left, final Object right) {
        final var value = (null == right) ? left : merge(description.get(name), left, right);
        return new Entry(name, value);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Object merge(final Type type, final Object left, final Object right) {
        if (type.core().isRecord()) {
            return by(type).merge((Record) left, (Record) right);
        } else {
            return right;
        }
    }

    private record Entry(String name, Object value) {
    }
}
