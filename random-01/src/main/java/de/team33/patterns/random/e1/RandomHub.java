package de.team33.patterns.random.e1;

import de.team33.patterns.producing.e1.FactoryHub;

import java.math.BigInteger;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RandomHub {

    public static final Byte BYTE = Byte.MAX_VALUE;
    public static final Short SHORT = Short.MAX_VALUE;
    public static final Integer INTEGER = Integer.MAX_VALUE;
    public static final Long LONG = Long.MAX_VALUE;
    public static final Float FLOAT = Float.MAX_VALUE;
    public static final Double DOUBLE = Double.MAX_VALUE;
    public static final Character CHARACTER = '*';
    public static final String STRING = "****";

    private final FactoryHub<RandomHub> backing;
    private final Random random = new Random();
    private final String stdCharacters;

    private RandomHub(final Builder builder) {
        backing = new FactoryHub<>(builder.backing, () -> this);
        stdCharacters = builder.stdCharacters;
    }

    public static Builder builder() {
        return new Builder().on(false).apply(rnd -> rnd.anyOf(false, true))
                            .on(true).apply(rnd -> rnd.anyOf(false, true))
                            .on(BYTE).apply(rnd -> rnd.anyBits(Byte.SIZE).byteValue())
                            .on(SHORT).apply(rnd -> rnd.anyBits(Short.SIZE).shortValue())
                            .on(INTEGER).apply(rnd -> rnd.anyBits(Integer.SIZE).intValue())
                            .on(LONG).apply(rnd -> rnd.anyBits(Long.SIZE).longValue())
                            .on(FLOAT).apply(rnd -> rnd.random.nextFloat())
                            .on(DOUBLE).apply(rnd -> rnd.random.nextDouble())
                            .on(CHARACTER).apply(rnd -> rnd.anyChar(rnd.stdCharacters))
                            .on(STRING).apply(rnd -> rnd.anyString(rnd.stdCharacters, rnd.anyInt(1, 65)));
    }

    public final <R> R any(final R template) {
        return backing.create(template);
    }

    public final <R> Stream<R> stream(final R template) {
        return backing.stream(template);
    }

    public final BigInteger anyBits(final int numBits) {
        return new BigInteger(numBits, random);
    }

    public final BigInteger anyBigInteger(final BigInteger bound) {
        if (BigInteger.ZERO.compareTo(bound) < 0) {
            final int numBits = bound.bitLength() + 32;
            return bound.multiply(anyBits(numBits))
                        .divide(BigInteger.ONE.shiftLeft(numBits));
        }
        throw new IllegalArgumentException("<bound> must be greater than ZERO but was " + bound);
    }

    public final BigInteger anyBigInteger(final BigInteger min, final BigInteger bound) {
        return anyBigInteger(bound.subtract(min)).add(min);
    }

    public final int anyInt(final int bound) {
        return anyBigInteger(BigInteger.valueOf(bound)).intValue();
    }

    public final int anyInt(final int min, final int bound) {
        return anyBigInteger(BigInteger.valueOf(min), BigInteger.valueOf(bound)).intValue();
    }

    public final char anyChar(final String characters) {
        return characters.charAt(anyInt(characters.length()));
    }

    public final String anyString(final String characters, final int length) {
        return IntStream.generate(() -> anyInt(characters.length()))
                        .limit(length)
                        .collect(StringBuilder::new,
                                 (sb, index) -> sb.append(characters.charAt(index)),
                                 StringBuilder::append)
                        .toString();
    }

    @SafeVarargs
    public final <R> R anyOf(final R... values) {
        return values[anyInt(values.length)];
    }

    public static class Builder {

        private static final String STD_CHARACTERS =
                "abcdefghijklmnopqrstuvwxyz-ABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789 @äöüÄÖÜß!§$%&";

        private final FactoryHub.Collector<RandomHub> backing;

        private String stdCharacters = STD_CHARACTERS;

        private Builder() {
            backing = new FactoryHub.Collector<>();
        }

        public final <T> Function<Function<RandomHub, T>, Builder> on(final T template) {
            return backing.on(template, this);
        }

        public final Builder setStdCharacters(final String characters) {
            this.stdCharacters = characters;
            return this;
        }

        public final RandomHub build() {
            return new RandomHub(this);
        }
    }
}
