package de.team33.patterns.json.charon;

import de.team33.patterns.collection.mneme.FinalList;
import de.team33.patterns.collection.mneme.FinalMap;
import de.team33.patterns.collection.mneme.ImmutableList;
import de.team33.patterns.collection.mneme.ImmutableMap;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class Json {

    /**
     * A singleton representation of JSON {@code null} as regular {@link JsonValue}.
     */
    public static final JsonValue NULL = Null.INSTANCE;

    private Json() {}

    /**
     * Returns a {@link JsonValue} of {@link JsonValue#type()} {@link JsonType#BOOLEAN}
     * from the given {@code boolean} <em>value</em>.
     */
    @SuppressWarnings("BooleanParameter")
    public static JsonValue valueOf(final boolean value) {
        return new Boolean(value);
    }

    /**
     * Returns a {@link JsonValue} of {@link JsonValue#type()} {@link JsonType#NUMBER}
     * from the given {@link BigDecimal} <em>value</em>.
     *
     * @throws NullPointerException if <em>value</em> is {@code null}.
     */
    public static JsonValue valueOf(final BigDecimal value) {
        return new Number(Objects.requireNonNull(value, "value is null"));
    }

    /**
     * Returns a {@link JsonValue} of {@link JsonValue#type()} {@link JsonType#STRING}
     * from the given {@link CharSequence} <em>value</em>.
     *
     * @throws NullPointerException if <em>value</em> is {@code null}.
     */
    public static JsonValue valueOf(final CharSequence value) {
        return new JString(value.toString());
    }

    /**
     * Returns a {@link JsonValue} of {@link JsonValue#type()} {@link JsonType#ARRAY}
     * from the given {@link Collection} <em>value</em>.
     *
     * @throws NullPointerException if <em>value</em> is {@code null}.
     */
    public static JsonValue valueOf(final Collection<? extends JsonValue> value) {
        return new Array(FinalList.of(value));
    }

    /**
     * Returns a {@link JsonValue} of {@link JsonValue#type()} {@link JsonType#OBJECT}
     * from the given {@link Map} <em>value</em>.
     *
     * @throws NullPointerException if <em>value</em> is {@code null}.
     */
    public static JsonValue valueOf(final Map<String, ? extends JsonValue> value) {
        return new JObject(FinalMap.of(value));
    }

    /**
     * Returns a {@link JsonValue} that is parsed from a given JSON <em>source</em>.
     */
    public static JsonValue parse(final String source) {
        return new Parser(source).parse();
    }

    /**
     * Returns a JSON string from a given {@link JsonValue} applying the given <em>options</em>.
     */
    public static String render(final JsonValue source, final Set<RenderOption> options) {
        return new Renderer(options).render(source, 0)
                                    .toString();
    }

    private enum Null implements JsonValue {

        INSTANCE;

        @Override
        public JsonType type() {
            return JsonType.NULL;
        }

        @Override
        public String toString() {
            return "null";
        }
    }

    private record Boolean(boolean value) implements JsonValue {

        @Override
        public JsonType type() {
            return JsonType.BOOLEAN;
        }

        @Override
        public boolean asBoolean() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    private record Number(BigDecimal value) implements JsonValue {

        @Override
        public JsonType type() {
            return JsonType.NUMBER;
        }

        @Override
        public BigDecimal asNumber() {
            return value;
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }

    private record JString(String value) implements JsonValue {

        @Override
        public JsonType type() {
            return JsonType.STRING;
        }

        @Override
        public String asString() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private record Array(FinalList<JsonValue> value) implements JsonValue {

        @Override
        public JsonType type() {
            return JsonType.ARRAY;
        }

        @Override
        public ImmutableList<JsonValue> asArray() {
            // Already is immutable ...
            // noinspection AssignmentOrReturnOfFieldWithMutableType
            return value;
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }

    private record JObject(FinalMap<String, JsonValue> value) implements JsonValue {

        @Override
        public JsonType type() {
            return JsonType.OBJECT;
        }

        @Override
        public ImmutableMap<String, JsonValue> asObject() {
            // Already is immutable ...
            // noinspection AssignmentOrReturnOfFieldWithMutableType
            return value;
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }
}
