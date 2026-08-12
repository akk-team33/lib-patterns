package de.team33.patterns.notes.eris;

import java.util.function.Function;

/**
 * @deprecated in this form, it appears largely useless.
 */
@Deprecated
public class ProtoService<S extends ProtoService<S>> {

    private static final String ILLEGAL_BUILDER_CLASS =
            "<builderClass> is expected to represent the class of <this> (%s) - but was %s";

    private final Audience audience;

    /**
     * Initializes a new instance and checks the intended service type for consistency.
     *
     * @param audience     A preconfigured {@link Audience} to handle notifications and targets.
     * @param serviceClass The {@link Class} representation of the intended effective service type.
     * @throws IllegalArgumentException if the specified service class does not represent the instance to create.
     */
    protected ProtoService(final Audience audience, final Class<S> serviceClass) {
        if (!serviceClass.isAssignableFrom(getClass())) {
            throw new IllegalArgumentException(String.format(ILLEGAL_BUILDER_CLASS, getClass(), serviceClass));
        }
        this.audience = audience;
    }

    /**
     * @deprecated see {@link ProtoService}.
     */
    @Deprecated
    public final Registry registry() {
        return audience;
    }

    /**
     * Triggers messages on the given, service-specific {@linkplain Channel channels}.
     */
    @SafeVarargs
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected final void fire(final Channel<S, ?>... channels) {
        for (final Channel channel : channels) {
            audience.send(channel, channel.apply(this));
        }
    }

    /**
     * @deprecated in this form, it appears largely useless.
     */
    @Deprecated
    @SuppressWarnings("ClassNameSameAsAncestorName")
    @FunctionalInterface
    public interface Channel<S, M> extends de.team33.patterns.notes.eris.Channel<M>, Function<S, M> {
    }
}
