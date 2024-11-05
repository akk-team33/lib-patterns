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
import java.util.Optional;
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

    private static final LinkOption[] DISTINCTIVE = {LinkOption.NOFOLLOW_LINKS};
    private static final LinkOption[] RESOLVING = {};

    private final Path path;
    private final FileType type;

    FileEntry(final Path path, final Normality normal, final FileType type) {
        this.path = normal.apply(path);
        this.type = type;
    }

    /**
     * Returns a new {@link FileEntry} based on a given {@link Path}.
     * Symbolic links will not be resolved!
     *
     * @see #resolved()
     */
    public static FileEntry of(final Path path) {
        return of(path, Normality.UNKNOWN, DISTINCTIVE);
    }

    private static FileEntry of(final Path path, final Normality normal, final LinkOption[] options) {
        try {
            final BasicFileAttributes attributes = //
                    Files.readAttributes(path, BasicFileAttributes.class, options);
            return of(path, normal, options, attributes);
        } catch (final IOException e) {
            return new Missing(path, normal, e);
        }
    }

    private static FileEntry of(final Path path, final Normality normal,
                                final LinkOption[] options, final BasicFileAttributes attributes) {
        return attributes.isDirectory()
               ? new Directory(path, normal, options, attributes)
               : attributes.isSymbolicLink()
                       ? new LinkEntry(path, normal, attributes)
                       : new PlainEntry(path, normal, attributes);
    }

    /**
     * Returns a {@link FileEntry} that represents the same file as <em>this</em> {@link FileEntry},
     * but symbolic links are resolved. This applies primarily to {@link FileEntry FileEntries} of
     * {@link FileEntry#type() type} {@link FileType#SYMBOLIC SYMBOLIC}, but also of {@link FileEntry#type() type}
     * {@link FileType#DIRECTORY DIRECTORY} if their {@link FileEntry#entries() contents} are in turn of
     * {@link FileEntry#type() type} {@link FileType#SYMBOLIC SYMBOLIC} or {@link FileType#DIRECTORY DIRECTORY}.
     * {@link FileEntry FileEntries} of other types simply return a self-reference.
     */
    public abstract FileEntry resolved();

    /**
     * Returns a {@link FileEntry} that represents the same file as <em>this</em> {@link FileEntry},
     * but symbolic links are NOT resolved. This applies primarily to {@link FileEntry FileEntries} of
     * {@link FileEntry#type() type} {@link FileType#SYMBOLIC SYMBOLIC}, but also of {@link FileEntry#type() type}
     * {@link FileType#DIRECTORY DIRECTORY} if their {@link FileEntry#entries() contents} are in turn of
     * {@link FileEntry#type() type} {@link FileType#SYMBOLIC SYMBOLIC} or {@link FileType#DIRECTORY DIRECTORY}.
     * {@link FileEntry FileEntries} of other types simply return a self-reference.
     */
    public final FileEntry distinct() {
        return of(path(), Normality.DEFINITE, DISTINCTIVE);
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
        return Optional.ofNullable(path.getFileName()).orElse(path).toString();
    }

    /**
     * Returns the {@link FileType} of the represented file.
     */
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
    public abstract boolean isSpecial();

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
    public abstract List<FileEntry> entries();

    @Override
    public final String toString() {
        return path.toString();
    }

    private static class LinkEntry extends NoDirectory {

        private final Lazy<FileEntry> lazyResolved;

        LinkEntry(final Path path, final Normality normal, final BasicFileAttributes attributes) {
            super(path, normal, attributes);
            this.lazyResolved = Lazy.init(() -> FileEntry.of(path(), Normality.DEFINITE, RESOLVING));
        }

        @Override
        public final FileEntry resolved() {
            return lazyResolved.get();
        }
    }

    private static class PlainEntry extends NoDirectory {

        PlainEntry(final Path path, final Normality normal, final BasicFileAttributes attributes) {
            super(path, normal, attributes);
        }

        @Override
        public final FileEntry resolved() {
            return this;
        }
    }

    private static abstract class NoDirectory extends Existing {

        NoDirectory(final Path path, final Normality normal, final BasicFileAttributes attributes) {
            super(path, normal, attributes);
        }

        @Override
        public final List<FileEntry> entries() {
            throw new UnsupportedOperationException("not a directory: " + path());
        }
    }

    private abstract static class Existing extends FileEntry {

        private final BasicFileAttributes attributes;

        Existing(final Path path, final Normality normal, final BasicFileAttributes attributes) {
            super(path, normal, FileType.map(attributes));
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
        public final boolean isSpecial() {
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

        private static final Comparator<String> PRIMARY = String::compareToIgnoreCase;
        private static final Comparator<String> SECONDARY = String::compareTo;
        private static final Comparator<FileEntry> ENTRY_ORDER = comparing(FileEntry::name,
                                                                           PRIMARY.thenComparing(SECONDARY));
        private final LinkOption[] options;
        private final Lazy<List<FileEntry>> lazyEntries;
        private final Lazy<FileEntry> lazyResolved;

        private Directory(final Path path, final Normality normal,
                          final LinkOption[] options, final BasicFileAttributes attributes) {
            super(path, normal, attributes);
            this.options = options;
            this.lazyEntries = Lazy.init(this::newEntries);
            this.lazyResolved = Lazy.init(this::newResolved);
        }

        private List<FileEntry> newEntries() {
            try (final Stream<Path> stream = Files.list(path())) {
                return stream.map(path -> FileEntry.of(path, Normality.DEFINITE, options))
                             .sorted(ENTRY_ORDER)
                             .collect(Collectors.toList());
            } catch (final IOException ignored) {
                return Collections.emptyList();
            }
        }

        private FileEntry newResolved() {
            if (RESOLVING == options) {
                return this;
            } else {
                return FileEntry.of(path(), Normality.DEFINITE, RESOLVING);
            }
        }

        @Override
        public final FileEntry resolved() {
            return lazyResolved.get();
        }

        @Override
        public final List<FileEntry> entries() {
            return lazyEntries.get();
        }
    }

    private static class Missing extends FileEntry {

        private final IOException cause;

        Missing(final Path path, final Normality normal, final IOException cause) {
            super(path, normal, FileType.MISSING);
            this.cause = cause;
        }

        @Override
        public final FileEntry resolved() {
            return this;
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
        public final boolean isSpecial() {
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
        public final List<FileEntry> entries() {
            throw new UnsupportedOperationException("not existing: " + path(), cause);
        }
    }
}
