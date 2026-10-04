package de.team33.patterns.arbitrary.mimas.sample;

import de.team33.patterns.arbitrary.mimas.Generator;

import java.math.BigInteger;
import java.security.SecureRandom;

public class FixedGenerator implements Generator {

    private final BigInteger value = new BigInteger(256, new SecureRandom());

    @Override
    public BigInteger anyBits(final int numBits) {
        final BigInteger mask = BigInteger.ONE.shiftLeft(numBits).subtract(BigInteger.ONE);
        return value.and(mask);
    }
}
