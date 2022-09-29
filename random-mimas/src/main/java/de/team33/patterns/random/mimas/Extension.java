package de.team33.patterns.random.mimas;

import java.math.BigInteger;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface Extension {

    BigInteger nextBits(final int numBits);

    default byte nextByte() {
        return nextBits(Byte.SIZE).byteValue();
    }

    default short nextShort() {
        return nextBits(Short.SIZE).shortValue();
    }

    default int nextInt(final int bound) {
        return nextBigInteger(BigInteger.valueOf(bound)).intValue();
    }

    default int nextInt(final int min, final int bound) {
        return nextBigInteger(BigInteger.valueOf(min), BigInteger.valueOf(bound)).intValue();
    }

    default long nextLong(final long bound) {
        return nextBigInteger(BigInteger.valueOf(bound)).longValue();
    }

    default long nextLong(final long min, final long bound) {
        return nextBigInteger(BigInteger.valueOf(min), BigInteger.valueOf(bound)).longValue();
    }

    default char nextChar(final String characters) {
        return characters.charAt(nextInt(characters.length()));
    }

    default String nextString(final int length, final String characters) {
        if (0 <= length) {
            return IntStream.generate(() -> nextInt(characters.length()))
                            .limit(length)
                            .collect(StringBuilder::new,
                                     (sb, i) -> sb.append(characters.charAt(i)),
                                     StringBuilder::append)
                            .toString();
        }
        throw new IllegalArgumentException("<length> must be greater than or equal to zero but was " + length);
    }

    default BigInteger nextBigInteger(final BigInteger bound) {
        if (BigInteger.ZERO.compareTo(bound) < 0) {
            final int bitLength = bound.bitLength();
            return Stream.generate(() -> nextBits(bitLength))
                         .filter(result -> result.compareTo(bound) < 0)
                         .findAny()
                         .orElseThrow(NoSuchElementException::new);
        }
        throw new IllegalArgumentException("<bound> must be greater than ZERO but was " + bound);
    }

    default BigInteger nextBigInteger(final BigInteger min, final BigInteger bound) {
        return nextBigInteger(bound.subtract(min)).add(min);
    }

    default <T extends Enum<T>> T nextOf(final Class<T> enumClass) {
        return nextOf(enumClass.getEnumConstants());
    }

    default <T> T nextOf(final T... values) {
        return values[nextInt(values.length)];
    }
}
