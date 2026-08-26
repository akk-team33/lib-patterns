package de.team33.patterns.records.rho;

import de.team33.patterns.typing.proteus.Type;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.UnaryOperator;

import static de.team33.patterns.records.rho.Util.typeName;

/**
 * A utility to convert between:
 * <ul>
 *     <li>{@linkplain Record Records} and JSON-formatted {@linkplain String Strings}</li>
 *     <li>{@linkplain Record Records} and {@linkplain Map Map} representations</li>
 * </ul>
 *
 * @see de.team33.patterns.records.triton package
 */
public final class Triton {

    private static final Map<Class<?>, ReflectorCore> CACHE = new ConcurrentHashMap<>();
    private static final RenderOption[] EMPTY_OPTIONS = {};

    private Triton() {
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
        return toRecord(Type.of(recordType), json);
    }

    /**
     * Returns a new instance of the given <em>recordType</em>, parsed from the given <em>json</em> {@link String}.
     *
     * @param <T> The record type.
     * @see de.team33.patterns.records.triton package
     */
    @SuppressWarnings("unchecked")
    public static <T extends Record> T toRecord(final Type<T> recordType, final String json) {
        final JsonValue value = Parser.parse(json);
        final Object result = Resolver.resolve(recordType, value);
        return (T) result;
    }

    /**
     * Returns a {@link Map} containing the component values of the given <em>source</em>.
     * <p>
     * The returned {@link Map} is independent of the supplied record instance,
     * preserves the declaration order of the record components and is mutable.
     *
     * @see de.team33.patterns.records.triton package
     */
    @SuppressWarnings("rawtypes")
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
        return toRecord(Type.of(recordType), map);
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
    public static <T extends Record> T toRecord(final Type<T> recordType, final Map<String, Object> map) {
        return new Reflector<>(recordType).toRecord(map);
    }

    /**
     * Returns a {@link Descriptor} describing the given <em>recordType</em>.
     *
     * @deprecated consider {@link #description(Class)} or {@link #description(Type)} as a replacement.
     */
    @Deprecated
    public static <T extends Record> Descriptor<T> descriptor(final Class<T> recordType) {
        return new DescriptorImpl<>(recordType);
    }

    /**
     * Returns a {@link Description} describing the given <em>recordType</em>.
     */
    public static <T extends Record> Description<T> description(final Class<T> recordType) {
        return description(Type.of(recordType));
    }

    /**
     * Returns a {@link Description} describing the given <em>recordType</em>.
     */
    public static <T extends Record> Description<T> description(final Type<T> recordType) {
        return new Reflector<>(recordType);
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

    private static ReflectorCore reflector(final Class<?> recordClass) {
        return CACHE.computeIfAbsent(recordClass, ReflectorCore::new);
    }

    private static final class ReflectorCore {

        private final List<RecordComponent> components;
        private final List<String> names;
        private final List<Method> methods;
        private final Constructor<?> constructor;

        private ReflectorCore(final Class<?> type) {
            this.components = List.of(type.getRecordComponents());
            this.names = components.stream()
                                   .map(RecordComponent::getName)
                                   .toList();
            this.methods = components.stream()
                                     .map(RecordComponent::getAccessor)
                                     .peek(method -> method.setAccessible(true))
                                     .toList();
            final Class<?>[] types = components.stream()
                                               .map(RecordComponent::getType)
                                               .toArray(Class<?>[]::new);
            try {
                this.constructor = type.getDeclaredConstructor(types);
                this.constructor.setAccessible(true);
            } catch (final NoSuchMethodException e) {
                // difficult to test (should not happen at all) ...
                throw new IllegalArgumentException(("Cannot find constructor ..." +
                                                    "    type: %s%n" +
                                                    "    args: %s%n").formatted(type, List.of(types)), e);
            }
        }

        final Constructor<?> constructor() {
            return constructor;
        }

        final List<String> names() {
            return names;
        }

        final int indexOf(final String name) {
            return names().indexOf(name);
        }

        final RecordComponent component(final String name) {
            return components.get(indexOf(name));
        }

        final Map<String, Object> toMap(final Record source) {
            return names.stream()
                        .collect(LinkedHashMap::new, (map, name) -> put(map, source, name), Map::putAll);
        }

        private void put(final Map<? super String, Object> map, final Record source, final String name) {
            map.put(name, get(source, name));
        }

        private Object get(final Record source, final String name) {
            try {
                return methods.get(indexOf(name))
                              .invoke(source);
            } catch (final IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException(("Cannot access component <%s>%n" +
                                                 "    source : %s%n" +
                                                 "    type   : %s%n").formatted(name, source, typeName(source)), e);
            }
        }
    }

    private static final class Reflector<T extends Record> implements Description<T> {

        private final Type<T> type;
        private final ReflectorCore core;

        private Reflector(final Type<T> recordType) {
            this.type = recordType;
            this.core = reflector(recordType.core());
        }

        @Override
        public final Type<T> type() {
            return type;
        }

        @Override
        public final List<String> names() {
            return core.names();
        }

        @Override
        public final Type<?> componentType(final String name) {
            return type.typeOf(core.component(name));
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        final T toRecord(final Map<String, Object> source) {
            final Object[] args = core.names().stream()
                                      .map(source::get)
                                      .toArray(Object[]::new);
            final Constructor constructor = core.constructor();
            try {
                return (T) constructor.newInstance(args);
            } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException(("Cannot apply constructor:%n" +
                                                 "     %s%n" +
                                                 "     %s%n").formatted(constructor, List.of(args)), e);
            }
        }
    }

    @Deprecated
    private static final class DescriptorImpl<T extends Record> implements Descriptor<T> {

        private final Class<T> recordType;
        private final ReflectorCore core;

        private DescriptorImpl(final Class<T> recordType) {
            this.recordType = recordType;
            this.core = reflector(recordType);
        }

        @Override
        public final Class<T> recordType() {
            return recordType;
        }

        @Override
        public final List<String> names() {
            return core.names();
        }

        @Override
        public final Class<?> type(final String name) {
            return core.component(name).getType();
        }
    }
}
