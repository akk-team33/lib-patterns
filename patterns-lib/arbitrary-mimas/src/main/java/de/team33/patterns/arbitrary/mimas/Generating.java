package de.team33.patterns.arbitrary.mimas;

import java.math.BigInteger;
import java.util.stream.Stream;

final class Generating {

    private static final int FLOAT_RESOLUTION = Float.SIZE - 8;
    private static final int DOUBLE_RESOLUTION = Double.SIZE - 11;

    private Generating() {
    }

    private static BigInteger anyBigInteger(final Generator generator, final BigInteger bound, final int bitLength) {
        if (BigInteger.ZERO.compareTo(bound) < 0) {
            return Stream.generate(() -> generator.anyBits(bitLength))
                         .limit(Util.MAX_RETRY)
                         .filter(result -> result.compareTo(bound) < 0)
                         .findAny()
                         .orElseGet(() -> generator.anyBits(bitLength - 1));
        }
        throw new IllegalArgumentException("<bound> must be greater than ZERO but was " + bound);
    }

    static boolean anyBoolean(final Generator generator) {
        return generator.anyBits(1).equals(BigInteger.ONE);
    }

    static byte anyByte(final Generator generator) {
        return generator.anyBits(Byte.SIZE).byteValue();
    }

    static short anyShort(final Generator generator) {
        return generator.anyBits(Short.SIZE).shortValue();
    }

    static int anyInt(final Generator generator) {
        return generator.anyBits(Integer.SIZE).intValue();
    }

    static int anyInt(final Generator generator, final int bound) {
        return anyBigInteger(generator, BigInteger.valueOf(bound)).intValue();
    }

    static int anyInt(final Generator generator, final int min, final int bound) {
        return anyBigInteger(generator, BigInteger.valueOf(min), BigInteger.valueOf(bound)).intValue();
    }

    static long anyLong(final Generator generator) {
        return generator.anyBits(Long.SIZE).longValue();
    }

    static float anyFloat(final Generator generator) {
        final float numerator = generator.anyBits(FLOAT_RESOLUTION).floatValue();
        final float denominator = BigInteger.ONE.shiftLeft(FLOAT_RESOLUTION).floatValue();
        return numerator / denominator;
    }

    static double anyDouble(final Generator generator) {
        final double numerator = generator.anyBits(DOUBLE_RESOLUTION).doubleValue();
        final double denominator = BigInteger.ONE.shiftLeft(DOUBLE_RESOLUTION).doubleValue();
        return numerator / denominator;
    }

    static BigInteger anyBigInteger(final Generator generator) {
        return BigInteger.valueOf(anyLong(generator));
    }

    static BigInteger anyBigInteger(final Generator generator, final BigInteger bound) {
        return anyBigInteger(generator, bound, bound.bitLength());
    }

    static BigInteger anyBigInteger(final Generator generator, final BigInteger min, final BigInteger bound) {
        return anyBigInteger(generator, bound.subtract(min)).add(min);
    }

    static BigInteger anySmallBigInteger(final Generator generator) {
        return anySmallBigInteger(generator, BigInteger.ONE.shiftLeft(16));
    }

    static BigInteger anySmallBigInteger(final Generator generator, final BigInteger bound) {
        return anyBigInteger(generator, bound, anyInt(generator, bound.bitLength()) + 2);
    }
}
