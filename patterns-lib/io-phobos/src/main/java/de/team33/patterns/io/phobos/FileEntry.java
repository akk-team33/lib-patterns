package de.team33.patterns.io.phobos;

import de.team33.patterns.lazy.narvi.Lazy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

/**
 * Represents an entry from the virtual file index.
 * Includes some meta information about a file, particularly the file system path, file type, size,
 * and some timestamps.
 * <p>
 * Strictly speaking, the meta information only applies to the moment of instantiation.
 * Therefore, an instance should be short-lived. The longer an instance "lives", the more likely it is
 * that the meta information is out of date because the underlying file may have been changed in the meantime.
 */
public abstract class FileEntry {

    private final Path path;
    private final FileType type;

    FileEntry(final Path path, final FileType type) {
        this.path = path.toAbsolutePath().normalize();
        this.type = type;
    }

    /**
     * Returns a new {@link FileEntry} based on a given {@link Path}.
     * <p>
     * If the specified {@link Path} points to a symbolic link,
     * the {@link #type() result type} is {@link FileType#SYMBOLIC}.
     */
    public static FileEntry primary(final Path path) {
        return of(path, LinkOption.NOFOLLOW_LINKS);
    }

    /**
     * Returns a new {@link FileEntry} based on a given {@link Path}.
     * <p>
     * If the specified {@link Path} points to a symbolic link,
     * the {@link #type() result type} corresponds to the linked file.
     */
    public static FileEntry evaluated(final Path path) {
        return of(path);
    }

    /**
     * @deprecated use {@link #primary(Path)} or {@link #evaluated(Path)} instead.
     */
    @Deprecated
    public static FileEntry of(final Path path, final LinkOption... linkOptions) {
        try {
            final BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class, linkOptions);
            return of(path, attributes);
        } catch (final IOException e) {
            return new Missing(path, e);
        }
    }

    private static FileEntry of(final Path path, final BasicFileAttributes attributes) {
        return attributes.isDirectory()
               ? new Directory(path, attributes)
               : new NoDirectory(path, attributes);
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

    public final FileType type() {
        return type;
    }

    /**
     * Determines if the represented file is a directory.
     */
    public abstract boolean isDirectory();

    /**
     * Determines if the represented file is a regular file.
     */
    public abstract boolean isRegularFile();

    /**
     * Determines if the represented file is a symbolic link.
     */
    public abstract boolean isSymbolicLink();

    /**
     * Determines if the represented file is something else than a directory, a regular file or a symbolic link.
     * E.g. a special file.
     */
    public abstract boolean isOther();

    /**
     * Determines if the represented file exists.
     */
    public abstract boolean exists();

    /**
     * Returns the timestamp of the last modification of the represented file.
     *
     * @throws UnsupportedOperationException if the file does not exist.
     */
    public abstract Instant lastModified();

    /**
     * Returns the timestamp of the last access to the represented file.
     *
     * @throws UnsupportedOperationException if the file does not exist.
     */
    public abstract Instant lastAccess();

    /**
     * Returns the timestamp of the creation of the represented file.
     *
     * @throws UnsupportedOperationException if the file does not exist.
     */
    public abstract Instant creation();

    /**
     * Returns the size of the represented file.
     *
     * @throws UnsupportedOperationException if the file does not exist.
     */
    public abstract long size();

    /**
     * Returns the content of the represented file if it {@link #isDirectory() is a directory}.
     *
     * @throws UnsupportedOperationException if the represented file is not a directory.
     */
    public abstract List<Path> content();

    @Override
    public final String toString() {
        return path.toString();
    }

    private static class NoDirectory extends Existing {

        NoDirectory(final Path path, final BasicFileAttributes attributes) {
            super(path, attributes);
        }

        @Override
        public final List<Path> content() {
            throw new UnsupportedOperationException("not a directory: " + path());
        }
    }

    private abstract static class Existing extends FileEntry {

        private final BasicFileAttributes attributes;

        Existing(final Path path, final BasicFileAttributes attributes) {
            super(path, FileType.map(attributes));
            this.attributes = attributes;
        }

        @Override
        public final boolean isDirectory() {
            return attributes.isDirectory();
        }

        @Override
        public final boolean isRegularFile() {
            return attributes.isRegularFile();
        }

        @Override
        public final boolean isSymbolicLink() {
            return attributes.isSymbolicLink();
        }

        @Override
        public final boolean isOther() {
            return attributes.isOther();
        }

        @Override
        public final boolean exists() {
            return true;
        }

        @Override
        public final Instant lastModified() {
            return attributes.lastModifiedTime().toInstant();
        }

        @Override
        public final Instant lastAccess() {
            return attributes.lastAccessTime().toInstant();
        }

        @Override
        public final Instant creation() {
            return attributes.creationTime().toInstant();
        }

        @Override
        public final long size() {
            return attributes.size();
        }
    }

    private static class Directory extends Existing {

        private static final Comparator<String> IGNORE_CASE = String::compareToIgnoreCase;
        private static final Comparator<String> RESPECT_CASE = String::compareTo;
        private static final Comparator<Path> ENTRY_ORDER = comparing(path -> path.getFileName().toString(),
                                                                      IGNORE_CASE.thenComparing(RESPECT_CASE));

        private final Lazy<List<Path>> lazyContent;

        private Directory(final Path path, final BasicFileAttributes attributes) {
            super(path, attributes);
            this.lazyContent = Lazy.init(this::newContent);
        }

        private List<Path> newContent() {
            try (final Stream<Path> stream = Files.list(path())) {
                return Collections.unmodifiableList(stream.sorted(ENTRY_ORDER)
                                                          .collect(Collectors.toList()));
            } catch (final IOException ignored) {
                return Collections.emptyList();
            }
        }

        @Override
        public final List<Path> content() {
            return lazyContent.get();
        }
    }

    private static class Missing extends FileEntry {

        private final IOException cause;

        Missing(final Path path, final IOException cause) {
            super(path, FileType.MISSING);
            this.cause = cause;
        }

        @Override
        public final boolean isDirectory() {
            return false;
        }

        @Override
        public final boolean isRegularFile() {
            return false;
        }

        @Override
        public final boolean isSymbolicLink() {
            return false;
        }

        @Override
        public final boolean isOther() {
            return false;
        }

        @Override
        public final boolean exists() {
            return false;
        }

        @Override
        public final Instant lastModified() {
            throw new UnsupportedOperationException("not existing: " + path(), cause);
        }

        @Override
        public final Instant lastAccess() {
            throw new UnsupportedOperationException("not existing: " + path(), cause);
        }

        @Override
        public final Instant creation() {
            throw new UnsupportedOperationException("not existing: " + path(), cause);
        }

        @Override
        public final long size() {
            throw new UnsupportedOperationException("not existing: " + path(), cause);
        }

        @Override
        public final List<Path> content() {
            throw new UnsupportedOperationException("not existing: " + path(), cause);
        }
    }
}
