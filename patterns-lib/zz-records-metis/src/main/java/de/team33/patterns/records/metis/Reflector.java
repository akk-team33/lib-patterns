package de.team33.patterns.records.metis;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.unmodifiableMap;

final class Reflector<T extends Record> {

    @SuppressWarnings("rawtypes")
    private static final Map<Class, Reflector> CACHE = new ConcurrentHashMap<>();

    private final List<RecordComponent> components;
    private final Map<String, Class<?>> description;
    private final Map<String, Method> access;
    private final Constructor<T> constructor;

    private Reflector(final Class<T> recordClass) {
        if (!recordClass.isRecord()) {
            throw new IllegalArgumentException(("no record type:%n" +
                                                "    %s%n").formatted(recordClass));
        }
        this.components = List.of(recordClass.getRecordComponents());
        this.description = components.stream()
                                     .collect(Descriptor::new, Descriptor::put, Descriptor::putAll)
                                     .toMap();
        this.access = components.stream()
                                .collect(Accessor::new, Accessor::put, Accessor::putAll)
                                .toMap();
        this.constructor = constructor(recordClass, components);
    }

    @SuppressWarnings("unchecked")
    static <T extends Record> Reflector<T> of(final Class<T> recordClass) {
        return CACHE.computeIfAbsent(recordClass, Reflector::new);
    }

    private static <T> Constructor<T> constructor(final Class<T> recordClass,
                                                  final List<RecordComponent> components) {
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

    final List<RecordComponent> components() {
        return components;
    }

    final Map<String, Class<?>> description() {
        return description;
    }

    final Map<String, Object> toMap(final T source) {
        return access.entrySet().stream()
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

    private static final class Accessor {

        private final Map<String, Method> map = new LinkedHashMap<>();

        private void put(final RecordComponent component) {
            final Method method = component.getAccessor();
            method.setAccessible(true);
            map.put(component.getName(), method);
        }

        private void putAll(final Accessor other) {
            map.putAll(other.map);
        }

        private Map<String, Method> toMap() {
            return unmodifiableMap(map);
        }
    }

    private static final class Descriptor {

        private final Map<String, Class<?>> map = new LinkedHashMap<>();

        private void put(final RecordComponent component) {
            map.put(component.getName(), component.getType());
        }

        private void putAll(final Descriptor other) {
            map.putAll(other.map);
        }

        private Map<String, Class<?>> toMap() {
            return unmodifiableMap(map);
        }
    }
}
