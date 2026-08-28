package de.team33.patterns.records.metis;

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
        final Map<String, Object> verified =
                map.entrySet().stream()
                   .filter(e -> description.containsKey(e.getKey()))
                   .map(this::verified)
                   .collect(LinkedHashMap::new,
                            (result, entry) -> result.put(entry.getKey(), entry.getValue()),
                            Map::putAll);
        return reflector.toRecord(verified);
    }

    private <V> Map.Entry<String, V> verified(final Map.Entry<String, V> entry) {
        final Class<?> target = description().get(entry.getKey()).core();
        final V value = entry.getValue();
        final Class<?> source = (null == value) ? null : value.getClass();
        if (target.isPrimitive() || null == source || target.isAssignableFrom(source)) {
            return entry;
        } else {
            throw new IllegalArgumentException(
                    ("cannot assign value ...%n" +
                     "    source value: %s%n" +
                     "    source type:  %s%n" +
                     "    target type:  %s%n").formatted(value, source, target));
        }
    }
}
