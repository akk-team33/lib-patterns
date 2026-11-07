package de.team33.patterns.json.charon;

import de.team33.patterns.collection.mneme.ImmutableList;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * Represents a JSON value.
 * <p>
 * Use one of {@link Json#NULL},  {@link Json#valueOf(boolean)}, {@link Json#valueOf(BigDecimal)},
 * {@link Json#valueOf(CharSequence)}, {@link Json#valueOf(Collection)}, {@link Json#valueOf(Map)}
 * to get an instance.
 */
public interface JsonValue {

    /**
     * Returns the {@link JsonType} of <em>this</em> {@link JsonValue}.
     */
    JsonType type();

    /**
     * Returns the {@code boolean} value represented by <em>this</em> {@link JsonValue}
     * if its {@link #type()} is {@link JsonType#BOOLEAN}.
     *
     * @throws UnsupportedOperationException if {@link #type()} is not {@link JsonType#BOOLEAN}.
     */
    default boolean asBoolean() {
        throw new UnsupportedOperationException("type is not BOOLEAN");
    }

    /**
     * Returns the {@link BigDecimal} value represented by <em>this</em> {@link JsonValue}
     * if its {@link #type()} is {@link JsonType#NUMBER}.
     *
     * @throws UnsupportedOperationException if {@link #type()} is not {@link JsonType#NUMBER}.
     */
    default BigDecimal asNumber() {
        throw new UnsupportedOperationException("type is not NUMBER");
    }

    /**
     * Returns the {@link String} value represented by <em>this</em> {@link JsonValue}
     * if its {@link #type()} is {@link JsonType#STRING}.
     *
     * @throws UnsupportedOperationException if {@link #type()} is not {@link JsonType#STRING}.
     */
    default String asString() {
        throw new UnsupportedOperationException("type is not STRING");
    }

    /**
     * Returns the {@link ImmutableList} value represented by <em>this</em> {@link JsonValue}
     * if its {@link #type()} is {@link JsonType#ARRAY}.
     *
     * @throws UnsupportedOperationException if {@link #type()} is not {@link JsonType#ARRAY}.
     */
    default ImmutableList<JsonValue> asArray() {
        throw new UnsupportedOperationException("type is not ARRAY");
    }
}
