package de.team33.patterns.io.charon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

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
    private final FileType type;

    private FileEntry(final Path path, final FileType type) {
        this.path = path.toAbsolutePath().normalize();
        this.type = type;
    }

    public static FileEntry of(final Path path) {
        try {
            final BasicFileAttributes attributes = Files.readAttributes(path,
                                                                        BasicFileAttributes.class,
                                                                        LinkOption.NOFOLLOW_LINKS);
            return new FileEntry(path, FileType.map(attributes));
        } catch (final NoSuchFileException ignored) {
            return new FileEntry(path, FileType.MISSING);
        } catch (final IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
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

    /**
     * Returns the {@link FileType} of the represented file.
     */
    public final FileType type() {
        return type;
    }

    public enum Mode {
        STRAIGHT,
        EFFECTIVE
    }
}
