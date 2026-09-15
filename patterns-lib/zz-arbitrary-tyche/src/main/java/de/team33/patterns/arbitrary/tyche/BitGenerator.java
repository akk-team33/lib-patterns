package de.team33.patterns.arbitrary.tyche;

import java.math.BigInteger;

@SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
@FunctionalInterface
interface BitGenerator {

    /**
     * Returns an arbitrary non-negative {@link BigInteger} representing a set of <em>numBits</em> significant bits.
     * In other words, the result is a value between zero (inclusive) and 2<sup>length</sup> (exclusive).
     * <p>
     * A typical implementation will return a value within the defined bounds,
     * with each possible value being equally probable.
     */
    BigInteger anyBits(final int numBits);
}
