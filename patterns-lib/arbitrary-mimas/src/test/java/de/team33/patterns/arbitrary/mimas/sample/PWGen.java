package de.team33.patterns.arbitrary.mimas.sample;

import de.team33.patterns.arbitrary.mimas.Generator;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.stream.Stream;

public class PWGen extends SecureRandom implements Generator {

    private static final String CHARS = "abcdefghijkmnopqrstuvwxyz-ABCDEFGHJKLMNPQRSTUVWXYZ_0123456789.&%$";

    @Override
    public final BigInteger nextBits(final int numBits) {
        return new BigInteger(numBits, this);
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public static void main(final String[] args) {
        final PWGen pwGen = new PWGen();
        Stream.generate(() -> pwGen.nextString(20, CHARS)).limit(20).forEach(System.out::println);
    }
}
