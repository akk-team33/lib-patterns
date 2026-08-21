package de.team33.patterns.records.triton;

import de.team33.patterns.typing.theta.Type;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static de.team33.patterns.records.triton.Util.typeName;

/**
 * A utility to convert between:
 * <ul>
 *     <li>{@linkplain Record Records} and JSON-formatted {@linkplain String Strings}</li>
 *     <li>{@linkplain Record Records} and {@linkplain Map Map} representations</li>
 * </ul>
 *
 * @see de.team33.patterns.records.triton package
 */
public final class TritonX {

    @SuppressWarnings("rawtypes")
    private static final Map<Class, Reflector> CACHE = new ConcurrentHashMap<>();
    private static final RenderOption[] EMPTY_OPTIONS = {};

    private TritonX() {
    }

    /**
     * Returns a JSON-formatted {@link String} representation of the given <em>source</em>.
     *
     * @see de.team33.patterns.records.triton package
     */
    public static String toJson(final Record source) {
        return toJson(source, EMPTY_OPTIONS);
    }

    /**
     * Returns a JSON-formatted {@link String} representation of the given <em>source</em>
     * using given rendering <em>options</em>.
     *
     * @see RenderOption
     * @see de.team33.patterns.records.triton package
     */
    @SuppressWarnings("OverloadedVarargsMethod")
    public static String toJson(final Record source, final RenderOption... options) {
        final JsonValue value = Generalizer.map(source);
        return Renderer.render(value, Set.of(options));
    }

    /**
     * Returns a new instance of the given <em>recordType</em>, parsed from the given <em>json</em> {@link String}.
     *
     * @param <T> The record type.
     * @see de.team33.patterns.records.triton package
     */
    public static <T extends Record> T toRecord(final Class<T> recordType, final String json) {
        final JsonValue value = Parser.parse(json);
        final Object result = Resolver.resolve(recordType, value);
        return recordType.cast(result);
    }

    /**
     * Returns a {@link Map} containing the component values of the given <em>source</em>.
     * <p>
     * The returned {@link Map} is independent of the supplied record instance,
     * preserves the declaration order of the record components and is mutable.
     *
     * @see de.team33.patterns.records.triton package
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map<String, Object> toMap(final Record source) {
        final Class recordType = source.getClass();
        return reflector(recordType).toMap(source);
    }

    /**
     * Returns a new instance of the given <em>recordType</em>, mapped from the given <em>map</em>.
     * <p>
     * Missing component values are treated as {@code null}. This may fail for primitive record components.
     * Component values for unknown record components are ignored.
     *
     * @param <T> The record type.
     * @see de.team33.patterns.records.triton package
     */
    public static <T extends Record> T toRecord(final Class<T> recordType, final Map<String, Object> map) {
        return reflector(recordType).toRecord(map);
    }

    public static <T extends Record> T toRecord(final Type<T> recordType, final Map<String, Object> map) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns a {@link Descriptor} describing the given <em>recordType</em>.
     */
    @SuppressWarnings("WeakerAccess")
    public static <T extends Record> Descriptor<T> descriptor(final Class<T> recordType) {
        return reflector(recordType);
    }

    @SuppressWarnings("WeakerAccess")
    public static <T extends Record> Description<T> description(final Type<T> recordType) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Allows to customize the {@link Mapping} of a <em>'stringable'</em> type.
     * <p>
     * <b>NOTE</b> that you can only customize the mapping of a particular type as long as it has not been
     * previously applied or customized.
     *
     * @param type     the {@link Class} representation of the type whose {@link Mapping} is to be customized.
     * @param operator an {@linkplain UnaryOperator operator} that customizes a {@link Mapping}.
     * @param <T>      the type whose {@link Mapping} is to be customized.
     * @throws IllegalStateException if you try to customize the mapping of a particular type
     *                               after it has already been applied or customized.
     * @see de.team33.patterns.records.triton package
     */
    public static <T> void setup(final Class<T> type, final UnaryOperator<Mapping<T, String>> operator) {
        Stringable.setup(type, operator);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Record> Reflector<T> reflector(final Class<T> recordClass) {
        return CACHE.computeIfAbsent(recordClass, Reflector::new);
    }

    private static final class Reflector<T extends Record> implements Descriptor<T> {

        private final Map<String, Integer> indices;
        private final List<String> names;
        private final List<Method> methods;
        private final Class<?>[] types;
        private final Constructor<T> constructor;

        private Reflector(final Class<T> recordType) {
            final RecordComponent[] components = recordType.getRecordComponents();
            this.names = Stream.of(components)
                               .map(RecordComponent::getName)
                               .toList();
            this.methods = Stream.of(components)
                                 .map(RecordComponent::getAccessor)
                                 .peek(accessor -> accessor.setAccessible(true))
                                 .toList();
            this.types = Stream.of(components)
                               .map(RecordComponent::getType)
                               .toArray(Class<?>[]::new);
            this.indices = IntStream.range(0, names.size())
                                    .boxed()
                                    .collect(HashMap::new,
                                             (map, index) -> map.put(names.get(index), index),
                                             HashMap::putAll);
            try {
                this.constructor = recordType.getDeclaredConstructor(types);
                this.constructor.setAccessible(true);
            } catch (final NoSuchMethodException e) {
                // difficult to test (should not happen at all) ...
                throw new IllegalArgumentException("Cannot find constructor for %s%n".formatted(recordType), e);
            }
        }

        @Override
        public final Class<T> recordType() {
            return constructor.getDeclaringClass();
        }

        @Override
        public final List<String> names() {
            return names;
        }

        @Override
        public final Class<?> type(final String name) {
            return types[indexOf(name)];
        }

        private Object get(final Record source, final String name) {
            try {
                return methods.get(indexOf(name)).invoke(source);
            } catch (final IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException(("Cannot access component <%s>%n" +
                                                 "    source : %s%n" +
                                                 "    type   : %s%n").formatted(name, source, typeName(source)), e);
            }
        }

        private int indexOf(final String name) {
            return Optional.ofNullable(indices.get(name))
                           .orElseThrow(() -> new NoSuchElementException("Cannot find component <%s>%n".formatted(name)));
        }

        private T toRecord(final Map<String, Object> source) {
            final Object[] args = names.stream()
                                       .map(source::get)
                                       .toArray(Object[]::new);
            try {
                return constructor.newInstance(args);
            } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException(("Cannot apply constructor:%n" +
                                                 "     %s%n").formatted(constructor), e);
            }
        }

        private Map<String, Object> toMap(final T source) {
            return names.stream()
                        .collect(LinkedHashMap::new, (map, name) -> put(map, source, name), Map::putAll);
        }

        private void put(final Map<? super String, Object> map, final T source, final String name) {
            map.put(name, get(source, name));
        }
    }
}
