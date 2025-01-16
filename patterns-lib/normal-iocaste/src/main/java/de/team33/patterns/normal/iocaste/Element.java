package de.team33.patterns.normal.iocaste;

import java.util.regex.Pattern;

/**
 * Represents a normalized elementary value object.
 */
@SuppressWarnings("unused")
public class Element extends Value {

    private static final Pattern INT_PATTERN = Pattern.compile("[+-]?\\d+");

    private final String value;

    private Element(final String value) {
        this.value = value;
    }

    public static Element of(final String value) {
        return new Element(value);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation always returns {@link Type#ELEMENT}.
     */
    @Override
    public final Type type() {
        return Type.ELEMENT;
    }

    @Override
    public final Element asElement() {
        return this;
    }

    /**
     * Determines if <em>this</em> element can be interpreted as an integer.
     */
    public final boolean isInteger() {
        return INT_PATTERN.matcher(value).matches();
    }

    /**
     * Interprets <em>this</em> element as an integer if {@link #isInteger() possible}.
     *
     * @throws UnsupportedOperationException if <em>not</em> {@link #isInteger()}.
     */
    public final int asInteger() {
        return Integer.parseInt(value);
    }
}
