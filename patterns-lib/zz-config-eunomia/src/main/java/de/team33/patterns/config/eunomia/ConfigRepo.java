package de.team33.patterns.config.eunomia;

import de.team33.patterns.typing.proteus.Type;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;

import static java.lang.System.Logger.Level.WARNING;

/**
 * Provides persistent access to configurations represented by a specific record type.
 * <p>
 * A {@code ConfigRepo} manages the configuration files belonging to one configuration
 * record. Configurations may exist on different {@link ConfigLevel configuration levels}.
 * Reading the effective configuration combines the available levels into a single
 * configuration instance, while writing always targets one explicitly specified level.
 * <p>
 * Each repository is associated with exactly one record type and is initialized with
 * a default configuration. This default configuration is available through the
 * {@link ConfigLevel#DEFAULT default level} and serves as the basis for the effective
 * configuration when no higher-level values are present.
 * <p>
 * Instances of this class are intended to be reused for repeated configuration access.
 *
 * @param <T> the record type representing the configuration.
 * @see ConfigLevel
 */
public class ConfigRepo<T extends Record> {

    private static final System.Logger LOGGER = System.getLogger(ConfigRepo.class.getCanonicalName());

    private final Type<T> configType;
    private final IOMapping<T> mapping;

    ConfigRepo(final IOMapping<T> mapping, final T defaultConfig) {
        this.configType = mapping.recordType();
        this.mapping = mapping;
        write(ConfigLevel.DEFAULT, defaultConfig);
    }

    public static <T extends Record> ConfigRepo<T> by(final Class<T> configClass, final T defaultConfig) {
        return by(Type.of(configClass), defaultConfig);
    }

    public static <T extends Record> ConfigRepo<T> by(final Type<T> configType, final T defaultConfig) {
        return new ConfigRepo<>(new IOMapping<>(configType, Pathing.DEFAULT, naming(configType.core())), defaultConfig);
    }

    private static Naming naming(final Class<?> configClass) {
        final String group = configClass.getPackageName();
        final String name = configClass.getCanonicalName()
                                       .substring(group.length() + 1);
        return new Naming(group, name);
    }

    /**
     * Returns the file path associated with the given configuration level.
     *
     * @param level the configuration level.
     * @return the corresponding configuration file path, or {@code null} if the
     * specified level is not persisted.
     */
    public final Path path(final ConfigLevel level) {
        return mapping.path(level);
    }

    /**
     * Reads the effective configuration.
     * <p>
     * The resulting configuration is obtained by reading all available configuration
     * levels and merging them according to their precedence.
     * <p>
     * During merging, values from higher-precedence levels replace values from
     * lower-precedence levels only if they are explicitly present. Missing values
     * and {@code null} values leave existing values unchanged.
     * <p>
     * For configuration records intended to participate in hierarchical merging,
     * nullable reference components are recommended where an unset state must be
     * distinguishable from an explicit value.
     *
     * @return the effective configuration.
     * @throws IllegalStateException if an I/O error occurs while reading a configuration file.
     */
    public final T read() {
        return Stream.of(ConfigLevel.values())
                     .map(this::read)
                     .filter(Objects::nonNull)
                     .reduce(this::merge)
                     .orElseThrow(Util::shouldNotHappen);
    }

    /**
     * Reads the configuration stored for the specified level.
     *
     * @param level the configuration level.
     * @return the stored configuration, or {@code null} if no configuration exists
     * for that level.
     * @throws IllegalStateException if an I/O error occurs while reading the configuration.
     */
    public final T read(final ConfigLevel level) {
        try {
            return mapping.get(level).read();
        } catch (final NoSuchFileException e) {
            return null;
        } catch (final IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * Stores the given configuration on the specified configuration level.
     *
     * @param level  the target configuration level.
     * @param config the configuration to be stored.
     * @throws IllegalStateException if an I/O error occurs while writing the configuration.
     */
    public final void write(final ConfigLevel level, final T config) {
        try {
            mapping.get(level).write(config);
        } catch (final IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * Removes all persisted configurations managed by this repository.
     * <p>
     * Any files that cannot be removed are reported through the system logger,
     * while the reset operation continues with the remaining configuration levels.
     */
    public final void reset() {
        for (final ConfigLevel level : ConfigLevel.values()) {
            final Path path = path(level);
            if (null != path) {
                try {
                    Files.deleteIfExists(path);
                } catch (final IOException e) {
                    LOGGER.log(WARNING, () -> "reset level %s failed: %s".formatted(level, path), e);
                }
            }
        }
    }

    private T merge(T left, T right) {
        return (null == right) ? left : Merger.by(configType).merge(left, right);
    }
}
