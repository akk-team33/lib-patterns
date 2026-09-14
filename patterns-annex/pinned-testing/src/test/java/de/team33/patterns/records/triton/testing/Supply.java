package de.team33.patterns.records.triton.testing;

import de.team33.patterns.arbitrary.mimas.Generator;

import java.math.BigInteger;
import java.security.SecureRandom;

public class Supply implements Generator {

    private final SecureRandom random = new SecureRandom();

    @Override
    public final BigInteger anyBits(final int numBits) {
        return new BigInteger(numBits, random);
    }
}
