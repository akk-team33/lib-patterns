package de.team33.patterns.normal.iocaste;

/**
 * Represents a normalized value object consisting of multiple values.
 */
public class Sequence extends Value {

    /**
     * {@inheritDoc}
     * <p>
     * This implementation always returns {@link Type#SEQUENCE}.
     */
    @Override
    public final Type type() {
        return Type.SEQUENCE;
    }

    @Override
    public final Sequence asSequence() {
        return this;
    }
}
