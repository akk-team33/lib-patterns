package de.team33.patterns.random.e1;

import de.team33.patterns.production.e1.FactoryHub;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class RandomHub extends XRandom {

    public static final Byte BYTE = Byte.MAX_VALUE;
    public static final Short SHORT = Short.MAX_VALUE;
    public static final Integer INTEGER = Integer.MAX_VALUE;
    public static final Long LONG = Long.MAX_VALUE;
    public static final Float FLOAT = Float.MAX_VALUE;
    public static final Double DOUBLE = Double.MAX_VALUE;
    public static final Character CHARACTER = '*';
    public static final String STRING = "****";

    private final FactoryHub<RandomHub> backing;
    private final String stdCharacters;

    private RandomHub(final Builder builder) {
        super(builder.newBitFactory.get());
        backing = new FactoryHub<>(builder.backing, () -> this, builder.unknownTokenListener);
        stdCharacters = builder.stdCharacters;
    }

    public static Builder builder() {
        return new Builder().on(false).apply(XRandom::anyBoolean)
                            .on(true).apply(XRandom::anyBoolean)
                            .on(BYTE).apply(XRandom::anyByte)
                            .on(SHORT).apply(XRandom::anyShort)
                            .on(INTEGER).apply(XRandom::anyInt)
                            .on(LONG).apply(XRandom::anyLong)
                            .on(FLOAT).apply(XRandom::anyFloat)
                            .on(DOUBLE).apply(XRandom::anyDouble)
                            .on(CHARACTER).apply(rnd -> rnd.anyChar(rnd.stdCharacters))
                            .on(STRING).apply(rnd -> rnd.anyString(rnd.anyInt(10), rnd.stdCharacters));
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
