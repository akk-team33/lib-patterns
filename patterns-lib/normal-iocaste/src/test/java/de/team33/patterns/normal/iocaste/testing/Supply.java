package de.team33.patterns.normal.iocaste.testing;

import de.team33.patterns.random.tarvos.Generator;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.Stream;

public class Supply implements Generator {

    private static final Random RANDOM = new SecureRandom();
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyz-ABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789";

    @Override
    public final BigInteger nextBits(final int numBits) {
        return new BigInteger(numBits, RANDOM);
    }

    public final char nextChar() {
        return nextChar(CHARS);
    }

    public final String nextString() {
        return nextString(nextInt(25), CHARS);
    }

    public String[] nextStringArray() {
        return Stream.generate(this::nextString)
                     .limit(nextInt(1, 8))
                     .toArray(String[]::new);
    }
}
