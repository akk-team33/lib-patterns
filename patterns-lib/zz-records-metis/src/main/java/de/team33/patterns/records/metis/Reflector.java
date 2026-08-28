package de.team33.patterns.records.metis;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.util.Collections.unmodifiableMap;

final class Reflector<T extends Record> {

    @SuppressWarnings("rawtypes")
    private static final Map<Class, Reflector> CACHE = new ConcurrentHashMap<>();

    private final List<RecordComponent> components;
    private final Map<String, Class<?>> description;
    private final Map<String, Function<T, Object>> methods;
    private final Constructor<T> constructor;

    private Reflector(final Class<T> recordClass) {
        final Class<T> verified = recordClass.isRecord() ? recordClass : Util.fail(
                new IllegalArgumentException(("no record class ...%n" +
                                              "    given class: %s%n").formatted(recordClass)));
        this.components = List.of(verified.getRecordComponents());
        this.description = unmodifiableMap(newDescription(components));
        this.methods = unmodifiableMap(newMethods(components));
        this.constructor = constructor(verified, components);
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

    private static Map<String, Class<?>> newDescription(final List<RecordComponent> components) {
        return components.stream()
                         .collect(LinkedHashMap::new, Reflector::putType, Map::putAll);
    }

    private static void putType(final Map<? super String, ? super Class<?>> map, final RecordComponent component) {
        map.put(component.getName(), component.getType());
    }

    private static <T> Map<String, Function<T, Object>> newMethods(final List<RecordComponent> components) {
        return components.stream()
                         .collect(LinkedHashMap::new, Reflector::putMethod, Map::putAll);
    }

    private static <T> void putMethod(final Map<? super String, ? super Function<T, Object>> map,
                                      final RecordComponent component) {
        map.put(component.getName(), method(component.getAccessor()));
    }

    private static <T> Function<T, Object> method(final Method accessor) {
        accessor.setAccessible(true);
        return record -> {
            try {
                return accessor.invoke(record);
            } catch (final IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException(("cannot apply accessor ...%n" +
                                                 "    record:   %s%n" +
                                                 "    accessor: %s%n").formatted(record, accessor), e);
            }
        };
    }

    final List<RecordComponent> components() {
        return components;
    }

    final Map<String, Class<?>> description() {
        return description;
    }

    final Map<String, Object> toMap(final T sample) {
        return methods.entrySet().stream()
                      .collect(LinkedHashMap::new,
                               (map, entry) -> map.put(entry.getKey(), entry.getValue().apply(sample)),
                               Map::putAll);
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
