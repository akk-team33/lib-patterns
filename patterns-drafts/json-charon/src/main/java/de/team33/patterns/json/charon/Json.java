package de.team33.patterns.json.charon;

import de.team33.patterns.collection.mneme.FinalList;
import de.team33.patterns.collection.mneme.ImmutableList;

import java.math.BigDecimal;
import java.util.Collection;

public class Json {

    /**
     * A singleton representation of JSON null.
     */
    public static final JsonValue NULL = Null.INSTANCE;

    /**
     * Returns a {@link JsonValue} of {@link JsonValue#type()} {@link JsonType#BOOLEAN}
     * from the given {@code boolean} <em>value</em>.
     */
    public static JsonValue valueOf(final boolean value) {
        return new Boolean(value);
    }

    /**
     * Returns a {@link JsonValue} of {@link JsonValue#type()} {@link JsonType#NUMBER}
     * from the given {@link BigDecimal} <em>value</em>.
     */
    public static JsonValue valueOf(final BigDecimal value) {
        return new Number(value);
    }

    /**
     * Returns a {@link JsonValue} of {@link JsonValue#type()} {@link JsonType#STRING}
     * from the given {@link CharSequence} <em>value</em>.
     */
    public static JsonValue valueOf(final CharSequence value) {
        return new JString(value.toString());
    }

    /**
     * Returns a {@link JsonValue} of {@link JsonValue#type()} {@link JsonType#STRING}
     * from the given {@link CharSequence} <em>value</em>.
     */
    public static JsonValue valueOf(final Collection<? extends JsonValue> value) {
        return new Array(FinalList.of(value));
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
}
