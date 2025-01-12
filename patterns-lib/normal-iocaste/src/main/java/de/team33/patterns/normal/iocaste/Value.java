package de.team33.patterns.normal.iocaste;

/**
 * Represents a normalized value object.
 */
public abstract class Value {

    /**
     * Symbolizes the type of {@link Value value object}.
     */
    public enum Type {

        /**
         * Symbolizes an elementary value object.
         */
        ELEMENT,

        /**
         * Symbolizes a value object consisting of multiple values.
         */
        SEQUENCE,

        /**
         * Symbolizes a value object consisting of key-value pairs.
         */
        COMPOUND;
    }
}
