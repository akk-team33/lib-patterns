package de.team33.patterns.json.charon;

/**
 * Represents a JSON value.
 */
public sealed interface JsonValue permits JsonBase, JsonNull {

    /**
     * Returns a java business representation of <em>this</em> {@link JsonValue}.
     */
    Object value();
}
