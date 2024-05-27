package de.team33.patterns.arbitrary.mimas;

import java.math.BigInteger;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A utility interface:
 * represents an interface of a basic arbitrary generator that defines methods for primitive values as well as
 * values of some other basic types, including {@code enum} types, {@link String} and {@link BigInteger}.
 * <p>
 * Most methods provide a default implementation that (directly or indirectly) uses {@link #anyBits(int)}.
 * The latter is the only method without a default implementation.
 *
 * @see de.team33.patterns.arbitrary.mimas package
 */
@FunctionalInterface
public interface Generator {

    /**
     * Provides a new {@link Generator} using a new {@link Random}.
     */
    static Generator simple() {
        return simple(new Random());
    }

    /**
     * Provides a new {@link Generator} using a given {@link Random}.
     */
    static Generator simple(final Random random) {
        return numBits -> new BigInteger(numBits, random);
    }

    /**
     * Returns any non-negative {@link BigInteger} representing a sequence of significant bits of a given length.
     * In other words, the result is any value between zero (inclusive) and 2<sup>length</sup> (exclusive).
     * <p>
     * A typical implementation will return an arbitrary value within the defined bounds, with each possible value
     * being equally probable.
     */
    BigInteger anyBits(final int numBits);

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
     * Returns an {@code int} value between {@code zero} (incl.) and {@code bound} (excl.).
     * <p>
     * A typical implementation will return an arbitrary value within the defined bounds, with smaller values
     * being more probable than bigger values.
     * <p>
     * The default implementation depends on the implementation of {@link #anySmallBigInteger(BigInteger)}.
     */
    default int anySmallInt(final int bound) {
        return anySmallBigInteger(BigInteger.valueOf(bound)).intValue();
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
     * Returns any {@code char} value of a predefined character set.
     * <p>
     * The default implementation depends on the implementation of {@link #anyChar(String)} and returns
     * one of {@code "0123456789_abcdefghijklmnopqrstuvwxyz-ABCDEFGHIJKLMNOPQRSTUVWXYZ !#$§%&*+,.?@äöüÄÖÜß"}.
     */
    default char anyChar() {
        return anyChar(Util.STD_CHARACTERS);
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
     * Returns a {@link BigInteger} value between {@link Long#MIN_VALUE} and {@link Long#MAX_VALUE} (both incl.).
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default BigInteger anyBigInteger() {
        return BigInteger.valueOf(anyBits(Long.SIZE).longValue());
    }

    /**
     * Returns a {@link BigInteger} value between {@link BigInteger#ZERO} (incl.) and {@code bound} (excl.).
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default BigInteger anyBigInteger(final BigInteger bound) {
        return Util.anyBigInteger(this, bound, bound.bitLength());
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
     * Returns a {@link BigInteger} value between zero (incl.) and 2<sup>16</sup> (excl.).
     * <p>
     * A typical implementation will return an arbitrary value within the defined bounds, with smaller values
     * being more probable than bigger values.
     * <p>
     * The default implementation depends on the implementation of {@link #anySmallBigInteger(BigInteger)}.
     */
    default BigInteger anySmallBigInteger() {
        return anySmallBigInteger(BigInteger.ONE.shiftLeft(16));
    }

    /**
     * Returns a {@link BigInteger} value between {@link BigInteger#ZERO} (incl.) and {@code bound} (excl.).
     * <p>
     * A typical implementation will return an arbitrary value within the defined bounds, with smaller values
     * being more probable than bigger values.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default BigInteger anySmallBigInteger(final BigInteger bound) {
        return Util.anyBigInteger(this, bound, anyInt(bound.bitLength()) + 1);
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
