package de.team33.patterns.records.metis;

import de.team33.patterns.typing.proteus.Type;

import java.util.Map;

/**
 * A utility that provides conversions between {@linkplain java.lang.Record Records}
 * and {@linkplain java.util.Map Map} representations.
 */
public final class Metis {

    private Metis() {}

    /**
     * Returns an immutable {@link Map} describing the given <em>recordClass</em>.
     * <table>
     *     <caption>Each entry of the {@link Map} describes a single record component as follows:</caption>
     *     <tr>
     *         <th>key</th>
     *         <th>value</th>
     *     </tr>
     *     <tr>
     *         <td>{@link String}</td>
     *         <td>{@link Class}{@code <?>}</td>
     *     </tr>
     *     <tr>
     *         <td>The name of the record component</td>
     *         <td>The type of the record component</td>
     *     </tr>
     * </table>
     */
    public static <T extends Record> Map<String, Class<?>> description(final Class<T> recordClass) {
        return Reflector.of(recordClass).description();
    }

    /**
     * Returns an immutable {@link Map} describing the given <em>recordType</em>.
     * <table>
     *     <caption>Each entry of the {@link Map} describes a single record component as follows:</caption>
     *     <tr>
     *         <th>key</th>
     *         <th>value</th>
     *     </tr>
     *     <tr>
     *         <td>{@link String}</td>
     *         <td>{@link Type}{@code <?>}</td>
     *     </tr>
     *     <tr>
     *         <td>The name of the record component</td>
     *         <td>The type of the record component</td>
     *     </tr>
     * </table>
     */
    public static <T extends Record> Map<String, Type<?>> description(final Type<T> recordType) {
        return Refractor.of(recordType).description();
    }

    /**
     * Returns a {@link Map} containing the component values of the given <em>source</em>.
     * <p>
     * The returned {@link Map} is independent of the supplied record instance,
     * preserves the declaration order of the record components and is mutable.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map<String, Object> toMap(final Record source) {
        return Reflector.of((Class) source.getClass())
                        .toMap(source);
    }

    /**
     * Returns a new instance of the given <em>recordClass</em>, mapped from the given <em>source</em>.
     * <p>
     * Missing component values are treated as {@code null}. This may fail for primitive record components.
     * Source values for unknown record components are ignored.
     *
     * @param <T> The record type.
     */
    public static <T extends Record> T toRecord(final Class<T> recordClass, final Map<String, Object> source) {
        return Reflector.of(recordClass)
                        .toRecord(source);
    }

    /**
     * Returns a new instance of the given <em>recordType</em>, mapped from the given <em>source</em>.
     * <p>
     * Missing component values are treated as {@code null}. This may fail for primitive record components.
     * Source values for unknown record components are ignored.
     *
     * @param <T> The record type.
     */
    public static <T extends Record> T toRecord(final Type<T> recordType, final Map<String, Object> source) {
        return Refractor.of(recordType)
                        .toRecord(source);
    }
}
