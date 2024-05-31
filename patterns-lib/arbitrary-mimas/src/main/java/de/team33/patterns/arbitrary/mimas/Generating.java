package de.team33.patterns.arbitrary.mimas;

import java.math.BigInteger;
import java.util.function.ObjIntConsumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

final class Generating {

    private static final int FLOAT_RESOLUTION = Float.SIZE - 8;
    private static final int DOUBLE_RESOLUTION = Double.SIZE - 11;

    private Generating() {
    }

    private static BigInteger anyBigInteger(final BitGenerator generator, final BigInteger bound, final int bitLength) {
        if (BigInteger.ZERO.compareTo(bound) < 0) {
            return Stream.generate(() -> generator.anyBits(bitLength))
                         .limit(Util.MAX_RETRY)
                         .filter(result -> result.compareTo(bound) < 0)
                         .findAny()
                         .orElseGet(() -> generator.anyBits(bitLength - 1));
        }
        throw new IllegalArgumentException("<bound> must be greater than ZERO but was " + bound);
    }

    private static ObjIntConsumer<StringBuilder> sbAppender(final String characters) {
        return (sb, index) -> sb.append(characters.charAt(index));
    }

    static boolean anyBoolean(final BitGenerator generator) {
        return generator.anyBits(1).equals(BigInteger.ONE);
    }

    static byte anyByte(final BitGenerator generator) {
        return generator.anyBits(Byte.SIZE).byteValue();
    }

    static short anyShort(final BitGenerator generator) {
        return generator.anyBits(Short.SIZE).shortValue();
    }

    static int anyInt(final BitGenerator generator) {
        return generator.anyBits(Integer.SIZE).intValue();
    }

    static int anyInt(final BitGenerator generator, final int bound) {
        return anyBigInteger(generator, BigInteger.valueOf(bound)).intValue();
    }

    static int anyInt(final BitGenerator generator, final int min, final int bound) {
        return anyBigInteger(generator, BigInteger.valueOf(min), BigInteger.valueOf(bound)).intValue();
    }

    static int anySmallInt(final BitGenerator generator, final int bound) {
        return anySmallBigInteger(generator, BigInteger.valueOf(bound)).intValue();
    }

    static long anyLong(final BitGenerator generator) {
        return generator.anyBits(Long.SIZE).longValue();
    }

    static long anyLong(final BitGenerator generator, final long bound) {
        return anyBigInteger(generator, BigInteger.valueOf(bound)).longValue();
    }

    static long anyLong(final BitGenerator generator, final long min, final long bound) {
        return anyBigInteger(generator, BigInteger.valueOf(min), BigInteger.valueOf(bound)).longValue();
    }

    static float anyFloat(final BitGenerator generator) {
        final float numerator = generator.anyBits(FLOAT_RESOLUTION).floatValue();
        final float denominator = BigInteger.ONE.shiftLeft(FLOAT_RESOLUTION).floatValue();
        return numerator / denominator;
    }

    static double anyDouble(final BitGenerator generator) {
        final double numerator = generator.anyBits(DOUBLE_RESOLUTION).doubleValue();
        final double denominator = BigInteger.ONE.shiftLeft(DOUBLE_RESOLUTION).doubleValue();
        return numerator / denominator;
    }

    static BigInteger anyBigInteger(final BitGenerator generator) {
        return BigInteger.valueOf(anyLong(generator));
    }

    static BigInteger anyBigInteger(final BitGenerator generator, final BigInteger bound) {
        return anyBigInteger(generator, bound, bound.bitLength());
    }

    static BigInteger anyBigInteger(final BitGenerator generator, final BigInteger min, final BigInteger bound) {
        return anyBigInteger(generator, bound.subtract(min)).add(min);
    }

    static BigInteger anySmallBigInteger(final BitGenerator generator) {
        return anySmallBigInteger(generator, BigInteger.ONE.shiftLeft(16));
    }

    static BigInteger anySmallBigInteger(final BitGenerator generator, final BigInteger bound) {
        return anyBigInteger(generator, bound, anyInt(generator, bound.bitLength()) + 1);
    }

    static <T> T anyOf(final BitGenerator generator, final T[] values) {
        return values[anyInt(generator, values.length)];
    }

    static char anyChar(final BitGenerator generator, final String characters) {
        return characters.charAt(anyInt(generator, characters.length()));
    }

    static char anyChar(final BitGenerator generator) {
        return anyChar(generator, Util.STD_CHARACTERS);
    }

    static String anyString(final BitGenerator generator, final int length, final String characters) {
        if (0 > length) {
            throw new IllegalArgumentException("<length> must be greater than or equal to zero but was " + length);
        }
        if (characters.isEmpty()) {
            throw new IllegalArgumentException("<characters> must not be empty but was \"" + characters + "\"");
        }
        return IntStream.generate(() -> anyInt(generator, characters.length()))
                        .limit(length)
                        .collect(StringBuilder::new,
                                 sbAppender(characters),
                                 StringBuilder::append)
                        .toString();
    }

    public static String anyString(final BitGenerator generator) {
        return anyString(generator, 1 + anyInt(generator, 128), Util.STD_CHARACTERS);
    }
}
