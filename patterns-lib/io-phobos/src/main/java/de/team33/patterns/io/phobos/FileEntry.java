package de.team33.patterns.io.phobos;

import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

/**
 * Represents an entry from the file index.
 * Includes some meta information about a file, particularly the file system path, file type, size,
 * and some timestamps.
 * <p>
 * Use {@link FileIndex#entry(Path)} to get an instance.
 */
@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface FileEntry {

    /**
     * Returns the file system path of the represented file.
     * An implementation is expected to return an {@link Path#toAbsolutePath() absolute}
     * {@link Path#normalize() normalized} {@link Path}.
     */
    Path path();

    /**
     * Determines if the represented file is a directory.
     */
    default boolean isDirectory() {
        return false;
    }

    /**
     * Determines if the represented file is a regular file.
     */
    default boolean isRegularFile() {
        return false;
    }

    /**
     * Determines if the represented file is a symbolic link.
     */
    default boolean isSymbolicLink() {
        return false;
    }

    /**
     * Determines if the represented file is something else than a directory, a regular file or a symbolic link.
     * E.g. a special file.
     */
    default boolean isOther() {
        return false;
    }

    /**
     * Determines if the represented file exists.
     */
    default boolean exists() {
        return false;
    }

    /**
     * Returns the timestamp of the last modification of the represented file.
     *
     * @throws UnsupportedOperationException if the file does not exist.
     */
    default Instant lastModified() {
        throw new UnsupportedOperationException("a non existing file cannot have a timestamp");
    }

    /**
     * Returns the timestamp of the last access to the represented file.
     *
     * @throws UnsupportedOperationException if the file does not exist.
     */
    default Instant lastAccess() {
        throw new UnsupportedOperationException("a non existing file cannot have a timestamp");
    }

    /**
     * Returns the timestamp of the creation of the represented file.
     *
     * @throws UnsupportedOperationException if the file does not exist.
     */
    default Instant creation() {
        throw new UnsupportedOperationException("a non existing file cannot have a timestamp");
    }

    /**
     * Returns the size of the represented file.
     *
     * @throws UnsupportedOperationException if the file does not exist.
     */
    default long size() {
        throw new UnsupportedOperationException("a non existing file cannot have a size");
    }

    /**
     * Returns the entries of the content of the represented file if it {@link #isDirectory() is a directory}.
     *
     * @throws UnsupportedOperationException if the represented file is not a directory.
     */
    default List<FileEntry> content() {
        throw new UnsupportedOperationException("only directories can have a content");
    }
}
