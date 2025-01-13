package de.team33.patterns.normal.iocaste;

/**
 * Represents a normalized value object (aka data object).
 */
public abstract class Value {

    /**
     * Returns the {@link Type} of <em>this</em> value.
     */
    public abstract Type type();

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
