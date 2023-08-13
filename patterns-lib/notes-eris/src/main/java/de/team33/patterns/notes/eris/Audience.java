package de.team33.patterns.notes.eris;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * Implementation of a registry with the additional option to send messages to the registered listeners.
 */
public class Audience implements Registry {

    private final Object monitor = new Object();
    private final Map<Channel<?>, List<Consumer<?>>> backing = new HashMap<>(0);
    private final Executor executor;

    /**
     * Initializes a new instance that synchronously {@linkplain #send(Channel, Object) sends} messages to the
     * {@linkplain #add(Channel, Consumer) registered} listeners.
     */
    public Audience() {
        this(Runnable::run);
    }

    /**
     * Initializes a new instance that {@linkplain #send(Channel, Object) sends} messages to the
     * {@linkplain #add(Channel, Consumer) registered} listeners using a given {@link Executor}.
     */
    public Audience(final Executor executor) {
        this.executor = executor;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private <M> List<Consumer<? super M>> getListeners(final Channel<M> channel) {
        final List list = backing.get(channel);
        return (null == list) ? Collections.emptyList() : list;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void putListeners(final Channel channel, final List newList) {
        backing.put(channel, newList);
    }

    @Override
    public final <M> void add(final Channel<M> channel, final Consumer<? super M> listener) {
        synchronized (monitor) {
            final List<Consumer<? super M>> oldList = getListeners(channel);
            final List<Consumer<? super M>> newList = new ArrayList<>(oldList.size() + 1);
            newList.addAll(oldList);
            newList.add(listener);
            putListeners(channel, newList);
        }
    }

    private static <M> Optional<Consumer<M>> emitter(final Collection<? extends Consumer<? super M>> listeners) {
        return listeners.isEmpty()
                ? Optional.empty()
                : Optional.of(message -> listeners.forEach(listener -> listener.accept(message)));
    }

    private <M> Optional<Consumer<M>> emitter(final Channel<? super M> channel) {
        synchronized (monitor) {
            return emitter(getListeners(channel));
        }
    }

    /**
     * Sends a given message to all listeners that have {@linkplain #add(Channel, Consumer) registered}
     * for the given {@link Channel}.
     *
     * @param <M> The message type.
     */
    public final <M> void send(final Channel<M> channel, final M message) {
        emitter(channel).ifPresent(emitter -> executor.execute(() -> emitter.accept(message)));
    }
}
