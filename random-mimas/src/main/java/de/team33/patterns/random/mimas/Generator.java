package de.team33.patterns.random.mimas;

import java.math.BigInteger;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Represents a generator for values of many basic types, including {@link String} and {@link BigInteger}.
 * <p>
 * It is primarily intended as a random generator.
 * For certain purposes, however, there can also be deterministic implementations.
 */
@FunctionalInterface
public interface Generator {

    /**
     * Creates a new instance backed by a given {@link Supplier}.
     */
    static Generator using(final IntFunction<BigInteger> backing) {
        return numBits -> {
            final BigInteger mask = BigInteger.ONE.shiftLeft(numBits).subtract(BigInteger.ONE);
            return backing.apply(numBits).and(mask);
        };
    }

    /**
     * Creates a new instance backed by a given {@link Random} instance.
     */
    static Generator using(final Random random) {
        return numBits -> new BigInteger(numBits, random);
    }

    /**
     * Returns any non-negative {@link BigInteger} representing a sequence of significant bits of a given length.
     * In other words, the result is any value between zero (inclusive) and 2<sup>length</sup> (exclusive).
     * <p>
     * A typical implementation will return a random value within the defined bounds, with each possible value being
     * equally probable.
     */
    BigInteger anyBits(int numBits);

    /**
     * Returns any {@code boolean} value.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default boolean anyBoolean() {
        return anyBits(1).equals(BigInteger.ONE);
    }

    /**
     * Returns any {@code byte} value.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default byte anyByte() {
        return anyBits(Byte.SIZE).byteValue();
    }

    /**
     * Returns any {@code short} value.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default short anyShort() {
        return anyBits(Short.SIZE).shortValue();
    }

    /**
     * Returns any {@code int} value.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default int anyInt() {
        return anyBits(Integer.SIZE).intValue();
    }

    /**
     * Returns an {@code int} value between {@code zero} (incl.) and {@code bound} (excl.).
     * <p>
     * The default implementation depends on the implementation of {@link #anyBigInteger(BigInteger)}.
     */
    default int anyInt(final int bound) {
        return anyBigInteger(BigInteger.valueOf(bound)).intValue();
    }

    /**
     * Returns an {@code int} value between {@code min} (incl.) and {@code bound} (excl.).
     * <p>
     * The default implementation depends on the implementation of {@link #anyBigInteger(BigInteger, BigInteger)}.
     */
    default int anyInt(final int min, final int bound) {
        return anyBigInteger(BigInteger.valueOf(min), BigInteger.valueOf(bound)).intValue();
    }

    /**
     * Returns any {@code long} value.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default long anyLong() {
        return anyBits(Long.SIZE).longValue();
    }

    /**
     * Returns any {@code long} value between {@code zero} (incl.) and {@code bound} (excl.).
     * <p>
     * The default implementation depends on the implementation of {@link #anyBigInteger(BigInteger)}.
     */
    default long anyLong(final long bound) {
        return anyBigInteger(BigInteger.valueOf(bound)).longValue();
    }

    /**
     * Returns any {@code long} value between {@code min} (incl.) and {@code bound} (excl.).
     * <p>
     * The default implementation depends on the implementation of {@link #anyBigInteger(BigInteger, BigInteger)}.
     */
    default long anyLong(final long min, final long bound) {
        return anyBigInteger(BigInteger.valueOf(min), BigInteger.valueOf(bound)).longValue();
    }

    /**
     * Returns a {@code float} value between zero (incl.) and one (excl.).
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default float anyFloat() {
        final float numerator = anyBits(Util.FLOAT_RESOLUTION).floatValue();
        final float denominator = BigInteger.ONE.shiftLeft(Util.FLOAT_RESOLUTION).floatValue();
        return numerator / denominator;
    }

    /**
     * Returns a {@code double} value between zero (incl.) and one (excl.).
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default double anyDouble() {
        final double numerator = anyBits(Util.DOUBLE_RESOLUTION).doubleValue();
        final double denominator = BigInteger.ONE.shiftLeft(Util.DOUBLE_RESOLUTION).doubleValue();
        return numerator / denominator;
    }

    /**
     * Returns any {@code char} value of the given {@code characters}.
     * <p>
     * The default implementation depends on the implementation of {@link #anyInt(int)}.
     *
     * @param characters A {@link String} made up of the characters that are a possible result.
     */
    default char anyChar(final String characters) {
        return characters.charAt(anyInt(characters.length()));
    }

    /**
     * Returns a {@link BigInteger} value between ZERO (incl.) and {@code bound} (excl.).
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default BigInteger anyBigInteger(final BigInteger bound) {
        if (BigInteger.ZERO.compareTo(bound) < 0) {
            final int bitLength = bound.bitLength();
            return Stream.generate(() -> anyBits(bitLength))
                         .filter(result -> result.compareTo(bound) < 0)
                         .findAny()
                         .orElseThrow(NoSuchElementException::new);
        }
        throw new IllegalArgumentException("<bound> must be greater than ZERO but was " + bound);
    }

    /**
     * Returns a {@link BigInteger} value between {@code min} (incl.) and {@code bound} (excl.).
     * <p>
     * The default implementation depends on the implementation of {@link #anyBigInteger(BigInteger)}.
     */
    default BigInteger anyBigInteger(final BigInteger min, final BigInteger bound) {
        return anyBigInteger(bound.subtract(min)).add(min);
    }

    /**
     * Returns a {@link String} with the given {@code length} consisting of the given {@code characters}.
     * <p>
     * The default implementation depends on the implementation of {@link #anyInt(int)}.
     *
     * @param length     The length of the resulting string.
     * @param characters A string made up of the characters that make up a possible result.
     */
    default String anyString(final int length, final String characters) {
        if (0 <= length) {
            return IntStream.generate(() -> anyInt(characters.length()))
                            .limit(length)
                            .collect(StringBuilder::new,
                                     (sb, i) -> sb.append(characters.charAt(i)),
                                     StringBuilder::append)
                            .toString();
        }
        throw new IllegalArgumentException("<length> must be greater than or equal to zero but was " + length);
    }

    /**
     * Returns one of the given {@code values}.
     * <p>
     * The default implementation depends on the implementation of {@link #anyInt(int)}.
     */
    default <T> T anyOf(final T... values) {
        return values[anyInt(values.length)];
    }

    /**
     * Returns one of the given {@code enum} {@code values}.
     * <p>
     * The default implementation depends on the implementation of {@link #anyOf(Object[])}.
     */
    default <T extends Enum<T>> T anyOf(final Class<T> enumClass) {
        return anyOf(enumClass.getEnumConstants());
    }
}
