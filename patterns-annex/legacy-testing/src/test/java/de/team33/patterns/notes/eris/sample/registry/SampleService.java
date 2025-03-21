package de.team33.patterns.notes.eris.sample.registry;

import de.team33.patterns.notes.eris.Channel;
import de.team33.patterns.notes.eris.Registry;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class SampleService implements Registry {

    private Path path;
    private Instant timestamp;
    @SuppressWarnings("rawtypes")
    private Map<Channel, List<Consumer>> audience = new ConcurrentHashMap<>();

    @Override
    public final <M> void add(final Channel<M> channel, final Consumer<? super M> listener) {
        audience.computeIfAbsent(channel, SampleService::newList).add(listener);
    }

    @SuppressWarnings("rawtypes")
    private static List<Consumer> newList(final Channel channel) {
        return Collections.synchronizedList(new LinkedList<>());
    }
}
