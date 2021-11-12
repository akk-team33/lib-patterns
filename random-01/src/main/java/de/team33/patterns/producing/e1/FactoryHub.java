package de.team33.patterns.producing.e1;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * A hub of methods for creating instances of virtually any type.
 * <p>
 * When called, a method is parameterized with a context of a certain type.
 * It is identified by a template (a constant) of the respective result type,
 * so that the templates can also be used within complex templates as placeholders
 * for methods for initializing their components.
 * <p>
 * Instances that contain a {@link FactoryHub}, are derived from it or simply a {@link FactoryHub} itself,
 * are typically used as context, so that the methods that are assigned to a {@link FactoryHub}
 * can be used by other such methods.
 * <p>
 * A class intended to serve as the context of a {@link FactoryHub} will either contain a {@link FactoryHub}
 * or be derived from it. The following example shows a derivation:
 * <pre>
 * public final class FactoryHubSample extends FactoryHub&lt;FactoryHubSample&gt; {
 *
 *     // Some pre-defined templates ...
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
 *     // with the templates defined above and corresponding methods ...
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
 *         public final &lt;T&gt; Function&lt;Function&lt;FactoryHubSample, T&gt;, Builder&gt; on(final T template) {
 *             return collector.on(template, this);
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
            "unknown template:%n" +
            "- type of template   : %s%n" +
            "- value* of template : %s%n" +
            "*(string representation)";

    @SuppressWarnings("rawtypes")
    private final Map<Object, Function> methods;
    private final Supplier<C> context;

    /**
     * Initializes a new instance.
     * <p>
     * This variant is used when a {@link FactoryHub} is to be used as part of its context.
     *
     * @param collector A fully prepared {@link Collector} that associates the constants to be supported with
     *                  appropriate methods.
     * @param context   A {@link Supplier} that provides the context as soon as it is actually needed.
     *                  It is expected that every call to {@link Supplier#get()} will return the same context instance!
     * @see #FactoryHub(Collector, Class)
     */
    public FactoryHub(final Collector<C> collector, final Supplier<C> context) {
        this.methods = copyOf(collector);
        this.context = context;
    }

    /**
     * Initializes a new instance.
     * <p>
     * This variant is used when a context type extends a {@link FactoryHub}.
     *
     * @param collector   A fully prepared {@link Collector} that associates the constants to be supported with
     *                    appropriate methods.
     * @param contextType The {@link Class} that represents the context type that actually extends this
     *                    {@link FactoryHub}.
     * @throws IllegalArgumentException if the given context type does not actually extend this {@link FactoryHub}.
     * @see #FactoryHub(Collector, Supplier)
     */
    protected FactoryHub(final Collector<C> collector, final Class<C> contextType) {
        if (getClass().isAssignableFrom(contextType)) {
            this.methods = copyOf(collector);
            this.context = () -> contextType.cast(this);
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
    private <R> Function<C, R> getMethod(final R template) {
        return Optional.ofNullable(methods.get(template))
                       .orElseThrow(() -> new IllegalArgumentException(format(ILLEGAL_TEMPLATE,
                                                                              classOf(template), template)));
    }

    /**
     * Produces a new* instance of a certain type, whereby the production method to be used is identified by a
     * template of the same type.
     * <p>
     * The association of template and production method is made when the hub is initialized via a {@link Collector}.
     * <p>
     * *The result is typically but not necessarily a new instance. In principle, it can also be a singleton,
     * for example.
     *
     * @param <R> The type of the produced result.
     * @see #stream(Object)
     */
    @SuppressWarnings("ReturnOfNull")
    public final <R> R create(final R template) {
        return (null == template) ? null : getMethod(template).apply(context.get());
    }

    /**
     * Produces an infinite (!) {@link Stream} of newly* produced elements af a certain type,
     * whereby the production method to be used for each element is identified by a template of the same type.
     *
     * @param <R> The type of the produced result.
     * @see #create(Object)
     */
    public final <R> Stream<R> stream(final R template) {
        return Stream.generate(() -> create(template));
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
         * Takes a template of a certain type as a parameter and returns a {@link Function} that will associate that
         * template with a production method (also a {@link Function}) and will return this collector.
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
         *         public final &lt;T&gt; Function&lt;Function&lt;Sample, T&gt;, Builder&gt; on(final T template) {
         *             return on(template, this);
         *         }
         *
         *         public final Sample build() {
         *             return new Sample(...);
         *         }
         *     }
         * }
         * </pre>
         *
         * @param <T> The type of the template and also the result type of the production method.
         * @see #on(Object, Object)
         */
        @SuppressWarnings("DesignForExtension")
        public <T> Function<Function<C, T>, ? extends Collector<C>> on(final T template) {
            return on(template, this);
        }

        /**
         * Extended variant of {@link #on(Object)}:
         * allows an explicit specification of the result and thus the result type of the resulting {@link Function}.
         * This enables a builder implementation that uses a {@link Collector} or is derived from it to adapt
         * its two-stage builder pattern.
         * <p>
         * Examples can be found in the descriptions of {@link #on(Object)} and {@link FactoryHub}.
         *
         * @param <T> The type of the template and also the result type of the production method.
         * @param <R> The result typ of the resulting {@link Function}.
         * @see #on(Object)
         */
        public final <T, R> Function<Function<C, T>, R> on(final T template, final R result) {
            return function -> {
                methods.put(template, function);
                return result;
            };
        }
    }
}
