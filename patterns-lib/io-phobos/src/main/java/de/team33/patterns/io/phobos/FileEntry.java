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

/**
 * Represents an entry from the virtual file index.
 * Includes some meta information about a file, particularly the file system path, file type, size,
 * and some timestamps.
 */
public abstract class FileEntry {

    private final Path path;

    FileEntry(Path path) {
        this.path = path.toAbsolutePath().normalize();
    }

    /**
     * Returns a new {@link FileEntry} based on a given {@link Path}.
     */
    public static FileEntry of(final Path path, final LinkOption... linkOptions) {
        try {
            final BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class, linkOptions);
            return of(path, attributes, linkOptions);
        } catch (final IOException e) {
            return new Missing(path, e);
        }
    }

    private static FileEntry of(Path path, BasicFileAttributes attributes, LinkOption[] linkOptions) {
        return attributes.isDirectory()
               ? new Directory(path, attributes, linkOptions)
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
     * Returns the entries of the content of the represented file if it {@link #isDirectory() is a directory}.
     *
     * @throws UnsupportedOperationException if the represented file is not a directory.
     */
    public abstract List<FileEntry> content();

    private static class NoDirectory extends Existing {

        NoDirectory(Path path, BasicFileAttributes attributes) {
            super(path, attributes);
        }

        @Override
        public final List<FileEntry> content() {
            throw new UnsupportedOperationException("not a directory: " + path());
        }
    }

    private static abstract class Existing extends FileEntry {

        private final BasicFileAttributes attributes;

        Existing(Path path, BasicFileAttributes attributes) {
            super(path);
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
        private static final Comparator<String> STRING_ORDER = IGNORE_CASE.thenComparing(RESPECT_CASE);
        private static final Comparator<FileEntry> ENTRY_ORDER = Comparator.comparing(FileEntry::name, STRING_ORDER);

        private final LinkOption[] linkOptions;

        private final Lazy<List<FileEntry>> lazyContent;

        public Directory(Path path, BasicFileAttributes attributes, LinkOption[] linkOptions) {
            super(path, attributes);
            this.linkOptions = linkOptions;
            this.lazyContent = Lazy.init(this::newContent);
        }

        private List<FileEntry> newContent() {
            try (final Stream<Path> stream = Files.list(path())) {
                return Collections.unmodifiableList(stream.map(this::newEntry)
                                                          .sorted(ENTRY_ORDER)
                                                          .collect(Collectors.toList()));
            } catch (final IOException ignored) {
                return Collections.emptyList();
            }
        }

        private FileEntry newEntry(final Path path) {
            return FileEntry.of(path, linkOptions);
        }

        @Override
        public final List<FileEntry> content() {
            return lazyContent.get();
        }
    }

    private static class Missing extends FileEntry {

        private final IOException cause;

        Missing(final Path path, final IOException cause) {
            super(path);
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
        public final List<FileEntry> content() {
            throw new UnsupportedOperationException("not existing: " + path(), cause);
        }
    }
}
