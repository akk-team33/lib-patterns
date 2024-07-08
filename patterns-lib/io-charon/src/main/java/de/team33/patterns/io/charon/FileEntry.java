package de.team33.patterns.io.charon;

import java.nio.file.Path;

/**
 * Represents an entry from the file system.
 * Includes some meta information about a file, particularly the file system path, file type, size,
 * and some timestamps.
 * <p>
 * Strictly speaking, the meta information only applies to the moment of instantiation.
 * Therefore, an instance should be short-lived. The longer an instance "lives", the more likely it is
 * that the meta information is out of date because the underlying file may have been changed in the meantime.
 */
public class FileEntry {

    private final Path path;

    private FileEntry(final Path path) {
        this.path = path.toAbsolutePath().normalize();
    }

    public static FileEntry of(final Path path) {
        return new FileEntry(path);
    }

    /**
     * Returns the file system path of the represented file.
     * The implementation will return an {@link Path#toAbsolutePath() absolute}
     * {@link Path#normalize() normalized} {@link Path}.
     */
    public final Path path() {
        return path;
    }

    /**
     * Returns the simple name of the represented file.
     */
    public final String name() {
        return path.getFileName().toString();
    }
}
