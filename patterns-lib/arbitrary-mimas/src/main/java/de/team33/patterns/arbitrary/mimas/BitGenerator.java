package de.team33.patterns.arbitrary.mimas;

import java.math.BigInteger;

@SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
@FunctionalInterface
interface BitGenerator {

    /**
     * Returns any non-negative {@link BigInteger} representing a sequence of significant bits of a given length.
     * In other words, the result is any value between zero (inclusive) and 2<sup>length</sup> (exclusive).
     * <p>
     * A typical implementation will return an arbitrary value within the defined bounds, with each possible value
     * being equally probable.
     */
    BigInteger anyBits(final int numBits);
}
