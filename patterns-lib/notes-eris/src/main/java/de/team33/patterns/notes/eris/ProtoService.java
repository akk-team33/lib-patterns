package de.team33.patterns.notes.eris;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Preliminary stage of a service implementation that sends messages to an audience when certain events occur.
 *
 * @param <S> The service type: the intended effective type of the concrete service implementation.
 */
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
     * Provides a {@link Registry} where participants can register {@link Consumer}s as listeners.
     */
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
     * Identifies service specific {@linkplain de.team33.patterns.notes.eris.Channel channels} in the context
     * of a {@link ProtoService} implementation.
     * On this level an instance "knows" how to get the actual message from the underlying service.
     *
     * @param <S> The service type: the effective type of the underlying service implementation.
     * @param <M> The message type.
     */
    @SuppressWarnings("ClassNameSameAsAncestorName")
    @FunctionalInterface
    public interface Channel<S, M> extends de.team33.patterns.notes.eris.Channel<M>, Function<S, M> {
    }
}
