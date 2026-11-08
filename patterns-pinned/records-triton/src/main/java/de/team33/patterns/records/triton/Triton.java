package de.team33.patterns.records.triton;

import de.team33.patterns.collection.mneme.FinalList;
import de.team33.patterns.records.metis.Metis;
import de.team33.patterns.typing.proteus.Type;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;

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
     * @deprecated use {@link Metis#toMap(Record)} instead.
     */
    @Deprecated
    public static Map<String, Object> toMap(final Record source) {
        return Metis.toMap(source);
    }

    /**
     * @deprecated use {@link Metis#toRecord(Class, Map)} or {@link Metis#toRecord(Type, Map)} instead.
     */
    @Deprecated
    public static <T extends Record> T toRecord(final Class<T> recordType, final Map<String, Object> map) {
        return Metis.toRecord(recordType, map);
    }

    /**
     * @deprecated use {@link Metis#description(Class)} or {@link Metis#description(Type)} instead.
     */
    @Deprecated
    public static <T extends Record> Descriptor<T> descriptor(final Class<T> recordType) {
        return new DescriptorImpl<>(recordType, Metis.description(recordType));
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

    @Deprecated
    private static final class DescriptorImpl<T extends Record> implements Descriptor<T> {

        private final Class<T> recordType;
        private final Map<String, Class<?>> description;
        private final FinalList<String> names;

        private DescriptorImpl(final Class<T> recordType, final Map<String, Class<?>> description) {
            this.recordType = recordType;
            this.description = description;
            this.names = FinalList.of(description.keySet());
        }

        @Override
        public final Class<T> recordType() {
            return recordType;
        }

        @Override
        public final List<String> names() {
            // Already is immutable ...
            // noinspection AssignmentOrReturnOfFieldWithMutableType
            return names;
        }

        @Override
        public final Class<?> type(final String name) {
            return description.get(name);
        }
    }
}
