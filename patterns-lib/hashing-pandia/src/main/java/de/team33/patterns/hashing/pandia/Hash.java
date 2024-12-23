package de.team33.patterns.hashing.pandia;

import de.team33.patterns.lazy.narvi.Lazy;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Represents a hash value.
 */
public class Hash {

    private final byte[] bytes;
    private final transient Lazy<BigInteger> lazyBigInteger = Lazy.init(this::newBigInteger);

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
        final int length = bytes.length;
        final byte[] bytesEx = new byte[length + 1];
        System.arraycopy(bytes, 0, bytesEx, 1, length);
        return new BigInteger(bytesEx);
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
        final StringBuilder result = new StringBuilder();
        for (BigInteger tail = toBigInteger(); BigInteger.ZERO.compareTo(tail) != 0; tail = tail.divide(base)) {
            final int digit = tail.mod(base).intValue();
            result.insert(0, chars.charAt(digit));
        }
        return result.length() > 0 ? result.toString() : chars.substring(0, 1);
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
    public boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof Hash) && Arrays.equals(bytes, ((Hash) obj).bytes));
    }
}
