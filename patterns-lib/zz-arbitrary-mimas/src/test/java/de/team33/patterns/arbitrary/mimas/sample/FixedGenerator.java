package de.team33.patterns.arbitrary.mimas.sample;

import de.team33.patterns.arbitrary.mimas.Generator;

import java.math.BigInteger;
import java.security.SecureRandom;

public class FixedGenerator implements Generator {

    @SuppressWarnings("removal")
    private final BigInteger value = Generator.anyBits(256, new SecureRandom());

    @Override
    public final BigInteger anyBits(final int numBits) {
        final BigInteger mask = BigInteger.ONE.shiftLeft(numBits).subtract(BigInteger.ONE);
        return value.and(mask);
    }

    public final String anyName() {
        return "name:" + anyString("abcdefghijklmnopqrstuvwxyz");
    }

    public final String anyTitle() {
        return "title:" + anyString(5);
    }

    public final CharSequence anyCharSequence() {
        return "charSequence:" + anyString(3);
    }
}
