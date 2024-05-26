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
    default byte nextByte() {
        return anyBits(Byte.SIZE).byteValue();
    }

    /**
     * Returns any {@code short} value.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default short nextShort() {
        return anyBits(Short.SIZE).shortValue();
    }

    /**
     * Returns any {@code int} value.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     * <p>
     * When used as extension of a {@link Random} derivation, this method will (at least) be overridden by
     * {@link Random#nextInt()}.
     */
    default int nextInt() {
        return anyBits(Integer.SIZE).intValue();
    }

    /**
     * Returns an {@code int} value between {@code zero} (incl.) and {@code bound} (excl.).
     * <p>
     * The default implementation depends on the implementation of {@link #nextBigInteger(BigInteger)}.
     * <p>
     * When used as extension of a {@link Random} derivation, this method will (at least) be overridden by
     * {@link Random#nextInt(int)}.
     */
    default int nextInt(final int bound) {
        return nextBigInteger(BigInteger.valueOf(bound)).intValue();
    }

    /**
     * Returns an {@code int} value between {@code min} (incl.) and {@code bound} (excl.).
     * <p>
     * The default implementation depends on the implementation of {@link #nextBigInteger(BigInteger, BigInteger)}.
     */
    default int nextInt(final int min, final int bound) {
        return nextBigInteger(BigInteger.valueOf(min), BigInteger.valueOf(bound)).intValue();
    }

    /**
     * Returns any {@code long} value.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     * <p>
     * When used as extension of a {@link Random} derivation, this method will (at least) be overridden by
     * {@link Random#nextLong()}.
     */
    default long nextLong() {
        return anyBits(Long.SIZE).longValue();
    }

    /**
     * Returns any {@code long} value between {@code zero} (incl.) and {@code bound} (excl.).
     * <p>
     * The default implementation depends on the implementation of {@link #nextBigInteger(BigInteger)}.
     */
    default long nextLong(final long bound) {
        return nextBigInteger(BigInteger.valueOf(bound)).longValue();
    }

    /**
     * Returns any {@code long} value between {@code min} (incl.) and {@code bound} (excl.).
     * <p>
     * The default implementation depends on the implementation of {@link #nextBigInteger(BigInteger, BigInteger)}.
     */
    default long nextLong(final long min, final long bound) {
        return nextBigInteger(BigInteger.valueOf(min), BigInteger.valueOf(bound)).longValue();
    }

    /**
     * Returns a {@code float} value between zero (incl.) and one (excl.).
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     * <p>
     * When used as extension of a {@link Random} derivation, this method will (at least) be overridden by
     * {@link Random#nextFloat()}.
     */
    default float nextFloat() {
        final float numerator = anyBits(Util.FLOAT_RESOLUTION).floatValue();
        final float denominator = BigInteger.ONE.shiftLeft(Util.FLOAT_RESOLUTION).floatValue();
        return numerator / denominator;
    }

    /**
     * Returns a {@code double} value between zero (incl.) and one (excl.).
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     * <p>
     * When used as extension of a {@link Random} derivation, this method will (at least) be overridden by
     * {@link Random#nextDouble()}.
     */
    default double nextDouble() {
        final double numerator = anyBits(Util.DOUBLE_RESOLUTION).doubleValue();
        final double denominator = BigInteger.ONE.shiftLeft(Util.DOUBLE_RESOLUTION).doubleValue();
        return numerator / denominator;
    }

    /**
     * Returns any {@code char} value of the given {@code characters}.
     * <p>
     * The default implementation depends on the implementation of {@link #nextInt(int)}.
     *
     * @param characters A {@link String} made up of the characters that are a possible result.
     */
    default char nextChar(final String characters) {
        return characters.charAt(nextInt(characters.length()));
    }

    /**
     * Returns a {@link String} with the given {@code length} consisting of the given {@code characters}.
     * <p>
     * The default implementation depends on the implementation of {@link #nextInt(int)}.
     *
     * @param length     The length of the resulting string.
     * @param characters A string made up of the characters that make up a possible result.
     */
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

    /**
     * Returns a {@link BigInteger} value between {@link BigInteger#ZERO} (incl.) and {@code bound} (excl.).
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default BigInteger nextBigInteger(final BigInteger bound) {
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
     * The default implementation depends on the implementation of {@link #nextBigInteger(BigInteger)}.
     */
    default BigInteger nextBigInteger(final BigInteger min, final BigInteger bound) {
        return nextBigInteger(bound.subtract(min)).add(min);
    }

    /**
     * Returns one of the given {@code values}.
     * <p>
     * The default implementation depends on the implementation of {@link #nextInt(int)}.
     */
    default <T> T nextOf(final T... values) {
        return values[nextInt(values.length)];
    }

    /**
     * Returns one of the given {@code enum} {@code values}.
     * <p>
     * The default implementation depends on the implementation of {@link #nextOf(Object[])}.
     */
    default <T extends Enum<T>> T nextOf(final Class<T> enumClass) {
        return nextOf(enumClass.getEnumConstants());
    }
}
