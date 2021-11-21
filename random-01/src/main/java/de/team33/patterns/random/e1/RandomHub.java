package de.team33.patterns.random.e1;

import de.team33.patterns.production.e1.FactoryHub;

import java.math.BigInteger;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class RandomHub implements XFactory {

    /**
     * Predefined <em>token</em> that is linked to the {@link #anyByte()} method in a {@link RandomHub} by default.
     */
    public static final Byte BYTE = Byte.MAX_VALUE;

    /**
     * Predefined <em>token</em> that is linked to the {@link #anyShort()} method in a {@link RandomHub} by default.
     */
    public static final Short SHORT = Short.MAX_VALUE;

    /**
     * Predefined <em>token</em> that is linked to the {@link #anyInt()} method in a {@link RandomHub} by default.
     */
    public static final Integer INTEGER = Integer.MAX_VALUE;

    /**
     * Predefined <em>token</em> that is linked to the {@link #anyLong()} method in a {@link RandomHub} by default.
     */
    public static final Long LONG = Long.MAX_VALUE;

    /**
     * Predefined <em>token</em> that is linked to the {@link #anyFloat()} method in a {@link RandomHub} by default.
     */
    public static final Float FLOAT = Float.MAX_VALUE;

    /**
     * Predefined <em>token</em> that is linked to the {@link #anyDouble()} method in a {@link RandomHub} by default.
     */
    public static final Double DOUBLE = Double.MAX_VALUE;

    /**
     * Predefined <em>token</em> that is linked to the {@link #anyChar(String)} method using the
     * {@link Builder#setStdCharacters(String) standard characters} in a {@link RandomHub} by default.
     */
    public static final Character CHARACTER = 'ø';

    /**
     * Predefined <em>token</em> that is linked to the {@link #anyString(int, String)} method using a random length
     * and the {@link Builder#setStdCharacters(String) standard characters} in a {@link RandomHub} by default.
     */
    public static final String STRING = "øøøø";

    private final BitFactory bitFactory;
    private final FactoryHub<RandomHub> backing;
    private final String stdCharacters;

    private RandomHub(final Builder builder) {
        bitFactory = builder.newBitFactory.get();
        backing = new FactoryHub<>(builder.backing, () -> this, builder.unknownTokenListener);
        stdCharacters = builder.stdCharacters;
    }

    public static Builder builder() {
        return new Builder().on(false).apply(XFactory::anyBoolean)
                            .on(true).apply(XFactory::anyBoolean)
                            .on(BYTE).apply(XFactory::anyByte)
                            .on(SHORT).apply(XFactory::anyShort)
                            .on(INTEGER).apply(XFactory::anyInt)
                            .on(LONG).apply(XFactory::anyLong)
                            .on(FLOAT).apply(XFactory::anyFloat)
                            .on(DOUBLE).apply(XFactory::anyDouble)
                            .on(CHARACTER).apply(rnd -> rnd.anyChar(rnd.stdCharacters))
                            .on(STRING).apply(rnd -> rnd.anyString(rnd.anyInt(1 + rnd.anyInt(24)), rnd.stdCharacters));
    }

    public final <R> R any(final R token) {
        return backing.get(token);
    }

    public final <R> Stream<R> stream(final R token) {
        return backing.stream(token);
    }

    public final <R> Stream<R> stream(final R token, final int length) {
        return backing.stream(token, length);
    }

    @Override
    public final BigInteger anyBits(final int numBits) {
        return bitFactory.anyBits(numBits);
    }

    @SuppressWarnings("FieldHasSetterButNoGetter")
    public static class Builder {

        private static final String STD_CHARACTERS =
                "abcdefghijklmnopqrstuvwxyz-ABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789 @äöüÄÖÜß!§$%&";

        private final FactoryHub.Collector<RandomHub> backing;
        private Consumer<Object> unknownTokenListener = FactoryHub.ACCEPT_UNKNOWN_TOKEN;

        private String stdCharacters = STD_CHARACTERS;
        private Supplier<BitFactory> newBitFactory = () -> BitFactory.using(new Random());

        private Builder() {
            backing = new FactoryHub.Collector<>();
        }

        public final <T> Function<Function<RandomHub, T>, Builder> on(final T token) {
            return backing.on(token, this);
        }

        public final Builder setStdCharacters(final String characters) {
            this.stdCharacters = characters;
            return this;
        }

        public final Builder setNewBitFactory(final Supplier<BitFactory> newBitFactory) {
            this.newBitFactory = newBitFactory;
            return this;
        }

        public final Builder setNewRandom(final Supplier<Random> newRandom) {
            return setNewBitFactory(() -> BitFactory.using(newRandom.get()));
        }

        public Builder setUnknownTokenListener(final Consumer<Object> unknownTokenListener) {
            this.unknownTokenListener = unknownTokenListener;
            return this;
        }

        public final RandomHub build() {
            return new RandomHub(this);
        }
    }
}
