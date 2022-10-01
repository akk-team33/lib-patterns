package de.team33.patterns.random.e1;

import de.team33.patterns.production.e1.FactoryHub;

import java.math.BigInteger;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * A hub of methods for creating random instances of virtually any type under constant conditions.
 * It is backed by a {@link FactoryHub}.
 * <p>
 * It is primarily intended as a random generator.
 * For certain purposes, however, there can also be deterministic or mixed implementations.
 *
 * @deprecated Further development is discontinued and this package/module may be removed in a future release.
 * Successor edition is the module <em>random-mimas</em>.
 */
@Deprecated
public final class RandomHub implements XRandom {

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
    public static final String STRING = "øøøøøøøøøøøøøøøø";

    private final BitFactory bitFactory;

    private final FactoryHub<RandomHub> backing;
    private final String stdCharacters;

    private RandomHub(final Builder builder) {
        backing = FactoryHub.instance(builder, () -> this);
        bitFactory = builder.newBitFactory.get();
        stdCharacters = builder.stdCharacters;
    }

    /**
     * Returns a new instance of {@link Builder} that is predefined with some tokens and methods:
     *
     * <ul>
     *     <li>{@link #BYTE}</li>
     *     <li>{@link #SHORT}</li>
     *     <li>{@link #INTEGER}</li>
     *     <li>{@link #LONG}</li>
     *     <li>{@link #FLOAT}</li>
     *     <li>{@link #DOUBLE}</li>
     *     <li>{@link #CHARACTER}</li>
     *     <li>{@link #STRING}</li>
     * </ul>
     * <p>
     * In addition, it links any boolean value as <em>token</em> with {@link #anyBoolean()} by default.
     * <p>
     * Each of those associations may be overridden.
     */
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
                .on(STRING).apply(rnd -> rnd.anyString(rnd.anyInt(1 + rnd.anyInt(STRING.length())),
                        rnd.stdCharacters));
    }

    /**
     * Produces a random instance of a certain type, whereby the production method to be used is identified by a
     * <em>token</em> of the result type.
     *
     * @see FactoryHub#get(Object)
     */
    public final <R> R any(final R token) {
        return backing.get(token);
    }

    /**
     * Produces an infinite (!) {@link Stream} of {@link #any(Object) newly produced} elements af a certain type,
     * whereby the production method to be used for each element is identified by a <em>token</em> of the same type.
     *
     * @see FactoryHub#stream(Object)
     */
    public final <R> Stream<R> stream(final R token) {
        return backing.stream(token);
    }

    /**
     * Produces a {@link Stream} with limited length of {@link #any(Object) newly produced} elements
     * af a certain type, whereby the production method to be used for each element is identified by a
     * <em>token</em> of the same type.
     *
     * @see FactoryHub#stream(Object, int)
     */
    public final <R> Stream<R> stream(final R token, final int length) {
        return backing.stream(token, length);
    }

    /**
     * @see FactoryHub#map(Object, Object, int)
     */
    public final <K, V> Map<K, V> map(final K keyToken, final V valueToken, final int size) {
        return backing.map(keyToken, valueToken, size);
    }

    /**
     * @see FactoryHub#map(Map)
     */
    public final <K, V> Map<K, V> map(final Map<K, V> template) {
        return backing.map(template);
    }

    /**
     * @see FactoryHub#map(Object, Function, Function)
     */
    public final <T, R> R map(final T template,
                              final Function<T, Map<?, ?>> toMap,
                              final Function<Map<?, ?>, R> reMap) {
        return backing.map(template, toMap, reMap);
    }

    @Override
    public final BigInteger anyBits(final int numBits) {
        return bitFactory.anyBits(numBits);
    }

    /**
     * A Builder implementation for new instances of {@link RandomHub} that is based on {@link FactoryHub.Collector}.
     *
     * @see #builder()
     * @deprecated Further development is discontinued and this package/module may be removed in a future release.
     * Successor edition is the module <em>random-mimas</em>.
     */
    @Deprecated
    @SuppressWarnings("FieldHasSetterButNoGetter")
    public static class Builder extends FactoryHub.Collector<RandomHub, Builder> {

        private static final String STD_CHARACTERS =
                "abcdefghijklmnopqrstuvwxyz-ABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789 @äöüÄÖÜß!§$%&";

        private String stdCharacters = STD_CHARACTERS;
        private Supplier<BitFactory> newBitFactory = () -> BitFactory.using(new Random());

        private Builder() {
        }

        /**
         * @return {@code this}
         */
        @Override
        protected final Builder getBuilder() {
            return this;
        }

        /**
         * Defines the standard set of characters that is used for {@link #CHARACTER} and {@link #STRING}.
         * <p>
         * Unless otherwise specified, an internal constant is used.
         */
        public final Builder setStdCharacters(final String characters) {
            this.stdCharacters = characters;
            return this;
        }

        /**
         * Specifies a producer for a {@link BitFactory} that will underlie a resulting instance of {@link RandomHub}.
         * <p>
         * Overwrites a previous definition by {@link #setNewRandom(Supplier)}, if one has been made.
         *
         * @see #setNewRandom(Supplier)
         */
        public final Builder setNewBitFactory(final Supplier<BitFactory> newBitFactory) {
            this.newBitFactory = newBitFactory;
            return this;
        }

        /**
         * Specifies a producer for a {@link Random} instance that will underlie a resulting instance of
         * {@link RandomHub}.
         * <p>
         * Overwrites a previous definition by {@link #setNewBitFactory(Supplier)}, if one has been made.
         *
         * @see #setNewBitFactory(Supplier)
         */
        public final Builder setNewRandom(final Supplier<? extends Random> newRandom) {
            return setNewBitFactory(() -> BitFactory.using(newRandom.get()));
        }

        /**
         * Returns a new instance of {@link RandomHub}.
         */
        public final RandomHub build() {
            return new RandomHub(this);
        }
    }
}
