package de.team33.patterns.notes.eris;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * @deprecated in this form, it appears largely useless.
 */
@Deprecated
public class Audience implements Registry {

    private final Object monitor = new Object();
    private final Map<Channel<?>, List<Consumer<?>>> backing = new HashMap<>(0);
    private final Executor executor;

    /**
     * @deprecated see {@link Audience}.
     */
    @Deprecated
    public Audience() {
        this(Runnable::run);
    }

    /**
     * @deprecated see {@link Audience}.
     */
    @Deprecated
    public Audience(final Executor executor) {
        this.executor = executor;
    }

    private static <M> Optional<Consumer<M>> emitter(final Collection<? extends Consumer<? super M>> listeners) {
        return listeners.isEmpty()
               ? Optional.empty()
               : Optional.of(message -> listeners.forEach(listener -> listener.accept(message)));
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

    /**
     * @deprecated see {@link Audience}.
     */
    @Deprecated
    public final <M> void add(final Channel<M> channel, final Consumer<? super M> listener) {
        synchronized (monitor) {
            final List<Consumer<? super M>> oldList = getListeners(channel);
            final List<Consumer<? super M>> newList = new ArrayList<>(oldList.size() + 1);
            newList.addAll(oldList);
            newList.add(listener);
            putListeners(channel, newList);
        }
    }

    private <M> Optional<Consumer<M>> emitter(final Channel<? super M> channel) {
        synchronized (monitor) {
            return emitter(getListeners(channel));
        }
    }

    /**
     * @deprecated see {@link Audience}.
     */
    @Deprecated
    public final <M> void send(final Channel<M> channel, final M message) {
        emitter(channel).ifPresent(emitter -> executor.execute(() -> emitter.accept(message)));
    }
}
