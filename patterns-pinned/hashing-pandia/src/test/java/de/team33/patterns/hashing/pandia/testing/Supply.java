package de.team33.patterns.hashing.pandia.testing;

import de.team33.patterns.arbitrary.mimas.Generator;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class Supply implements Generator {

    private final Random random = new SecureRandom();

    @Override
    public final BigInteger anyBits(final int numBits) {
        return new BigInteger(numBits, random);
    }
}
