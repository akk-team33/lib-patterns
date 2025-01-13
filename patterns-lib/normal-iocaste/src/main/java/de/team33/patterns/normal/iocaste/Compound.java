package de.team33.patterns.normal.iocaste;

/**
 * Represents a normalized value object consisting of key-value pairs.
 */
public class Compound extends Value {

    /**
     * {@inheritDoc}
     * <p>
     * This implementation always returns {@link Type#COMPOUND}.
     */
    @Override
    public final Type type() {
        return Type.COMPOUND;
    }
}
