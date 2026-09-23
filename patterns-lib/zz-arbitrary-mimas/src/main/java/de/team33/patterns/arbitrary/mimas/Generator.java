package de.team33.patterns.arbitrary.mimas;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Represents a basic arbitrary value generator that defines methods for primitive values as well as
 * values of some other basic types, including {@code enum} types, {@link String} and {@link BigInteger}.
 * <p>
 * Most methods provide a default implementation that (directly or indirectly) uses {@link #anyBits(int)}.
 * The latter is the only method without a default implementation.
 *
 * @see de.team33.patterns.arbitrary.mimas package
 */
@SuppressWarnings("ClassWithTooManyMethods")
@FunctionalInterface
public interface Generator extends BitGenerator {

    /**
     * <b>Utility method:</b>
     * Returns an arbitrary non-negative {@link BigInteger} representing a sequence of <em>numBits</em> significant
     * bits, intended as a result of {@link #anyBits(int)}.
     */
    static BigInteger anyBits(final int numBits, final Random random) {
        return new BigInteger(numBits, random);
    }

    /**
     * @deprecated use {@link #by(Random)} instead.
     */
    @Deprecated(forRemoval = true)
    static Generator of(final Random random) {
        return numBits -> anyBits(numBits, random);
    }

    /**
     * <b>Utility method:</b>
     * Provides a new instance based on a given {@link Random}.
     */
    static Generator by(final Random random) {
        return numBits -> anyBits(numBits, random);
    }

    /**
     * <b>Utility method:</b>
     * Provides a new instance based on a new {@link SecureRandom}.
     */
    static Generator byDefault() {
        return by(new SecureRandom());
    }

    /**
     * Returns a {@code boolean} value.
     * <p>
     * A typical implementation will return one of {true, false}, with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default boolean anyBoolean() {
        return Generating.anyBoolean(this);
    }

    /**
     * Returns a {@code byte} value.
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
     * Returns a {@code short} value.
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
     * Returns an {@code int} value.
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
     * Returns an {@code int} value between {@code zero} (incl.) and <em>bound</em> (excl.).
     * <p>
     * A typical implementation will return an arbitrary {@code int} value within the defined bounds,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     *
     * @throws IllegalArgumentException if not {@code zero} &lt; <em>bound</em>
     */
    default int anyInt(final int bound) {
        return Generating.anyInt(this, bound);
    }

    /**
     * Returns an {@code int} value between <em>min</em> (incl.) and <em>bound</em> (excl.).
     * <p>
     * A typical implementation will return an arbitrary {@code int} value within the defined bounds,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     *
     * @throws IllegalArgumentException if not <em>min</em> &lt; <em>bound</em>
     */
    default int anyInt(final int min, final int bound) {
        return Generating.anyInt(this, min, bound);
    }

    /**
     * Returns an {@code int} value between {@code zero} (incl.) and <em>bound</em> (excl.).
     * <p>
     * A typical implementation will return an arbitrary {@code int} value within the defined bounds,
     * with smaller values being more probable than bigger values.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     *
     * @throws IllegalArgumentException if not {@code zero} &lt; <em>bound</em>
     */
    default int anySmallInt(final int bound) {
        return Generating.anySmallInt(this, bound);
    }

    /**
     * Returns a {@code long} value.
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
     * Returns a {@code long} value between {@code zero} (incl.) and <em>bound</em> (excl.).
     * <p>
     * A typical implementation will return an arbitrary {@code long} value within the defined bounds,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     *
     * @throws IllegalArgumentException if not {@code zero} &lt; <em>bound</em>
     */
    default long anyLong(final long bound) {
        return Generating.anyLong(this, bound);
    }

    /**
     * Returns a {@code long} value between <em>min</em> (incl.) and <em>bound</em> (excl.).
     * <p>
     * A typical implementation will return an arbitrary {@code long} value within the defined bounds,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     *
     * @throws IllegalArgumentException if not <em>min</em> &lt; <em>bound</em>
     */
    default long anyLong(final long min, final long bound) {
        return Generating.anyLong(this, min, bound);
    }

    /**
     * Returns a {@code float} value between {@code zero} (incl.) and {@code one} (excl.).
     * <p>
     * A typical implementation will return an arbitrary {@code float} value within the defined bounds,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default float anyFloat() {
        return Generating.anyFloat(this);
    }

    /**
     * Returns a {@code double} value between {@code zero} (incl.) and {@code one} (excl.).
     * <p>
     * A typical implementation will return an arbitrary {@code double} value within the defined bounds,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default double anyDouble() {
        return Generating.anyDouble(this);
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
     * Returns a {@link BigInteger} value between {@link BigInteger#ZERO zero} (incl.) and <em>bound</em> (excl.).
     * <p>
     * A typical implementation will return an arbitrary {@link BigInteger} value within the defined bounds,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     *
     * @throws IllegalArgumentException if not {@code zero} &lt; <em>bound</em>
     */
    default BigInteger anyBigInteger(final BigInteger bound) {
        return Generating.anyBigInteger(this, bound);
    }

    /**
     * Returns a {@link BigInteger} value between <em>min</em> (incl.) and <em>bound</em> (excl.).
     * <p>
     * A typical implementation will return an arbitrary {@link BigInteger} value within the defined bounds,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     *
     * @throws IllegalArgumentException if not <em>min</em> &lt; <em>bound</em>
     */
    default BigInteger anyBigInteger(final BigInteger min, final BigInteger bound) {
        return Generating.anyBigInteger(this, min, bound);
    }

    /**
     * Returns a {@link BigInteger} value between {@link BigInteger#ZERO zero} (incl.) and 2<sup>16</sup> (excl.).
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
     * Returns a {@link BigInteger} value between {@link BigInteger#ZERO zero} (incl.) and <em>bound</em> (excl.).
     * <p>
     * A typical implementation will return an arbitrary value within the defined bounds, with smaller values
     * being more probable than bigger values.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     *
     * @throws IllegalArgumentException if not {@code zero} &lt; <em>bound</em>
     */
    default BigInteger anySmallBigInteger(final BigInteger bound) {
        return Generating.anySmallBigInteger(this, bound);
    }

    /**
     * Returns a {@code char} value from a predefined character set.
     * <p>
     * A typical implementation will return an arbitrary {@code char} value from the predefined character set,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)} and returns
     * one of {@code "0123456789_abcdefghijklmnopqrstuvwxyz-ABCDEFGHIJKLMNOPQRSTUVWXYZ !#$§%&*+,.?@äöüÄÖÜß"}.
     */
    default char anyChar() {
        return Generating.anyChar(this);
    }

    /**
     * Returns a {@code char} value from the given <em>characters</em>.
     * <p>
     * A typical implementation will return an arbitrary {@code char} value from the given <em>characters</em>,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     *
     * @param characters A {@link String} made up of the characters that are a possible result.
     */
    default char anyChar(final String characters) {
        return Generating.anyChar(this, characters);
    }

    /**
     * Returns a {@link String} with the given <em>length</em> made up from the given <em>characters</em>.
     * <p>
     * A typical implementation will return an arbitrary {@link String} value made up from the given
     * <em>characters</em>, with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     *
     * @param length     The length of the resulting string.
     * @param characters A string made up of the characters that make up a possible result.
     */
    default String anyString(final int length, final String characters) {
        return Generating.anyString(this, length, characters);
    }

    /**
     * Returns a {@link String} with a <em>length</em> between 1 and 64 consisting from a predefined character set.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)} and uses
     * {@code "0123456789_abcdefghijklmnopqrstuvwxyz-ABCDEFGHIJKLMNOPQRSTUVWXYZ !#$§%&*+,.?@äöüÄÖÜß"}
     * as predefined character set.
     */
    default String anyString() {
        return Generating.anyString(this);
    }

    /**
     * Returns one of the given {@code values}.
     * <p>
     * A typical implementation will return an arbitrary value within the defined bounds,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    @SuppressWarnings("unchecked")
    default <T> T anyOf(final T... values) {
        return Generating.anyOf(this, values);
    }

    /**
     * Returns one of the given {@code enum} {@code values}.
     * <p>
     * A typical implementation will return an arbitrary value within the defined bounds,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default <E extends Enum<E>> E anyOf(final Class<E> enumClass) {
        return Generating.anyOf(this, enumClass.getEnumConstants());
    }

    /**
     * Returns maybe {@code null} or else a result generated by applying the given <em>method</em> to <em>this</em>
     * generator, assuming that <em>this</em> generator is an instance of {@code <G>}.
     * <p>
     * A typical implementation will return {@code null} with a probability of <em>possibilities</em><sup>-1</sup>.
     * <p>
     * The default implementation depends on the implementation of {@link #anyInt(int)}.
     *
     * @param <E> the type of result to be generated
     * @param <G> the assumed type of <em>this</em> generator
     * @throws ClassCastException if <em>this is not an instance of {@code <G>}</em>
     */
    @SuppressWarnings({"unchecked", "ReturnOfNull"})
    default <E, G extends Generator> E anyNullable(final int possibilities,
                                                   final Function<? super G, ? extends E> method) {
        return (0 == anyInt(possibilities)) ? null : method.apply((G) this);
    }

    /**
     * Returns an infinite {@link Stream} of elements generated by applying the given <em>method</em>
     * to <em>this</em> generator, assuming that <em>this</em> generator is an instance of {@code <G>}.
     *
     * @param <E> the type of elements to be generated
     * @param <G> the assumed type of <em>this</em> generator
     * @throws ClassCastException if <em>this is not an instance of {@code <G>}</em>
     */
    @SuppressWarnings("unchecked")
    default <E, G extends Generator> Stream<E> stream(final Function<? super G, ? extends E> method) {
        return Stream.generate(() -> method.apply((G) this));
    }
}
