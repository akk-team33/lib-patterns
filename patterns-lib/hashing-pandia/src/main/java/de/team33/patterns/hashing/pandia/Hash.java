package de.team33.patterns.hashing.pandia;

import de.team33.patterns.lazy.narvi.Lazy;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Represents a hash value.
 */
@SuppressWarnings("WeakerAccess")
public class Hash {

    private final byte[] bytes;
    private final transient Lazy<BigInteger> lazyBigInteger = Lazy.init(this::newBigInteger);

    /**
     * @param bytes CAUTION: will not be cloned!
     */
    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    Hash(final byte[] bytes) {
        if ((null != bytes) && (0 < bytes.length)) {
            this.bytes = bytes;
        } else {
            final String found = (null == bytes) ? null : "empty";
            final String message = "the given byte array is expected not to be empty - but was " + found;
            throw new IllegalArgumentException(message);
        }
    }

    private BigInteger newBigInteger() {
        return new BigInteger(1, bytes);
    }

    /**
     * Returns the byte array representing this hash value.
     */
    public final byte[] bytes() {
        return bytes.clone();
    }

    /**
     * Returns a non-negative {@link BigInteger} representing this hash value.
     */
    public final BigInteger toBigInteger() {
        return lazyBigInteger.get();
    }

    /**
     * Returns a hexadecimal {@link String} representation of this hash value.
     */
    public final String toHexString() {
        return toString("0123456789abcdef");
    }

    /**
     * Returns a {@link String} representation of this hash value using the given <em>chars</em> as digits.
     */
    public final String toString(final String chars) {
        final BigInteger base = BigInteger.valueOf(chars.length());
        final StringBuilder result = new StringBuilder(0);
        for (BigInteger tail = toBigInteger(); BigInteger.ZERO.compareTo(tail) != 0; tail = tail.divide(base)) {
            final int digit = tail.mod(base).intValue();
            result.insert(0, chars.charAt(digit));
        }
        return result.isEmpty() ? chars.substring(0, 1) : result.toString();
    }

    @Override
    public final String toString() {
        return Arrays.toString(bytes);
    }

    @Override
    public final int hashCode() {
        return Arrays.hashCode(bytes);
    }

    @Override
    public final boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof final Hash other) && Arrays.equals(bytes, other.bytes));
    }
}
