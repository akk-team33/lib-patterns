package de.team33.patterns.arbitrary.mimas;

import de.team33.patterns.typing.proteus.Type;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Optional;
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

    String CHARACTERS = "0123456789_abcdefghijklmnopqrstuvwxyz-ABCDEFGHIJKLMNOPQRSTUVWXYZ !#$§%&*+,.?@äöüÄÖÜß";
    int MAX_STRING_LENGTH = 32;

    /**
     * @deprecated use {@link Basic} as basic implementation instead.
     */
    @Deprecated(forRemoval = true)
    static BigInteger anyBits(final int numBits, final Random random) {
        return new BigInteger(numBits, random);
    }

    /**
     * @deprecated use {@link #by(Random)} instead.
     */
    @Deprecated(forRemoval = true)
    static Generator of(final Random random) {
        return by(random);
    }

    /**
     * <b>Utility method:</b>
     * Returns a new instance backed by a given {@link Random}.
     */
    static Generator by(final Random random) {
        return new Basic(random);
    }

    /**
     * <b>Utility method:</b>
     * Returns a new instance backed by a new {@link SecureRandom}.
     */
    static Generator byDefault() {
        return new Basic();
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
     * A typical implementation will return an arbitrary {@code char} value,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)} and returns
     * one of {@link #CHARACTERS}.
     */
    default char anyChar() {
        return Generating.anyChar(this, CHARACTERS);
    }

    /**
     * Returns a {@code char} value from the given <em>characters</em>.
     * <p>
     * A typical implementation will return an arbitrary {@code char} value,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default char anyChar(final String characters) {
        return Generating.anyChar(this, characters);
    }

    /**
     * Returns a {@link String} with a length between <em>minLength</em> and <em>maxLength</em> (both inclusive)
     * consisting of the given <em>characters</em>.
     * <p>
     * A typical implementation will return an arbitrary {@link String} value,
     * with each possible length being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     *
     * @throws IllegalArgumentException if not {@code zero} &lt;= <em>minLength</em> &lt;= <em>maxLength</em>
     */
    default String anyString(final int minLength, final int maxLength, final CharSequence characters) {
        return Generating.anyString(this, minLength, maxLength, characters);
    }

    /**
     * Returns a {@link String} with a length between <em>minLength</em> and <em>maxLength</em> (both inclusive)
     * consisting of predefined {@link #CHARACTERS}.
     * <p>
     * A typical implementation will return an arbitrary {@link String} value,
     * with each possible length being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     *
     * @throws IllegalArgumentException if not {@code zero} &lt;= <em>minLength</em> &lt;= <em>maxLength</em>
     */
    default String anyString(final int minLength, final int maxLength) {
        return Generating.anyString(this, minLength, maxLength, CHARACTERS);
    }

    /**
     * Returns a {@link String} with a given <em>length</em> consisting of the given <em>characters</em>.
     * <p>
     * A typical implementation will return an arbitrary {@link String} value,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     *
     * @throws IllegalArgumentException if not {@code zero} &lt;= <em>length</em>
     */
    default String anyString(final int length, final String characters) {
        return Generating.anyString(this, length, characters);
    }

    /**
     * Returns a {@link String} with a given <em>length</em> consisting of predefined {@link #CHARACTERS}.
     * <p>
     * A typical implementation will return an arbitrary {@link String} value,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     *
     * @throws IllegalArgumentException if not {@code zero} &lt;= <em>length</em>
     */
    default String anyString(final int length) {
        return Generating.anyString(this, length, CHARACTERS);
    }

    /**
     * Returns a {@link String} with a length between {@code zero} and {@link #MAX_STRING_LENGTH} (both inclusive)
     * consisting of the given <em>characters</em>.
     * <p>
     * A typical implementation will return an arbitrary {@link String} value,
     * with each possible length being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default String anyString(final String characters) {
        return Generating.anyString(this, 0, MAX_STRING_LENGTH, characters);
    }

    /**
     * Returns a {@link String} with a length between {@code zero} and {@link #MAX_STRING_LENGTH} (both inclusive)
     * consisting of predefined {@link #CHARACTERS}.
     * <p>
     * A typical implementation will return an arbitrary {@link String} value,
     * with each possible length being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default String anyString() {
        return Generating.anyString(this, 0, MAX_STRING_LENGTH, CHARACTERS);
    }

    /**
     * Returns one of the given <em>values</em>.
     * <p>
     * A typical implementation will return an arbitrary value,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    @SuppressWarnings("unchecked")
    default <T> T anyOf(final T... values) {
        return Generating.anyOf(this, values);
    }

    /**
     * Returns one of the {@code enum} {@link Class#getEnumConstants() values} of the given <em>enumClass</em>.
     * <p>
     * A typical implementation will return an arbitrary value,
     * with each possible value being equally probable.
     * <p>
     * The default implementation depends on the implementation of {@link #anyBits(int)}.
     */
    default <E extends Enum<E>> E anyOf(final Class<E> enumClass) {
        return Generating.anyOf(this, enumClass.getEnumConstants());
    }

    /**
     * Returns a {@link Map} representing a data set based on a given data set <em>description</em>.
     * <p>
     * A typical implementation proceeds as follows:
     * <p>
     * For each entry in the <em>description</em>, <em>this</em> generator is searched for a public,
     * parameterless instance method capable of returning a value that is compatible with the required type.
     * Object specific methods like toString() or hashCode() are ignored.
     * An entry in the resulting map is then formed from the description's key and the value returned by the method.
     * If no suitable method is found, the corresponding value in the result is {@code null}.
     * <p>
     * If more than one suitable method exists, they are prioritized according to the following criteria:
     * <ol>
     *     <li>If "name" is the key of the description entry, a method named "anyName" is preferred.</li>
     *     <li>If {@code Type.of(String.class)} is the type of the description entry,
     *     a method named "anyString" is preferred.</li>
     *     <li>A method whose name starts with "any" is preferred.</li>
     *     <li>If {@code Type.of(CharSequence.class)} is the type of the description entry,
     *     a method whose result is exactly of type {@code CharSequence} is preferred.</li>
     * </ol>
     * <p>
     * If, beyond this, no method could be uniquely determined, an arbitrary one is used.
     * <p>
     * <b>NOTE:</b> this method may not work properly if <em>this</em> {@link Generator} implementation is generic.
     */
    default Map<String, Object> anyDataSet(final Map<String, Type<?>> description) {
        return new DataSetup(this).generate(description);
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
     * @throws ClassCastException       if <em>this</em> is not an instance of {@code <G>}
     * @throws IllegalArgumentException if not {@code zero} &lt; <em>possibilities</em>
     */
    @SuppressWarnings({"unchecked", "ReturnOfNull"})
    default <E, G extends Generator> E anyNullable(final int possibilities, final Function<? super G, E> method) {
        return (0 == anyInt(possibilities)) ? null : method.apply((G) this);
    }

    /**
     * Returns an {@link Optional} result generated by applying the given <em>method</em> to <em>this</em>
     * generator, assuming that <em>this</em> generator is an instance of {@code <G>}.
     * <p>
     * A typical implementation will return {@link Optional#empty()} with a probability of
     * <em>possibilities</em><sup>-1</sup>.
     * <p>
     * The default implementation depends on the implementation of {@link #anyInt(int)}.
     *
     * @param <E> the type of result to be generated
     * @param <G> the assumed type of <em>this</em> generator
     * @throws ClassCastException       if <em>this</em> is not an instance of {@code <G>}
     * @throws IllegalArgumentException if not {@code zero} &lt; <em>possibilities</em>
     */
    @SuppressWarnings("unchecked")
    default <E, G extends Generator> Optional<E> anyOptional(final int possibilities,
                                                             final Function<? super G, ? extends E> method) {
        return Stream.generate(() -> (E) method.apply((G) this))
                     .limit(anyInt(possibilities))
                     .findAny();
    }

    /**
     * Returns an infinite {@link Stream} of elements generated by applying the given <em>method</em>
     * to <em>this</em> generator, assuming that <em>this</em> generator is an instance of {@code <G>}.
     *
     * @param <E> the type of elements to be generated
     * @param <G> the assumed type of <em>this</em> generator
     * @throws ClassCastException if <em>this</em> is not an instance of {@code <G>}
     */
    @SuppressWarnings("unchecked")
    default <E, G extends Generator> Stream<E> stream(final Function<? super G, ? extends E> method) {
        return Stream.generate(() -> method.apply((G) this));
    }

    /**
     * A basic implementation of a {@link Generator}
     */
    class Basic implements Generator {

        private final Random random;

        /**
         * Creates an instance backed by a new {@link SecureRandom}.
         */
        public Basic() {
            this(new SecureRandom());
        }

        /**
         * Creates an instance backed by a given {@link Random}.
         */
        public Basic(final Random random) {
            this.random = random;
        }

        /**
         * {@inheritDoc}
         * <p>
         * This is a typical implementation.
         */
        @Override
        public final BigInteger anyBits(final int numBits) {
            return new BigInteger(numBits, random);
        }
    }
}
