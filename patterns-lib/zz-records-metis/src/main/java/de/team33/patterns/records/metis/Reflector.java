package de.team33.patterns.records.metis;

import de.team33.patterns.collection.mneme.FinalMap;
import de.team33.patterns.streamable.naiad.Streamable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class Reflector<T extends Record> {

    @SuppressWarnings("rawtypes")
    private static final Map<Class, Reflector> CACHE = new ConcurrentHashMap<>();

    private final Streamable<RecordComponent> components;
    private final FinalMap<String, Class<?>> description;
    private final FinalMap<String, Method> accessors;
    private final Constructor<T> constructor;

    private Reflector(final Class<T> recordClass) {
        if (!recordClass.isRecord()) {
            throw new IllegalArgumentException(("no record type:%n" +
                                                "    %s%n").formatted(recordClass));
        }
        this.components = Streamable.of(recordClass.getRecordComponents());
        this.description = FinalMap.of(components, RecordComponent::getName, RecordComponent::getType);
        this.accessors = FinalMap.of(components, RecordComponent::getName, Reflector::accessor);
        this.constructor = constructor(recordClass, components);
    }

    private static Method accessor(final RecordComponent component) {
        final Method accessor = component.getAccessor();
        accessor.setAccessible(true);
        return accessor;
    }

    @SuppressWarnings("unchecked")
    static <T extends Record> Reflector<T> of(final Class<T> recordClass) {
        return CACHE.computeIfAbsent(recordClass, Reflector::new);
    }

    private static <T> Constructor<T> constructor(final Class<T> recordClass,
                                                  final Streamable<RecordComponent> components) {
        final Class<?>[] parameters = components.stream()
                                                .map(RecordComponent::getType)
                                                .toArray(Class<?>[]::new);
        try {
            final Constructor<T> result = recordClass.getDeclaredConstructor(parameters);
            result.setAccessible(true);
            return result;
        } catch (final NoSuchMethodException e) {
            throw new IllegalArgumentException(
                    ("cannot find constructor ...%n" +
                     "    record type: %s%n" +
                     "    parameters:  %s%n").formatted(recordClass, List.of(parameters)), e);
        }
    }

    final Streamable<RecordComponent> components() {
        return components;
    }

    final Map<String, Class<?>> description() {
        // Already is immutable ...
        // noinspection AssignmentOrReturnOfFieldWithMutableType
        return description;
    }

    final Map<String, Object> toMap(final T source) {
        return accessors.entrySet().stream()
                        .collect(LinkedHashMap::new,
                              (map, entry) -> map.put(entry.getKey(), apply(source, entry.getValue())),
                                 Map::putAll);
    }

    private Object apply(final T source, final Method method) {
        try {
            return method.invoke(source);
        } catch (final IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(("cannot apply accessor ...%n" +
                                             "    record:   %s%n" +
                                             "    accessor: %s%n").formatted(source, method), e);
        }
    }

    final T toRecord(final Map<String, Object> map) {
        final Object[] arguments = description.keySet().stream()
                                              .map(map::get)
                                              .toArray(Object[]::new);
        try {
            return constructor.newInstance(arguments);
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(
                    ("cannot apply constructor ...%n" +
                     "    constructor: %s%n" +
                     "    arguments:   %s%n").formatted(constructor, List.of(arguments)), e);
        }
    }
}
