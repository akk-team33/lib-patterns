package de.team33.patterns.normal.iocaste.testing;

import de.team33.patterns.random.tarvos.Generator;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class Supply implements Generator {

    private static final Random RANDOM = new SecureRandom();
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyz-ABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789";

    @Override
    public final BigInteger nextBits(final int numBits) {
        return new BigInteger(numBits, RANDOM);
    }

    public final String nextString() {
        return nextString(nextInt(25), CHARS);
    }
}
