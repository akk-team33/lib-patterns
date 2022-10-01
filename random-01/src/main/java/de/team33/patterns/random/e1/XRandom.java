package de.team33.patterns.random.e1;

import java.math.BigInteger;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Represents a factory for values of many basic types, including {@link String} and {@link BigInteger},
 * based on a {@link BitFactory}.
 * <p>
 * It is primarily intended as a random generator.
 * For certain purposes, however, there can also be deterministic implementations.
 *
 * @deprecated Further development is discontinued and this package/module may be removed in a future release.
 * Successor edition is the module <em>random-mimas</em>.
 */
@Deprecated
@FunctionalInterface
public interface XRandom extends BitFactory {

    /**
     * Creates a new instance backed by a given {@link BitFactory}.
     */
    static XRandom using(final BitFactory backing) {
        return backing::anyBits;
    }

    /**
     * Creates a new instance backed by a given {@link Random} instance.
     */
    static XRandom using(final Random random) {
        return using(BitFactory.using(random));
    }

    /**
     * Returns any {@code boolean} value.
     */
    default boolean anyBoolean() {
        return anyBits(1).equals(BigInteger.ONE);
    }

    /**
     * Returns any {@code byte} value.
     */
    default byte anyByte() {
        return anyBits(Byte.SIZE).byteValue();
    }

    /**
     * Returns any {@code short} value.
     */
    default short anyShort() {
        return anyBits(Short.SIZE).shortValue();
    }

    /**
     * Returns any {@code int} value.
     */
    default int anyInt() {
        return anyBits(Integer.SIZE).intValue();
    }

    /**
     * Returns an {@code int} value between {@code zero} (incl.) and {@code bound} (excl.).
     */
    default int anyInt(final int bound) {
        return anyBigInteger(BigInteger.valueOf(bound)).intValue();
    }

    /**
     * Returns an {@code int} value between {@code min} (incl.) and {@code bound} (excl.).
     */
    default int anyInt(final int min, final int bound) {
        return anyBigInteger(BigInteger.valueOf(min), BigInteger.valueOf(bound)).intValue();
    }

    /**
     * Returns any {@code long} value.
     */
    default long anyLong() {
        return anyBits(Long.SIZE).longValue();
    }

    /**
     * Returns a {@code float} value between zero (incl.) and one (excl.).
     */
    default float anyFloat() {
        final float numerator = anyBits(RandomUtil.FLOAT_RESOLUTION).floatValue();
        final float denominator = BigInteger.ONE.shiftLeft(RandomUtil.FLOAT_RESOLUTION).floatValue();
        return numerator / denominator;
    }

    /**
     * Returns a {@code double} value between zero (incl.) and one (excl.).
     */
    default double anyDouble() {
        final double numerator = anyBits(RandomUtil.DOUBLE_RESOLUTION).doubleValue();
        final double denominator = BigInteger.ONE.shiftLeft(RandomUtil.DOUBLE_RESOLUTION).doubleValue();
        return numerator / denominator;
    }

    /**
     * Returns any {@code char} value that {@linkplain Character#isDefined(char) is defined}.
     */
    default char anyChar() {
        char result = RandomUtil.anyRawChar(this);
        while (!Character.isDefined(result)) {
            result = RandomUtil.anyRawChar(this);
        }
        return result;
    }

    /**
     * Returns any {@code char} value of the given {@code characters}.
     *
     * @param characters A {@link String} made up of the characters that are a possible result.
     */
    default char anyChar(final String characters) {
        return characters.charAt(anyInt(characters.length()));
    }

    /**
     * Returns a {@link BigInteger} value between ZERO (incl.) and {@code bound} (excl.).
     */
    default BigInteger anyBigInteger(final BigInteger bound) {
        if (BigInteger.ZERO.compareTo(bound) < 0) {
            final int numBits = bound.bitLength() + RandomUtil.BIG_RESOLUTION;
            final BigInteger numerator = bound.multiply(anyBits(numBits));
            final BigInteger denominator = BigInteger.ONE.shiftLeft(numBits);
            return numerator.divide(denominator);
        }
        throw new IllegalArgumentException("<bound> must be greater than ZERO but was " + bound);
    }

    /**
     * Returns a {@link BigInteger} value between {@code min} (incl.) and {@code bound} (excl.).
     */
    default BigInteger anyBigInteger(final BigInteger min, final BigInteger bound) {
        return anyBigInteger(bound.subtract(min)).add(min);
    }

    /**
     * Returns a {@link String} with the given {@code length} consisting of any characters that
     * {@linkplain Character#isDefined(char) are defined}.
     *
     * @param length The length of the resulting string.
     */
    default String anyString(final int length) {
        return anyString(length, this::anyChar);
    }

    /**
     * Returns a {@link String} with the given {@code length} consisting of the given {@code characters}.
     *
     * @param length     The length of the resulting string.
     * @param characters A string made up of the characters that make up a possible result.
     */
    default String anyString(final int length, final String characters) {
        return anyString(length, () -> anyChar(characters));
    }

    /**
     * Returns a {@link String} with the given {@code length}.
     *
     * @param length   The length of the resulting string.
     * @param supplier A {@link Supplier} that supplies the characters.
     */
    default String anyString(final int length, final Supplier<Character> supplier) {
        if (0 <= length) {
            return Stream.generate(supplier)
                    .limit(length)
                    .collect(StringBuilder::new,
                            StringBuilder::append,
                            StringBuilder::append)
                    .toString();
        }
        throw new IllegalArgumentException("<length> must be greater than or equal to zero but was " + length);
    }

    /**
     * Returns one of the given {@code values}.
     */
    @SuppressWarnings("unchecked")
    default <T> T anyOf(final T... values) {
        return values[anyInt(values.length)];
    }
}
