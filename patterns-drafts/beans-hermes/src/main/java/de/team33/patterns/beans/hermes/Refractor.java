package de.team33.patterns.beans.hermes;

import de.team33.patterns.collection.mneme.FinalEntry;
import de.team33.patterns.collection.mneme.FinalMap;
import de.team33.patterns.typing.proteus.Type;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class Refractor<T> {

    @SuppressWarnings("rawtypes")
    private static final Map<Type, Refractor> CACHE = new ConcurrentHashMap<>();

    private final Reflector<T> reflector;
    private final FinalMap<String, Type<?>> description;

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Refractor(final Type<T> beanType) {
        this.reflector = Reflector.of((Class) beanType.core());
        this.description = reflector.components().stream()
                                    .map(FinalEntry.mapping(BeanComponent::getName,
                                                            component -> component.type(beanType)))
                                    .collect(FinalMap.collector());
    }

    @SuppressWarnings("unchecked")
    static <T> Refractor<T> of(final Type<T> recordType) {
        return CACHE.computeIfAbsent(recordType, Refractor::new);
    }

    final Map<String, Type<?>> description() {
        // Already is immutable ...
        // noinspection AssignmentOrReturnOfFieldWithMutableType
        return description;
    }

    final Map<String, Object> toMap(final T record) {
        return reflector.toMap(record);
    }

    final T toBean(final Map<String, Object> map) {
        final Map<String, Object> verified =
                map.entrySet().stream()
                   .filter(e -> description.containsKey(e.getKey()))
                   .map(this::verified)
                   .collect(LinkedHashMap::new,
                            (result, entry) -> result.put(entry.getKey(), entry.getValue()),
                            Map::putAll);
        return reflector.toBean(verified);
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
