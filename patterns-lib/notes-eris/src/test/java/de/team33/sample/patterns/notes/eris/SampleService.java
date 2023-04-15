package de.team33.sample.patterns.notes.eris;

import java.nio.file.Path;
import java.time.Instant;
import java.util.function.Function;

public class SampleService {

    private Path path;
    private Instant timestamp;

    public interface Channel<M> extends de.team33.patterns.notes.eris.Channel<M>, Function<SampleService, M> {

        Channel<Path> NEW_PATH = service -> service.path;
        Channel<Instant> NEW_TIMESTAMP = service -> service.timestamp;
        Channel<SampleService> UPDATED = service -> service;
    }
}
