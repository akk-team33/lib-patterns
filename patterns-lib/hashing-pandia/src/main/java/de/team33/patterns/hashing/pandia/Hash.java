package de.team33.patterns.hashing.pandia;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;

/**
 * Represents a hash value.
 */
public class Hash {

    private final byte[] bytes;

    Hash(final byte[] bytes) {
        this.bytes = Optional.ofNullable(bytes)
                             .filter(array -> 0 < array.length)
                             .orElseThrow(() -> new IllegalArgumentException(
                                     "the given byte array is expected not to be empty - but was " +
                                     Optional.ofNullable(bytes)
                                             .map(Arrays::toString)
                                             .orElse(null)));
    }

    /**
     * Returns the underlying byte array of this hash value.
     */
    public final byte[] bytes() {
        return bytes.clone();
    }

    public final BigInteger toBigInteger() {
        return new BigInteger(bytes);
    }
}
