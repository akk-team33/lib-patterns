package de.team33.patterns.json.charon;

import de.team33.patterns.streamable.galatea.Buffer;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class Json {

    public static final Value NULL = JsonNull.INSTANCE;

    private static String typeMismatch(final Json.Type expected, final Json.Value actual) {
        return "<this> JSON value ist not %s - but %s".formatted(expected, actual.type());
    }

    /**
     * Returns a {@link Value} that {@link Value#isBoolean()}
     * and is represented by the given {@code boolean} <em>value</em>.
     */
    public static Value value(final boolean value) {
        return new JsonBoolean(value);
    }

    /**
     * Returns a {@link Value} that {@link Value#isNumber()}
     * and is represented by the given {@link BigDecimal} <em>value</em>.
     */
    public static Value value(final BigDecimal value) {
        return new JsonNumber(value);
    }

    /**
     * Returns a {@link Value} that {@link Value#isString()}
     * and is represented by the given {@link String} <em>value</em>.
     */
    public static Value value(final String value) {
        return new JsonString(value);
    }

    /**
     * Returns a {@link Value} that {@link Value#isArray()}
     * and is represented by the given {@link Collection} <em>value</em>.
     */
    public static Value value(final Collection<? extends Value> value) {
        return new JsonArray(value);
    }

    /**
     * Returns a {@link Value} that {@link Value#isObject()}
     * and is represented by the given {@link Map} <em>value</em>.
     */
    public static Value value(final Map<String, ? extends Value> value) {
        return new JsonObject(value);
    }

    /**
     * Returns a {@link Collector} to {@linkplain Stream#collect(Collector) collect} {@link Value}s
     * into a new {@link Value} that {@link Value#isArray()}.
     */
    public static Collector<Value, ?, Value> arrayCollector() {
        return Buffer.collector(JsonArray::new);
    }

    /**
     * Returns a {@link Collector} to {@linkplain Stream#collect(Collector) collect} {@link Map.Entry}s
     * into a new {@link Value} that {@link Value#isObject()}.
     */
    public static Collector<Map.Entry<String, ? extends Value>, ?, Value> objectCollector() {
        return Buffer.collector(JsonObject::new);
    }

    public static String render(final Value value, final Set<Option> options) {
        return new Renderer(options).add(value, 0)
                                    .toString();
    }

    public static Value parse(final String source) {
        return new Parser(source).parseRoot();
    }

    /**
     * Identifies the type of a JSON value.
     */
    public enum Type {

        /**
         * Identifies a JSON null.
         */
        NULL,

        /**
         * Identifies a JSON boolean.
         */
        BOOLEAN,

        /**
         * Identifies a JSON number.
         */
        NUMBER,

        /**
         * Identifies a JSON string.
         */
        STRING,

        /**
         * Identifies a JSON array.
         */
        ARRAY,

        /**
         * Identifies a JSON object.
         */
        OBJECT;
    }

    public interface Value {

        /**
         * Determines whether <em>this</em> is an instance of the given <em>type</em>.
         */
        default boolean is(final Class<? extends Value> type) {
            return type.isInstance(this);
        }

        /**
         * Returns <em>this</em> as an instance of the given <em>type</em>,
         * provided that it is indeed an instance of that type.
         *
         * @param <T> The expected type.
         * @throws ClassCastException if <em>this</em> is not an instance of the given <em>type</em>.
         * @see #is(Class)
         */
        default <T extends Value> T as(final Class<T> type) {
            return type.cast(this);
        }

        /**
         * Returns <em>this</em> as an {@linkplain Optional optional} instance of the given <em>type</em>.
         * <p>
         * Returns {@link Optional#empty()} if <em>this</em> is not an instance of the given <em>type</em>.
         *
         * @param <T> The expected type.
         */
        @SuppressWarnings("BoundedWildcard")
        default <T extends Value> Optional<T> asOptional(final Class<T> type) {
            return Optional.of(this)
                           .filter(type::isInstance)
                           .map(type::cast);
        }

        /**
         * Returns the {@link Type} of <em>this</em> value.
         */
        Type type();

        /**
         * Determines whether <em>this</em> value is JSON null.
         * <p>
         * This is the case if and only if {@link #type()} is {@link Type#NULL}.
         */
        default boolean isNull() {
            return Type.NULL == type();
        }

        /**
         * Determines whether <em>this</em> value is a JSON boolean.
         * <p>
         * This is the case if and only if {@link #type()} is {@link Type#BOOLEAN}.
         */
        default boolean isBoolean() {
            return Type.BOOLEAN == type();
        }

        /**
         * Determines whether <em>this</em> value is a JSON number.
         * <p>
         * This is the case if and only if {@link #type()} is {@link Type#NUMBER}.
         */
        default boolean isNumber() {
            return Type.NUMBER == type();
        }

        /**
         * Determines whether <em>this</em> value is a JSON string.
         * <p>
         * This is the case if and only if {@link #type()} is {@link Type#STRING}.
         */
        default boolean isString() {
            return Type.STRING == type();
        }

        /**
         * Determines whether <em>this</em> value is a JSON array.
         * <p>
         * This is the case if and only if {@link #type()} is {@link Type#ARRAY}.
         */
        default boolean isArray() {
            return Type.ARRAY == type();
        }

        /**
         * Determines whether <em>this</em> value is a JSON object.
         * <p>
         * This is the case if and only if {@link #type()} is {@link Type#OBJECT}.
         */
        default boolean isObject() {
            return Type.OBJECT == type();
        }

        /**
         * Returns the {@code boolean} that represents <em>this</em> JSON value
         * if it {@linkplain #isBoolean() is a JSON boolean}.
         *
         * @throws UnsupportedOperationException if <em>this</em> JSON value is <b>not</b>
         *                                       {@linkplain #isBoolean() a JSON boolean}.
         */
        default boolean asBoolean() {
            throw new UnsupportedOperationException(typeMismatch(Type.BOOLEAN, this));
        }

        /**
         * Returns the {@link BigDecimal} that represents <em>this</em> JSON value
         * if it {@linkplain #isNumber() is a JSON number}.
         *
         * @throws UnsupportedOperationException if <em>this</em> JSON value is <b>not</b>
         *                                       {@linkplain #isNumber() a JSON number}.
         */
        default BigDecimal asBigDecimal() {
            throw new UnsupportedOperationException(typeMismatch(Type.NUMBER, this));
        }

        /**
         * Returns the {@link String} that represents <em>this</em> JSON value
         * if it {@linkplain #isString() is a JSON string}.
         *
         * @throws UnsupportedOperationException if <em>this</em> JSON value is <b>not</b>
         *                                       {@linkplain #isString() a JSON string}.
         */
        default String asString() {
            throw new UnsupportedOperationException(typeMismatch(Type.STRING, this));
        }

        /**
         * Returns the (immutable) {@link List} that represents <em>this</em> JSON value
         * if it {@linkplain #isArray() is a JSON array}.
         *
         * @throws UnsupportedOperationException if <em>this</em> JSON value is <b>not</b>
         *                                       {@linkplain #isArray() a JSON array}.
         */
        default List<Value> asList() {
            throw new UnsupportedOperationException(typeMismatch(Type.ARRAY, this));
        }

        /**
         * Returns the (immutable) {@link Map} that represents <em>this</em> JSON value
         * if it {@linkplain #isObject() is a JSON object}.
         *
         * @throws UnsupportedOperationException if <em>this</em> JSON value is <b>not</b>
         *                                       {@linkplain #isObject() a JSON object}.
         */
        default Map<String, Value> asMap() {
            throw new UnsupportedOperationException(typeMismatch(Type.OBJECT, this));
        }
    }

    /**
     * Defines options for JSON rendering.
     */
    public enum Option {

        /**
         * Causes {@code null} values to be skipped during rendering of JSON objects.
         * <p>
         * By default, {@code null} values will be rendered.
         */
        SKIP_NULL,

        /**
         * Causes JSON objects to be rendered in a single line.
         * <p>
         * By default, JSON objects are rendered in multi-line format.
         */
        INLINE_OBJECT,

        /**
         * Causes JSON arrays to be rendered in multi-line format.
         * <p>
         * By default, JSON arrays are rendered in a single line.
         */
        FORMAT_ARRAY
    }
}
