package de.team33.patterns.config.eunomia;

import de.team33.patterns.io.thalassa.IO;
import de.team33.patterns.typing.proteus.Type;

import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

class IOMapping<T extends Record> {

    private final Type<T> recordType;
    private final Map<ConfigLevel, IO<T>> backing;
    private final Map<ConfigLevel, Path> paths;

    IOMapping(final Type<T> recordType, final Pathing pathing, final Naming naming) {
        this.recordType = recordType;
        this.backing = new EnumMap<>(ConfigLevel.class);
        this.paths = new EnumMap<>(ConfigLevel.class);

        for (final ConfigLevel level : ConfigLevel.values()) {
            backing.put(level, level.newIO(recordType, pathing, naming));
            paths.put(level, level.path(pathing, naming));
        }
    }

    final Type<T> recordType() {
        return recordType;
    }

    final Path path(final ConfigLevel level) {
        return paths.get(level);
    }

    final IO<T> get(final ConfigLevel level) {
        return backing.get(level);
    }
}
