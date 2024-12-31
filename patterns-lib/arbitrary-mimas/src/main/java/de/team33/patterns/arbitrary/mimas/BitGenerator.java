package de.team33.patterns.arbitrary.mimas;

import java.math.BigInteger;

interface BitGenerator {

    /**
     * Returns any non-negative {@link BigInteger} that represents a sequence of a specified number of significant bits.
     * In other words, the result is any value between zero (inclusive) and 2<sup><em>numBits</em></sup> (exclusive).
     * <p>
     * A typical implementation will return an arbitrary value within the defined bounds, with each possible value
     * being equally probable.
     *
     * @param numBits The intended number of significant bits.
     */
    BigInteger anyBits(final int numBits);
}
