package de.team33.patterns.normal.iocaste;

/**
 * Represents a normalized elementary value object.
 */
public class Element extends Value {

    /**
     * {@inheritDoc}
     * <p>
     * This implementation always returns {@link Type#ELEMENT}.
     */
    @Override
    public final Type type() {
        return Type.ELEMENT;
    }
}
