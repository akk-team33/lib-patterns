package de.team33.patterns.production.e1;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * A hub of methods for creating instances of virtually any type.
 * <p>
 * A method is identified by a <em>token</em> (a constant) of the respective result type and called indirectly via
 * {@link #get(Object)}.
 * <p>
 * When a method is called, it is parameterized internally with a context of a certain type.
 * This is specified when the hub is initialized. Externally, apart from the identification of the method via the
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
 * A class intended to serve as the context of a {@link FactoryHub} will either contain a {@link FactoryHub}
 * or be derived from it. The following example shows a derivation:
 * <pre>
 * public final class FactoryHubSample extends FactoryHub&lt;FactoryHubSample&gt; {
 *
 *     // Some pre-defined tokens ...
 *     public static final Byte BYTE = Byte.MAX_VALUE;
 *     public static final Short SHORT = Short.MAX_VALUE;
 *     public static final Integer INTEGER = Integer.MAX_VALUE;
 *
 *     private final Random random = new Random();
 *
 *     // The instantiation takes place via a builder pattern ...
 *     private FactoryHubSample(final Builder builder) {
 *         super(builder.collector, FactoryHubSample.class);
 *     }
 *
 *     // To get a builder that has already been pre-initialized
 *     // with the tokens defined above and corresponding methods ...
 *     public static Builder builder() {
 *         return new Builder().on(BYTE).apply(ctx -&gt; ctx.createBits(Byte.SIZE).byteValue())
 *                             .on(SHORT).apply(ctx -&gt; ctx.createBits(Short.SIZE).shortValue())
 *                             .on(INTEGER).apply(ctx -&gt; ctx.createBits(Integer.SIZE).intValue());
 *     }
 *
 *     // A basic method to be provided by the context ...
 *     public final BigInteger createBits(final int numBits) {
 *         return new BigInteger(numBits, random);
 *     }
 *
 *     // Definition of the builder ...
 *     public static class Builder {
 *
 *         // ... which in this case contains a FactoryHub.Collector (it could also be derived from it instead)
 *         private final FactoryHub.Collector&lt;FactoryHubSample&gt; collector = new FactoryHub.Collector&lt;&gt;();
 *
 *         // In order to implement the two-stage builder pattern as proposed by the collector,
 *         // this method delegates to the collector ...
 *         public final &lt;T&gt; Function&lt;Function&lt;FactoryHubSample, T&gt;, Builder&gt; on(final T token) {
 *             return collector.on(token, this);
 *         }
 *
 *         // finally the typical production method ...
 *         public final FactoryHubSample build() {
 *             return new FactoryHubSample(this);
 *         }
 *     }
 * }
 * </pre>
 *
 * @param <C> The type of the context.
 */
public class FactoryHub<C> {

    private static final String ILLEGAL_CONTEXT_TYPE =
            "This instance cannot be viewed as a context of the specified type!%n" +
            "- type of this: %s%n" +
            "- context type: %s%n";
    private static final String ILLEGAL_TEMPLATE =
            "unknown token:%n" +
            "- type of token   : %s%n" +
            "- value* of token : %s%n" +
            "*(string representation)";

    public static Consumer<Object> IGNORE_UNKNOWN_TOKEN = token -> {
    };
    public static Consumer<Object> DENY_UNKNOWN_TOKEN = token -> {
        throw new IllegalArgumentException(format(ILLEGAL_TEMPLATE, classOf(token), token));
    };

    @SuppressWarnings("rawtypes")
    private final Map<Object, Function> methods;
    private final Supplier<C> context;
    private final Consumer<Object> unknownTokenListener;

    /**
     * Initializes a new instance.
     * <p>
     * This variant is used when a {@link FactoryHub} is to be used as part of its context.
     *
     * @param collector            A fully prepared {@link Collector} that associates the constants to be supported
     *                             with appropriate methods.
     * @param context              A {@link Supplier} that provides the context as soon as it is actually needed.
     *                             It is expected that every call to {@link Supplier#get()} will return the same
     *                             context instance!
     * @param unknownTokenListener A {@link Consumer} that is called with the <em>token</em> as parameter
     *                             when an unknown <em>token</em> is used.
     * @see #FactoryHub(Collector, Class, Consumer)
     */
    public FactoryHub(final Collector<C> collector,
                      final Supplier<C> context,
                      final Consumer<Object> unknownTokenListener) {
        this.methods = copyOf(collector);
        this.context = context;
        this.unknownTokenListener = unknownTokenListener;
    }

    /**
     * Initializes a new instance.
     * <p>
     * This variant is used when a context type extends a {@link FactoryHub}.
     *
     * @param collector            A fully prepared {@link Collector} that associates the <em>tokens</em> to be
     *                            supported with appropriate methods.
     * @param contextType          The {@link Class} that represents the context type that actually extends this
     *                             {@link FactoryHub}.
     * @param unknownTokenListener A {@link Consumer} that is called with the <em>token</em> as parameter
     *                             when an unknown <em>token</em> is used.
     * @throws IllegalArgumentException if the given context type does not actually extend this {@link FactoryHub}.
     * @see #FactoryHub(Collector, Supplier, Consumer)
     */
    protected FactoryHub(final Collector<C> collector,
                         final Class<C> contextType,
                         final Consumer<Object> unknownTokenListener) {
        if (getClass().isAssignableFrom(contextType)) {
            this.methods = copyOf(collector);
            this.context = () -> contextType.cast(this);
            this.unknownTokenListener = unknownTokenListener;
        } else {
            throw new IllegalArgumentException(format(ILLEGAL_CONTEXT_TYPE, getClass(), contextType));
        }
    }

    @SuppressWarnings("rawtypes")
    private static <C> Map<Object, Function> copyOf(final Collector<C> collector) {
        return Collections.unmodifiableMap(new HashMap<>(collector.methods));
    }

    @SuppressWarnings("ReturnOfNull")
    private static Class<?> classOf(final Object subject) {
        return (subject == null) ? null : subject.getClass();
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
     * Produces an instance of a certain type, whereby the production method to be used is identified by a
     * <em>token</em> of the result type.
     * <p>
     * The association of <em>token</em> and production method is made when the hub is initialized via a
     * {@link Collector}.
     * <p>
     * The result is always {@code null} if the <em>token</em> is {@code null}.
     *
     * @param <T> The type of the produced result.
     * @throws IllegalArgumentException if the <em>token</em> is not associated with a production method.
     */
    @SuppressWarnings("ReturnOfNull")
    public final <T> T get(final T token) {
        return (null == token) ? null : getMethod(token).apply(context.get());
    }

    /**
     * Produces an infinite (!) {@link Stream} of {@link #get(Object) newly produced} elements af a certain type,
     * whereby the production method to be used for each element is identified by a <em>token</em> of the same type.
     *
     * @param <T> The type of the produced elements.
     * @throws IllegalArgumentException if the <em>token</em> is not associated with a production method.
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
     *
     * @param <T> The type of the produced elements.
     * @throws IllegalArgumentException if the <em>token</em> is not associated with a production method.
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
     * @throws IllegalArgumentException if a <em>token</em> is not associated with a production method.
     * @see #get(Object)
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
     * @throws IllegalArgumentException if a <em>token</em> is not associated with a production method.
     * @see #get(Object)
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
     * @throws IllegalArgumentException if a <em>token</em> is not associated with a production method.
     * @see #get(Object)
     */
    public final <T, R> R map(final T template,
                              final Function<T, Map<?, ?>> toMap,
                              final Function<Map<?, ?>, R> reMap) {
        return reMap.apply(map(toMap.apply(template)));
    }

    /**
     * A tool for preparing and initializing a {@link FactoryHub}.
     * <p>
     * It provides a two-stage builder pattern.
     * A {@link Collector} is not a complete builder, but it can serve as the basis for the implementation of one,
     * e.g. a context builder. An example of this can be found in the description of {@link FactoryHub}.
     *
     * @param <C> The type of the context.
     */
    public static class Collector<C> {

        private final Map<Object, Function<C, ?>> methods = new HashMap<>();

        /**
         * Offers a two-step builder pattern:
         * <p>
         * Takes a token of a certain type as a parameter and returns a {@link Function} that will associate that
         * token with a production method (also a {@link Function}) and will return this collector.
         * <hr>
         * <em>Implementation details</em>
         * <p>
         * A builder implementation that extends a collector can override this method so that the result type of the
         * resulting {@link Function} meets the requirements of that builder implementation.
         * In particular, the {@linkplain #on(Object, Object) variant of this method} can be used for that.
         * Example:
         * <pre>
         * public class Sample {
         *
         *     // ...
         *
         *     public static class Builder extends FactoryHub.Collector&lt;Sample&gt; {
         *
         *         &#64;Override
         *         public final &lt;T&gt; Function&lt;Function&lt;Sample, T&gt;, Builder&gt; on(final T token) {
         *             return on(token, this);
         *         }
         *
         *         public final Sample build() {
         *             return new Sample(...);
         *         }
         *     }
         * }
         * </pre>
         *
         * @param <T> The type of the token and also the result type of the production method.
         * @see #on(Object, Object)
         */
        @SuppressWarnings("DesignForExtension")
        public <T> Function<Function<C, T>, ? extends Collector<C>> on(final T token) {
            return on(token, this);
        }

        /**
         * Extended variant of {@link #on(Object)}:
         * allows an explicit specification of the result and thus the result type of the resulting {@link Function}.
         * This enables a builder implementation that uses a {@link Collector} or is derived from it to adapt
         * its two-stage builder pattern.
         * <p>
         * Examples can be found in the descriptions of {@link #on(Object)} and {@link FactoryHub}.
         *
         * @param <T> The type of the token and also the result type of the production method.
         * @param <R> The result typ of the resulting {@link Function}.
         * @see #on(Object)
         */
        public final <T, R> Function<Function<C, T>, R> on(final T token, final R result) {
            return function -> {
                methods.put(token, function);
                return result;
            };
        }
    }
}
