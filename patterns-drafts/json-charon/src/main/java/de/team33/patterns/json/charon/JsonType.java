package de.team33.patterns.json.charon;

/**
 * Identifies the type of JSON value.
 */
public enum JsonType {

    /**
     * Identifies JSON null.
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
