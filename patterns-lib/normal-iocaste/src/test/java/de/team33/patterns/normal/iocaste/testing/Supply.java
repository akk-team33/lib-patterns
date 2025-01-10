package de.team33.patterns.normal.iocaste.testing;

import de.team33.patterns.arbitrary.mimas.Generator;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class Supply implements Generator {

    private static final Random RANDOM = new SecureRandom();

    @Override
    public final BigInteger anyBits(final int numBits) {
        return new BigInteger(numBits, RANDOM);
    }
}
