package de.team33.patterns.normal.iocaste;

/**
 * Represents a normalized value object (aka data object).
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class Value {

    private static final String NOT_SUPPORTED = "not supported for values of type ";


    /**
     * Returns the {@link Type} of <em>this</em> value.
     */
    public abstract Type type();

    /**
     * Determines if <em>this</em> value is an elementary value.
     */
    public final boolean isElement() {
        return Type.ELEMENT == type();
    }

    /**
     * Returns <em>this</em> as an instance of {@link Element} if {@link #isElement()}.
     *
     * @throws UnsupportedOperationException if <em>not</em> {@link #isElement()}.
     */
    @SuppressWarnings("DesignForExtension")
    public Element asElement() {
        throw new UnsupportedOperationException(NOT_SUPPORTED + type());
    }

    /**
     * Determines if <em>this</em> value is a sequence of multiple values.
     */
    public final boolean isSequence() {
        return Type.SEQUENCE == type();
    }

    /**
     * Returns <em>this</em> as an instance of {@link Sequence} if {@link #isSequence()}.
     *
     * @throws UnsupportedOperationException if <em>not</em> {@link #isSequence()}.
     */
    @SuppressWarnings("DesignForExtension")
    public Sequence asSequence() {
        throw new UnsupportedOperationException(NOT_SUPPORTED + type());
    }

    /**
     * Determines if <em>this</em> value is a set of key-value pairs.
     */
    public final boolean isCompound() {
        return Type.COMPOUND == type();
    }

    /**
     * Returns <em>this</em> as an instance of {@link Compound} if {@link #isCompound()}.
     *
     * @throws UnsupportedOperationException if <em>not</em> {@link #isCompound()}.
     */
    @SuppressWarnings("DesignForExtension")
    public Compound asCompound() {
        throw new UnsupportedOperationException(NOT_SUPPORTED + type());
    }

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
        COMPOUND
    }
}
