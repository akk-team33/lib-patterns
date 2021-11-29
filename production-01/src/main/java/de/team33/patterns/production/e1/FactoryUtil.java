package de.team33.patterns.production.e1;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static java.lang.String.format;

public class FactoryUtil {
    /**
     * A {@link Consumer} to be used with {@link FactoryHub.Collector#setUnknownTokenListener(Consumer)} that does nothing.
     */
    public static final Consumer<Object> ACCEPT_UNKNOWN_TOKEN = token -> {
    };
    private static final Logger LOG = Logger.getLogger(FactoryHub.class.getCanonicalName());
    private static final String ILLEGAL_CONTEXT_TYPE =
            "This instance cannot be viewed as a context of the specified type!%n" +
            "- type of this: %s%n" +
            "- context type: %s%n";
    private static final String UNKNOWN_TOKEN =
            "unknown token:%n" +
            "- type of token   : %s%n" +
            "- value* of token : %s%n" +
            "*(string representation)";
    /**
     * A {@link Consumer} to be used with {@link FactoryHub.Collector#setUnknownTokenListener(Consumer)} that logs the event via
     * {@linkplain Logger java logging}.
     */
    public static final Consumer<Object> LOG_UNKNOWN_TOKEN = token -> {
        LOG.info(() -> format(UNKNOWN_TOKEN, classOf(token), token));
    };
    /**
     * A {@link Consumer} to be used with {@link FactoryHub.Collector#setUnknownTokenListener(Consumer)} that throws an
     * {@link IllegalArgumentException}.
     */
    public static final Consumer<Object> DENY_UNKNOWN_TOKEN = token -> {
        throw new IllegalArgumentException(format(UNKNOWN_TOKEN, classOf(token), token));
    };

    public static Builder<FactoryHub<?>> builder() {
        return new Standalone();
    }

    public static <C> Builder<C> builder(final Supplier<C> contextSupplier) {
        return new ContextDependant<>(contextSupplier);
    }

    @SuppressWarnings("ReturnOfNull")
    private static Class<?> classOf(final Object subject) {
        return (subject == null) ? null : subject.getClass();
    }

    public abstract static class Builder<C> extends FactoryHub.Collector<C, Builder<C>> {

        @Override
        protected final Builder<C> getBuilder() {
            return this;
        }

        public abstract FactoryHub<C> build();
    }

    private static class Standalone extends Builder<FactoryHub<?>> {

        @Override
        public FactoryHub<FactoryHub<?>> build() {
            return new StandaloneHub(this);
        }
    }

    private static class StandaloneHub extends FactoryHub<FactoryHub<?>> {

        private StandaloneHub(final Standalone builder) {
            super(builder);
        }

        @Override
        protected final FactoryHub<?> getContext() {
            return this;
        }
    }

    private static class ContextDependant<C> extends Builder<C> {

        private final Supplier<C> contextSupplier;

        private ContextDependant(final Supplier<C> contextSupplier) {
            this.contextSupplier = contextSupplier;
        }

        @Override
        public final FactoryHub<C> build() {
            return new FactoryHub<C>(this) {
                @Override
                protected C getContext() {
                    return contextSupplier.get();
                }
            };
        }
    }
}
