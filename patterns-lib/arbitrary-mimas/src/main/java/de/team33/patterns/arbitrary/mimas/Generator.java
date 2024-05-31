package de.team33.patterns.arbitrary.mimas;

import java.math.BigInteger;
import java.util.Random;
import java.util.stream.IntStream;

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
     * Provides a new {@link Generator} based on a given {@link Random}.
     */
    static Generator of(final Random random) {
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
     * A typical implementation will return one of {true, false}, with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default boolean anyBoolean() {
        return Generating.anyBoolean(this);
    }

    /**
     * Returns any {@code byte} value.
     * <p>
     * A typical implementation will return an arbitrary {@code byte} value,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default byte anyByte() {
        return Generating.anyByte(this);
    }

    /**
     * Returns any {@code short} value.
     * <p>
     * A typical implementation will return an arbitrary {@code short} value,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default short anyShort() {
        return Generating.anyShort(this);
    }

    /**
     * Returns any {@code int} value.
     * <p>
     * A typical implementation will return an arbitrary {@code int} value,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default int anyInt() {
        return Generating.anyInt(this);
    }

    /**
     * Returns an {@code int} value between {@code zero} (incl.) and {@code bound} (excl.).
     * <p>
     * A typical implementation will return an arbitrary {@code int} value within the defined bounds,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default int anyInt(final int bound) {
        return Generating.anyInt(this, bound);
    }

    /**
     * Returns an {@code int} value between {@code min} (incl.) and {@code bound} (excl.).
     * <p>
     * A typical implementation will return an arbitrary {@code int} value within the defined bounds,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default int anyInt(final int min, final int bound) {
        return Generating.anyInt(this, min, bound);
    }

    /**
     * Returns an {@code int} value between {@code zero} (incl.) and {@code bound} (excl.).
     * <p>
     * A typical implementation will return an arbitrary {@code int} value within the defined bounds,
     * with smaller values being more probable than bigger values.
     * <p>
     * The default implementation depends on the implementation of {@link #anySmallBigInteger(BigInteger)}.
     */
    default int anySmallInt(final int bound) {
        return anySmallBigInteger(BigInteger.valueOf(bound)).intValue();
    }

    /**
     * Returns any {@code long} value.
     * <p>
     * A typical implementation will return an arbitrary {@code long} value,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default long anyLong() {
        return Generating.anyLong(this);
    }

    /**
     * Returns any {@code long} value between {@code zero} (incl.) and {@code bound} (excl.).
     * <p>
     * A typical implementation will return an arbitrary {@code long} value within the defined bounds,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBigInteger(BigInteger)}.
     */
    default long anyLong(final long bound) {
        return anyBigInteger(BigInteger.valueOf(bound)).longValue();
    }

    /**
     * Returns any {@code long} value between {@code min} (incl.) and {@code bound} (excl.).
     * <p>
     * A typical implementation will return an arbitrary {@code long} value within the defined bounds,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBigInteger(BigInteger, BigInteger)}.
     */
    default long anyLong(final long min, final long bound) {
        return anyBigInteger(BigInteger.valueOf(min), BigInteger.valueOf(bound)).longValue();
    }

    /**
     * Returns a {@code float} value between zero (incl.) and one (excl.).
     * <p>
     * A typical implementation will return an arbitrary {@code long} value within the defined bounds,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default float anyFloat() {
        return Generating.anyFloat(this);
    }

    /**
     * Returns a {@code double} value between zero (incl.) and one (excl.).
     * <p>
     * A typical implementation will return an arbitrary {@code long} value within the defined bounds,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default double anyDouble() {
        return Generating.anyDouble(this);
    }

    /**
     * Returns any {@code char} value of a predefined character set.
     * <p>
     * A typical implementation will return an arbitrary {@code char} value within the predefined character set,
     * with each possible value being equally probable.
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
     * A typical implementation will return an arbitrary {@code char} value within the given character set,
     * with each possible value being equally probable.
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
     * A typical implementation will return an arbitrary {@link BigInteger} value within the defined bounds,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default BigInteger anyBigInteger() {
        return Generating.anyBigInteger(this);
    }

    /**
     * Returns a {@link BigInteger} value between {@link BigInteger#ZERO} (incl.) and {@code bound} (excl.).
     * <p>
     * A typical implementation will return an arbitrary {@link BigInteger} value within the defined bounds,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default BigInteger anyBigInteger(final BigInteger bound) {
        return Generating.anyBigInteger(this, bound);
    }

    /**
     * Returns a {@link BigInteger} value between {@code min} (incl.) and {@code bound} (excl.).
     * <p>
     * A typical implementation will return an arbitrary {@link BigInteger} value within the defined bounds,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default BigInteger anyBigInteger(final BigInteger min, final BigInteger bound) {
        return Generating.anyBigInteger(this, min, bound);
    }

    /**
     * Returns a {@link BigInteger} value between zero (incl.) and 2<sup>16</sup> (excl.).
     * <p>
     * A typical implementation will return an arbitrary {@link BigInteger} value within the defined bounds,
     * with smaller values being more probable than bigger values.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default BigInteger anySmallBigInteger() {
        return Generating.anySmallBigInteger(this);
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
        return Generating.anySmallBigInteger(this, bound);
    }

    /**
     * Returns one of the given {@code values}.
     * <p>
     * The default implementation depends on the implementation of {@link #anyInt(int)}.
     */
    @SuppressWarnings("unchecked")
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
