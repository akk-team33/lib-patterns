package de.team33.patterns.records.rho;

import de.team33.patterns.typing.proteus.Type;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.unmodifiableMap;

final class Refractor<T extends Record> {

    @SuppressWarnings("rawtypes")
    private static final Map<Type, Refractor> CACHE = new ConcurrentHashMap<>();

    private final Reflector<T> reflector;
    private final Map<String, Type<?>> description;

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Refractor(final Type<T> recordType) {
        this.reflector = Reflector.of((Class) recordType.core());
        this.description = unmodifiableMap(
                reflector.components()
                         .stream()
                         .collect(LinkedHashMap::new,
                                  (map, component) -> map.put(component.getName(),
                                                              recordType.typeOf(component)),
                                  Map::putAll));
    }

    @SuppressWarnings("unchecked")
    static <T extends Record> Refractor<T> of(final Type<T> recordType) {
        return CACHE.computeIfAbsent(recordType, Refractor::new);
    }

    final Map<String, Type<?>> description() {
        return description;
    }

    final Map<String, Object> toMap(final T record) {
        return reflector.toMap(record);
    }

    final T toRecord(final Map<String, Object> map) {
        return reflector.toRecord(map);
    }
}
