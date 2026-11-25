package de.team33.patterns.json.charon;

import de.team33.patterns.collection.mneme.FinalSet;
import de.team33.patterns.streamable.galatea.Buffer;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * Provides factory methods for JSON values as well as methods for parsing and rendering JSON.
 */
public final class Json {

    /**
     * Represents JSON {@code null} - a {@link Value} that {@link Value#isNull() isNull()}.
     */
    public static final Value NULL = JsonNull.INSTANCE;

    private Json() {}

    private static String typeMismatch(final Type expected, final Value actual) {
        return "This JSON value is not %s - but %s".formatted(expected, actual.type());
    }

    /**
     * Returns a {@link Value} that {@link Value#isBoolean() isBoolean()}
     * representing the given {@link Boolean} <em>value</em>.
     *
     * @throws NullPointerException if <em>value</em> is {@code null}
     */
    public static Value value(final Boolean value) {
        return new JsonBoolean(value);
    }

    /**
     * Returns a {@link Value} that {@link Value#isNumber() isNumber()}
     * representing the given {@link BigDecimal} <em>value</em>.
     *
     * @throws NullPointerException if <em>value</em> is {@code null}
     */
    public static Value value(final BigDecimal value) {
        return new JsonNumber(value);
    }

    /**
     * Returns a {@link Value} that {@link Value#isString() isString()}
     * representing the given {@link String} <em>value</em>.
     *
     * @throws NullPointerException if <em>value</em> is {@code null}
     */
    public static Value value(final String value) {
        return new JsonString(value);
    }

    /**
     * Returns a {@link Value} that {@link Value#isArray() isArray()}
     * representing the given {@link Collection} <em>value</em>.
     *
     * @throws NullPointerException if <em>value</em> is {@code null}
     */
    public static Value value(final Collection<? extends Value> value) {
        return new JsonArray(value);
    }

    /**
     * Returns a {@link Value} that {@link Value#isObject() isObject()}
     * representing the given {@link Map} <em>value</em>.
     *
     * @throws NullPointerException if <em>value</em> is {@code null}
     */
    public static Value value(final Map<String, ? extends Value> value) {
        return new JsonObject(value);
    }

    /**
     * Returns a {@link Collector} to {@linkplain Stream#collect(Collector) collect} {@link Value}s
     * into a new {@link Value} that {@link Value#isArray() isArray()}.
     */
    public static Collector<Value, ?, Value> arrayCollector() {
        return Buffer.collector(JsonArray::new);
    }

    /**
     * Returns a {@link Collector} to {@linkplain Stream#collect(Collector) collect} {@link Map.Entry}s
     * into a new {@link Value} that {@link Value#isObject() isObject()}.
     */
    public static Collector<Map.Entry<String, ? extends Value>, ?, Value> objectCollector() {
        return Buffer.collector(JsonObject::new);
    }

    /**
     * Renders the given JSON <em>value</em> using the given <em>options</em> and
     * returns the rendered JSON text.
     */
    public static String render(final Value value, final Option... options) {
        return render(value, FinalSet.of(options));
    }

    /**
     * Renders the given JSON <em>value</em> using the given <em>options</em> and
     * returns the rendered JSON text.
     */
    public static String render(final Value value, final Set<Option> options) {
        return new Renderer(options).render(value, 0)
                                    .toString();
    }

    /**
     * Returns a {@linkplain Value JSON value} parsed from the given JSON <em>source</em>.
     */
    public static Value parse(final String source) {
        return new Parser(source).parse();
    }

    /**
     * Identifies the type of JSON value.
     */
    public enum Type {

        /**
         * Identifies a JSON null.
         */
        @SuppressWarnings("InnerClassFieldHidesOuterClassField")
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
        OBJECT
    }

    /**
     * Defines options for JSON rendering.
     */
    public enum Option {

        /**
         * Causes JSON null values to be omitted when rendering JSON objects.
         *
         * <p>By default, JSON null values are rendered.
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

    /**
     * Represents a JSON value.
     */
    @SuppressWarnings("unused")
    public sealed interface Value permits JsonItem, JsonNull {

        /**
         * Returns the {@link Type} of <em>this</em> JSON value.
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
         * Returns the {@link Boolean} that represents <em>this</em> JSON value
         * if it {@linkplain #isBoolean() is a JSON boolean}.
         *
         * @throws UnsupportedOperationException if <em>this</em> JSON value is <b>not</b>
         *                                       {@linkplain #isBoolean() a JSON boolean}.
         */
        default Boolean asBoolean() {
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
}
