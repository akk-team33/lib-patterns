package de.team33.patterns.normal.iocaste;

/**
 * Represents a normalized data object.
 */
public abstract class Normal {

    /**
     * Returns the {@link Type} of <em>this</em> instance.
     */
    public abstract Type type();

    /**
     * Represents the type of {@link Normal normalized data object}.
     */
    public enum Type {

        SIMPLE,

        SEQUENCE,

        COMPOUND;
    }
}
