package de.team33.patterns.arbitrary.mimas;

import java.math.BigInteger;
import java.util.function.ObjIntConsumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

final class Generating {

    static final int FLOAT_RESOLUTION = Float.SIZE - 8;
    static final int DOUBLE_RESOLUTION = Double.SIZE - 11;
    private static final int DEFAULT_BOUND_BITS = 16;

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

    private static ObjIntConsumer<StringBuilder> sbAppender(final CharSequence characters) {
        return (sb, index) -> sb.append(characters.charAt(index));
    }

    static int anyInt(final BitGenerator generator, final int bound) {
        return anyBigInteger(generator, BigInteger.valueOf(bound)).intValue();
    }

    static int anyInt(final BitGenerator generator, final int min, final int bound) {
        return anyBigInteger(generator, BigInteger.valueOf(min), BigInteger.valueOf(bound)).intValue();
    }

    static BigInteger anyBigInteger(final BitGenerator generator, final BigInteger bound) {
        return anyBigInteger(generator, bound, bound.bitLength());
    }

    static BigInteger anyBigInteger(final BitGenerator generator, final BigInteger min, final BigInteger bound) {
        return anyBigInteger(generator, bound.subtract(min)).add(min);
    }

    static BigInteger anySmallBigInteger(final BitGenerator generator) {
        return anySmallBigInteger(generator, BigInteger.ONE.shiftLeft(DEFAULT_BOUND_BITS));
    }

    static BigInteger anySmallBigInteger(final BitGenerator generator, final BigInteger bound) {
        return anyBigInteger(generator, bound, anyInt(generator, bound.bitLength()) + 1);
    }

    static <T> T anyOf(final BitGenerator generator, final T[] values) {
        return values[anyInt(generator, values.length)];
    }

    static char anyChar(final BitGenerator generator, final CharSequence characters) {
        return characters.charAt(anyInt(generator, characters.length()));
    }

    static String anyString(final BitGenerator generator,
                            final int minLength,
                            final int maxLength,
                            final CharSequence characters) {
        return anyString(generator, 1 + anyInt(generator, minLength - 1, maxLength), characters);
    }

    static String anyString(final BitGenerator generator, final int length, final CharSequence characters) {
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
}
