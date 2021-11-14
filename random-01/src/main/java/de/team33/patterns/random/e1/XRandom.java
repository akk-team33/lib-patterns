package de.team33.patterns.random.e1;

import java.math.BigInteger;
import java.util.stream.IntStream;

public class XRandom implements BitFactory {

    private static final int FLOAT_RESOLUTION = Float.SIZE;
    private static final int DOUBLE_RESOLUTION = Double.SIZE;
    private static final int BIG_RESOLUTION = Long.SIZE;

    private final BitFactory backing;

    public XRandom(final BitFactory backing) {
        this.backing = backing;
    }

    @Override
    public final BigInteger anyBits(final int numBits) {
        return backing.anyBits(numBits);
    }

    public final boolean anyBoolean() {
        return anyBits(1).equals(BigInteger.ONE);
    }

    public final byte anyByte() {
        return anyBits(Byte.SIZE).byteValue();
    }

    public final short anyShort() {
        return anyBits(Short.SIZE).shortValue();
    }

    public final int anyInt() {
        return anyBits(Integer.SIZE).intValue();
    }

    /**
     * @return an {@code int} value between {@code zero} (incl.) and {@code bound} (excl.).
     */
    public final int anyInt(final int bound) {
        final BigInteger result = anyBigInteger(BigInteger.valueOf(bound));
        return result.intValue();
    }

    /**
     * @return an {@code int} value between {@code min} (incl.) and {@code bound} (excl.).
     */
    public final int anyInt(final int min, final int bound) {
        return anyBigInteger(BigInteger.valueOf(min), BigInteger.valueOf(bound)).intValue();
    }

    public final long anyLong() {
        return anyBits(Long.SIZE).longValue();
    }

    /**
     * @return a {@code double} value between zero (incl.) and one (excl.).
     */
    public final float anyFloat() {
        final float numerator = anyBits(FLOAT_RESOLUTION).floatValue();
        final float denominator = BigInteger.ONE.shiftLeft(FLOAT_RESOLUTION).floatValue();
        return numerator / denominator;
    }

    /**
     * @return a {@code double} value between zero (incl.) and one (excl.).
     */
    public final double anyDouble() {
        final double numerator = anyBits(DOUBLE_RESOLUTION).doubleValue();
        final double denominator = BigInteger.ONE.shiftLeft(DOUBLE_RESOLUTION).doubleValue();
        return numerator / denominator;
    }

    @SuppressWarnings("NumericCastThatLosesPrecision")
    private char anyRawChar() {
        return (char) anyBits(Character.SIZE).intValue();
    }

    /**
     * @return any {@code char} value that {@linkplain Character#isDefined(char) is defined}.
     */
    public final char anyChar() {
        char result = anyRawChar();
        while (!Character.isDefined(result)) {
            result = anyRawChar();
        }
        return result;
    }

    /**
     * @return any {@code char} value of the given {@code characters}.
     */
    public final char anyChar(final String characters) {
        return characters.charAt(anyInt(characters.length()));
    }

    /**
     * @return a {@link BigInteger} value between ZERO (incl.) and {@code bound} (excl.).
     */
    public final BigInteger anyBigInteger(final BigInteger bound) {
        if (BigInteger.ZERO.compareTo(bound) < 0) {
            final int numBits = bound.bitLength() + BIG_RESOLUTION;
            final BigInteger numerator = bound.multiply(anyBits(numBits));
            final BigInteger denominator = BigInteger.ONE.shiftLeft(numBits);
            return numerator.divide(denominator);
        }
        throw new IllegalArgumentException("<bound> must be greater than ZERO but was " + bound);
    }

    /**
     * @return a {@link BigInteger} value between {@code min} (incl.) and {@code bound} (excl.).
     */
    public final BigInteger anyBigInteger(final BigInteger min, final BigInteger bound) {
        return anyBigInteger(bound.subtract(min)).add(min);
    }

    /**
     * @return a {@link String} with the given {@code length} consisting of the given {@code characters}.
     */
    public final String anyString(final int length, final String characters) {
        if (0 <= length) {
            return IntStream.generate(() -> anyInt(characters.length()))
                            .limit(length)
                            .collect(StringBuilder::new,
                                     (sb, index) -> sb.append(characters.charAt(index)),
                                     StringBuilder::append)
                            .toString();
        }
        throw new IllegalArgumentException("<length> must be greater than or equal to zero but was " + length);
    }

    @SafeVarargs
    public final <T> T anyOf(final T... values) {
        return values[anyInt(values.length)];
    }
}
