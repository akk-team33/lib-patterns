package de.team33.patterns.production.e1;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableMap;

/**
 * A hub of methods for creating instances of virtually any type under constant conditions.
 * <p>
 * A method is identified by a <em>token</em> (a constant) of the respective result type and called indirectly via
 * {@link #get(Object)}.
 * <p>
 * When a method is called, it is parameterized internally with a context of a certain type that is predefined
 * when the hub is initialized. Externally, apart from the identification of the method via the
 * <em>token</em>, no further parameterization is required.
 * <p>
 * In addition to the central method {@link #get(Object)} mentioned above, a hub provides additional methods
 * that are used to generate composite instances, whereby <em>tokens</em> can be used to define the generation of
 * individual elements: {@link #stream(Object)}, {@link #stream(Object, int)}, {@link #map(Object, Object, int)},
 * {@link #map(Map)}, {@link #map(Object, Function, Function)}.
 * <p>
 * Instances that contain a {@link FactoryHub} or are an extended {@link FactoryHub} itself are typically used as
 * context, so that the methods that are assigned to a {@link FactoryHub} can be used by other such methods.
 * <p>
 * Types that contain a {@link FactoryHub} or are themselves extended {@link FactoryHub}s are typically used
 * as context type, so that the methods assigned to a FactoryHub can be used by other such methods.
 * The following example shows a derivation:
 * <pre>
 * public final class FactoryHubSample extends FactoryHub&lt;FactoryHubSample&gt; {
 *
 *     // Some pre-defined tokens ...
 *     public static final Byte BYTE = Byte.MAX_VALUE;
 *     public static final Short SHORT = Short.MAX_VALUE;
 *     public static final Integer INTEGER = Integer.MAX_VALUE;
 *     public static final Long LONG = Long.MAX_VALUE;
 *
 *     private final Random random = new Random();
 *
 *     // The instantiation takes place via a builder pattern ...
 *     private FactoryHubSample(final Builder builder) {
 *         super(builder);
 *     }
 *
 *     // To get a builder that has already been pre-initialized
 *     // with the tokens defined above and corresponding methods ...
 *     public static Builder builder() {
 *         return new Builder().on(BYTE).apply(context -&gt; context.anyBits(Byte.SIZE).byteValue())
 *                             .on(SHORT).apply(context -&gt; context.anyBits(Short.SIZE).shortValue())
 *                             .on(INTEGER).apply(context -&gt; context.anyBits(Integer.SIZE).intValue())
 *                             .on(LONG).apply(context -&gt; context.anyBits(Long.SIZE).longValue());
 *     }
 *
 *     // Implementation of the method that provides the context ...
 *     &#64;Override
 *     protected final FactoryHubSample getContext() {
 *         return this;
 *     }
 *
 *     // A basic method to be provided by this context ...
 *     public final BigInteger anyBits(final int numBits) {
 *         return new BigInteger(numBits, random);
 *     }
 *
 *     // Definition of a Builder for a new instance ...
 *     public static class Builder extends Collector&lt;FactoryHubSample, Builder&gt; {
 *
 *         // Implementation of the method that provides the Builder as such for the underlying Collector ...
 *         &#64;Override
 *         protected final Builder getBuilder() {
 *             return this;
 *         }
 *
 *         // finally the typical production method of the Builder ...
 *         public final FactoryHubSample build() {
 *             return new FactoryHubSample(this);
 *         }
 *     }
 * }
 * </pre>
 *
 * @param <C> The type of the context.
 */
@SuppressWarnings("BoundedWildcard")
public abstract class FactoryHub<C> {

    @SuppressWarnings("rawtypes")
    private final Map<Object, Function> methods;
    private final Consumer<Object> unknownTokenListener;

    /**
     * Initializes a new instance.
     */
    protected FactoryHub(final Collector<C, ?> collector) {
        this.methods = unmodifiableMap(new HashMap<>(collector.methods));
        this.unknownTokenListener = collector.unknownTokenListener;
    }

    /**
     * Returns a new generic instance of {@link FactoryHub}.
     */
    @SuppressWarnings({"AnonymousInnerClass", "WeakerAccess"})
    public static <C> FactoryHub<C> instance(final Collector<C, ?> collector, final Supplier<C> contextGetter) {
        return new FactoryHub<C>(collector) {
            @Override
            protected C getContext() {
                return contextGetter.get();
            }
        };
    }

    /**
     * Returns a new generic instance of {@link Builder}.
     */
    public static <C> Builder<C> builder(final Supplier<C> contextGetter) {
        return new Builder<>(contextGetter);
    }

    @SuppressWarnings("unchecked")
    private <T> Function<C, T> getMethod(final T token) {
        return Optional.ofNullable(methods.get(token))
                       .orElseGet(() -> getDefaultMethod(token));
    }

    private <T> Function<C, T> getDefaultMethod(final T token) {
        unknownTokenListener.accept(token);
        return ctx -> token;
    }

    /**
     * Returns the context associated with this {@link FactoryHub}.
     */
    protected abstract C getContext();

    /**
     * Produces an instance of a certain type, whereby the production method to be used is identified by a
     * <em>token</em> of the result type.
     * <p>
     * The association of <em>token</em> and production method is made when the hub is initialized via a
     * {@link Collector}.
     * <p>
     * Returns the <em>token</em> itself if no method is associated with it (<em>"unknown token"</em>),
     * unless the {@linkplain Collector#setUnknownTokenListener(Consumer) initially defined} unknownTokenListener,
     * which is then called, throws an exception.
     * <p>
     * The result is always {@code null} if the <em>token</em> is {@code null}.
     *
     * @param <T> The type of the produced result.
     * @throws RuntimeException if the <em>token</em> is not associated with a production method
     *                          and the {@linkplain Collector#setUnknownTokenListener(Consumer) initially defined}
     *                          unknownTokenListener throws one.
     * @see FactoryUtil#ACCEPT_UNKNOWN_TOKEN
     * @see FactoryUtil#DENY_UNKNOWN_TOKEN
     * @see FactoryUtil#LOG_UNKNOWN_TOKEN
     */
    @SuppressWarnings("ReturnOfNull")
    public final <T> T get(final T token) {
        return (null == token) ? null : getMethod(token).apply(getContext());
    }

    /**
     * Produces an infinite (!) {@link Stream} of {@link #get(Object) newly produced} elements af a certain type,
     * whereby the production method to be used for each element is identified by a <em>token</em> of the same type.
     * <p>
     * Does the same as {@code Stream.generate(() -> get(token))}.
     *
     * @param <T> The type of the produced elements.
     * @throws RuntimeException if the <em>token</em> is not associated with a production method
     *                          and the initially defined unknownTokenListener throws one.
     * @see #get(Object)
     * @see #stream(Object, int)
     */
    public final <T> Stream<T> stream(final T token) {
        return Stream.generate(() -> get(token));
    }

    /**
     * Produces a {@link Stream} with limited length of {@link #get(Object) newly produced} elements
     * af a certain type, whereby the production method to be used for each element is identified by a
     * <em>token</em> of the same type.
     * <p>
     * Does the same as {@code Stream.generate(() -> get(token)).limit(length)}.
     *
     * @param <T> The type of the produced elements.
     * @throws RuntimeException if the <em>token</em> is not associated with a production method
     *                          and the initially defined unknownTokenListener throws one.
     * @see #get(Object)
     * @see #stream(Object)
     */
    public final <T> Stream<T> stream(final T token, final int length) {
        return stream(token).limit(length);
    }

    /**
     * Produces a {@link Map} of a given {@code size} whose keys are {@link #get(Object) produced} based on
     * the given {@code keyToken} and whose values are {@link #get(Object) produced} based on the given
     * {@code valueToken}.
     *
     * @param <K> The type of keys of the resulting {@link Map}.
     * @param <V> The type of values of the resulting {@link Map}.
     * @throws RuntimeException if a <em>token</em> is not associated with a production method
     *                          and the initially defined unknownTokenListener throws one.
     * @see #get(Object)
     * @see #map(Map)
     */
    public final <K, V> Map<K, V> map(final K keyToken, final V valueToken, final int size) {
        return stream(keyToken).distinct()
                               .limit(size)
                               .collect(Collectors.toMap(key -> key, key -> get(valueToken)));
    }

    /**
     * Produces a {@link Map} based on a given {@code template} {@link Map}.
     * The keys of the {@code template} are adopted unchanged in the result and the values are
     * {@linkplain #get(Object) produced} based on the values (<em>tokens</em>) of the {@code template}.
     *
     * @param <K> The type of keys of the resulting {@link Map}.
     * @param <V> The type of values of the resulting {@link Map}.
     * @throws RuntimeException if a <em>token</em> is not associated with a production method
     *                          and the initially defined unknownTokenListener throws one.
     * @see #get(Object)
     * @see #map(Object, Object, int)
     */
    public final <K, V> Map<K, V> map(final Map<K, V> template) {
        return template.entrySet()
                       .stream()
                       .collect(Collectors.toMap(Map.Entry::getKey,
                                                 entry -> get(entry.getValue())));
    }

    /**
     * Produces a result of a certain type from a {@code template} of a certain type.
     * First, a {@link Map} is generated from the {@code template} that contains its properties,
     * which are interpreted as <em>tokens</em>. This is translated via {@link #map(Map)}.
     * The resulting {@link Map} is then used to generate the result.
     *
     * @param template The template.
     * @param toMap    A method to create a {@link Map} containing the properties of the template.
     * @param reMap    A method to create the result from a {@link Map}.
     * @param <T>      The type of the template.
     * @param <R>      The type of the result. Mostly but not necessarily the same as the template type.
     * @throws RuntimeException if a <em>token</em> is not associated with a production method
     *                          and the initially defined unknownTokenListener throws one.
     * @see #get(Object)
     */
    public final <T, R> R map(final T template,
                              final Function<T, Map<?, ?>> toMap,
                              final Function<Map<?, ?>, R> reMap) {
        return reMap.apply(map(toMap.apply(template)));
    }

    /**
     * Abstracts a tool for preparing and initializing a {@link FactoryHub} intended for extension into a
     * <em>Builder</em> implementation.
     *
     * @param <C> The type of the context of a resulting {@link FactoryHub}.
     * @param <B> The type of the <em>Builder</em> implementation that utilizes this.
     */
    public abstract static class Collector<C, B> {

        private final Map<Object, Function<C, ?>> methods = new HashMap<>(0);
        private Consumer<Object> unknownTokenListener = FactoryUtil.ACCEPT_UNKNOWN_TOKEN;

        /**
         * Returns the <em>Builder</em> instance that utilizes this.
         * Usually (but not necessarily) this itself in its extended representation.
         */
        protected abstract B getBuilder();

        /**
         * Provides a two-step builder pattern to add a specific factory method and map it to a token:
         * <p>
         * Takes a token of a certain type as a parameter and returns a {@link Function} that will associate that
         * token with a factory method (also a {@link Function}) and will return
         * {@linkplain #getBuilder() the builder}.
         *
         * @param <T> The type of the token and also the result type of the production method.
         */
        public final <T> Function<Function<C, T>, B> on(final T token) {
            return method -> {
                methods.put(token, method);
                return getBuilder();
            };
        }

        /**
         * Defines a given consumer as the unknownTokenListener of a FactoryHub resulting from this Collector.
         * <p>
         * Unless otherwise specified, {@link FactoryUtil#ACCEPT_UNKNOWN_TOKEN} is used as the unknownTokenListener.
         *
         * @return {@linkplain #getBuilder() the builder}.
         */
        public final B setUnknownTokenListener(final Consumer<Object> consumer) {
            this.unknownTokenListener = consumer;
            return getBuilder();
        }
    }

    /**
     * A generic Builder implementation.
     *
     * @param <C> The type of the context of a resulting {@link FactoryHub}.
     * @see #builder(Supplier)
     */
    public static final class Builder<C> extends Collector<C, Builder<C>> {

        private final Supplier<C> contextGetter;

        private Builder(final Supplier<C> contextGetter) {
            this.contextGetter = contextGetter;
        }

        /**
         * @return {@code this}
         */
        @Override
        protected final Builder<C> getBuilder() {
            return this;
        }

        /**
         * Returns a new instance of {@link FactoryHub}.
         */
        public final FactoryHub<C> build() {
            return instance(this, contextGetter);
        }
    }
}
