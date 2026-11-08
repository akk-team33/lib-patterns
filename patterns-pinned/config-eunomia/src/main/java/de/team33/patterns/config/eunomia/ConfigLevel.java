package de.team33.patterns.config.eunomia;

import de.team33.patterns.io.thalassa.IO;
import de.team33.patterns.io.thalassa.RecordIO;
import de.team33.patterns.typing.proteus.Type;

import java.nio.file.Path;
import java.util.function.BiFunction;

/**
 * Defines the available levels of a hierarchical configuration.
 * <p>
 * Each configuration level represents an independent source of configuration
 * values. The effective configuration is obtained by combining the available
 * levels according to their precedence.
 * <p>
 * During this combination, only explicitly defined values from higher-precedence
 * levels replace values from lower-precedence levels. Missing values and
 * {@code null} values do not override existing values.
 * <p>
 * Consequently, configuration properties represented by primitive types cannot
 * express an explicit "not configured" state. Primitive properties always
 * contribute a value during merging and may therefore override values from
 * lower-precedence levels.
 *
 * @see ConfigRepo
 */
public enum ConfigLevel {

    /**
     * Built-in default configuration.
     * <p>
     * This level is not persisted. It provides the initial configuration supplied
     * when a {@link ConfigRepo} is created.
     */
    DEFAULT((c, p) -> new DefaultIO<>(), Resolving::none),

    /**
     * System-wide configuration shared by all users of the installation.
     */
    SYSTEM(RecordIO::by, Resolving::system),

    /**
     * User-specific configuration.
     * <p>
     * Values stored on this level override those provided by
     * {@link #SYSTEM SYSTEM}.
     */
    USER(RecordIO::by, Resolving::user),

    /**
     * Configuration associated with the current working directory.
     * <p>
     * This level has the highest precedence and is typically used for
     * project-specific or temporary configuration.
     */
    CWD(RecordIO::by, Resolving::cwd);

    @SuppressWarnings("rawtypes")
    private final BiFunction<Type, Path, IO> toIO;
    private final BiFunction<Pathing, Naming, Path> toPath;

    <T extends Record> ConfigLevel(final BiFunction<Type<T>, Path, IO<T>> toIO,
                                   final BiFunction<Pathing, Naming, Path> toPath) {
        this.toIO = toIO::apply;
        this.toPath = toPath;
    }

    @SuppressWarnings("unchecked")
    final <T extends Record> IO<T> newIO(final Type<T> recordType, final Pathing pathing, final Naming naming) {
        return toIO.apply(recordType, path(pathing, naming));
    }

    final Path path(final Pathing pathing, final Naming naming) {
        return toPath.apply(pathing, naming);
    }

    private static class Resolving {

        @SuppressWarnings("unused")
        static Path none(Pathing pathing, Naming naming) {
            return null;
        }

        static Path system(Pathing pathing, Naming naming) {
            return pathing.system()
                          .resolve(naming.group())
                          .resolve("%s.json".formatted(naming.name()));
        }

        static Path user(Pathing pathing, Naming naming) {
            return pathing.user()
                          .resolve(naming.group())
                          .resolve("%s.json".formatted(naming.name()));
        }

        static Path cwd(Pathing pathing, Naming naming) {
            return pathing.cwd()
                          .resolve(".%s.%s.json".formatted(naming.group(), naming.name()));
        }
    }
}
