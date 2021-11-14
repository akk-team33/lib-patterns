package de.team33.patterns.random.e1;

import java.math.BigInteger;
import java.util.Random;

/**
 * Represents a factory for bit sequences or bit patterns of variable length.
 */
@FunctionalInterface
public interface BitFactory {

    /**
     * Returns a BitFactory that uses a given {@link Random} instance to deliver a random result:
     * For every single significant bit in the result, both possible states (zero or one) are approximately
     * equally likely.
     */
    static BitFactory using(final Random random) {
        return numBits -> new BigInteger(numBits, random);
    }

    /**
     * Returns a non-negative {@link BigInteger} that represents a sequence of significant bits of a given length.
     * This means that these (least significant) bits in the result can each be zero or one,
     * all higher significant bits (if present) must be zero.
     */
    BigInteger anyBits(int numBits);
}
