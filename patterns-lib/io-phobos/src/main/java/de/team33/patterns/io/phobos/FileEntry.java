package de.team33.patterns.io.phobos;

import de.team33.patterns.lazy.narvi.Lazy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
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
public class FileEntry {

    private static final Comparator<String> PRIMARY = String::compareToIgnoreCase;
    private static final Comparator<String> SECONDARY = String::compareTo;
    private static final Comparator<FileEntry> ENTRY_ORDER = comparing(FileEntry::name,
                                                                       PRIMARY.thenComparing(SECONDARY));
    private static final LinkOption[] DISTINCTIVE = {LinkOption.NOFOLLOW_LINKS};
    private static final LinkOption[] RESOLVING = {};

    private final Path path;
    private final FileEntry distinct;
    private final Lazy<FileAttributes> lazyAttributes;
    private final Lazy<FileType> lazyType;

    private FileEntry(final Path path, final Normality normal, final FileEntry distinct) {
        this.path = normal.apply(path);
        this.distinct = distinct;
        this.lazyAttributes = Lazy.init(this::newAttributes);
        this.lazyType = Lazy.init(this::newType);
    }

    /**
     * Returns a new {@link FileEntry} based on a given {@link Path}.
     * Symbolic links will not be resolved!
     *
     * @see #resolved()
     */
    public static FileEntry of(final Path path) {
        return new FileEntry(path, Normality.UNKNOWN, null);
    }

    private FileAttributes newAttributes() {
        try {
            final BasicFileAttributes backing =
                    Files.readAttributes(path, BasicFileAttributes.class, (null == distinct) ? DISTINCTIVE : RESOLVING);
            if (backing.isDirectory()) {
                return new DirectoryAttributes(backing);
            } else {
                return new ExistingFileAttributes(backing);
            }
        } catch (final IOException e) {
            // TODO?: problems.add(e);
            return new MissingFileAttributes(path);
        }
    }

    private FileType newType() {
        return FileType.map(lazyAttributes.get());
    }

    /**
     * Returns a {@link FileEntry} that represents the same file as <em>this</em> {@link FileEntry},
     * but symbolic links are resolved. This applies primarily to {@link FileEntry FileEntries} of
     * {@link FileEntry#type() type} {@link FileType#SYMBOLIC SYMBOLIC}, but also of {@link FileEntry#type() type}
     * {@link FileType#DIRECTORY DIRECTORY} if their {@link FileEntry#entries() contents} are in turn of
     * {@link FileEntry#type() type} {@link FileType#SYMBOLIC SYMBOLIC} or {@link FileType#DIRECTORY DIRECTORY}.
     * {@link FileEntry FileEntries} of other types simply return a self-reference.
     */
    public final FileEntry resolved() {
        if (isDistinct() && (isSymbolicLink() || isDirectory())) {
            return new FileEntry(path, Normality.DEFINITE, this);
        } else {
            return this;
        }
    }

    /**
     * Returns a {@link FileEntry} that represents the same file as <em>this</em> {@link FileEntry},
     * but symbolic links are NOT resolved. This applies primarily to {@link FileEntry FileEntries} of
     * {@link FileEntry#type() type} {@link FileType#SYMBOLIC SYMBOLIC}, but also of {@link FileEntry#type() type}
     * {@link FileType#DIRECTORY DIRECTORY} if their {@link FileEntry#entries() contents} are in turn of
     * {@link FileEntry#type() type} {@link FileType#SYMBOLIC SYMBOLIC} or {@link FileType#DIRECTORY DIRECTORY}.
     * {@link FileEntry FileEntries} of other types simply return a self-reference.
     */
    public final FileEntry distinct() {
        return isDistinct() ? this : distinct;
    }

    final boolean isDistinct() {
        return (null == distinct);
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
        return lazyType.get();
    }

    /**
     * Determines if the represented file is a directory.
     */
    public final boolean isDirectory() {
        return lazyAttributes.get().isDirectory();
    }

    /**
     * Determines if the represented file is a regular file.
     */
    public final boolean isRegularFile() {
        return lazyAttributes.get().isRegularFile();
    }

    /**
     * Determines if the represented file is a symbolic link.
     */
    public final boolean isSymbolicLink() {
        return lazyAttributes.get().isSymbolicLink();
    }

    /**
     * Determines if the represented file is something else than a directory, a regular file or a symbolic link.
     * E.g. a special file.
     */
    public final boolean isSpecial() {
        return lazyAttributes.get().isOther();
    }

    /**
     * Determines if the represented file is missing.
     */
    public final boolean isMissing() {
        return type() == FileType.MISSING;
    }

    /**
     * Determines if the represented file exists.
     */
    public final boolean exists() {
        return type() != FileType.MISSING;
    }

    /**
     * Returns the timestamp of the last modification of the represented file.
     *
     * @throws UnsupportedOperationException if the file does not exist.
     */
    public final Instant lastModified() {
        return lazyAttributes.get().lastModifiedTime().toInstant();
    }

    /**
     * Returns the timestamp of the last update of the represented file.
     * <p>
     * A regular file returns the same value as {@link #lastModified()} truncated to a second.
     * A directory returns the most recent timestamp of all files and directories it contains
     * or <em>null</em> if it is empty.
     * Other files always return <em>null</em>.
     */
    public final Instant lastUpdated() {
        switch (type()) {
        case REGULAR:
            return lastModified().truncatedTo(ChronoUnit.SECONDS);
        case DIRECTORY:
            return entries().map(FileEntry::lastUpdated)
                            .filter(Objects::nonNull)
                            .reduce((left, right) -> (left.compareTo(right) < 0) ? right : left)
                            .orElse(null);
        default:
            return null;
        }
    }

    /**
     * Returns the timestamp of the last access to the represented file.
     *
     * @throws UnsupportedOperationException if the file does not exist.
     */
    public final Instant lastAccess() {
        return lazyAttributes.get().lastAccessTime().toInstant();
    }

    /**
     * Returns the timestamp of the creation of the represented file.
     *
     * @throws UnsupportedOperationException if the file does not exist.
     */
    public final Instant creation() {
        return lazyAttributes.get().creationTime().toInstant();
    }

    /**
     * Returns the size of the represented file.
     *
     * @throws UnsupportedOperationException if the file does not exist.
     */
    public final long size() {
        return lazyAttributes.get().size();
    }

    /**
     * Returns the total size of the represented file.
     * <p>
     * A regular file returns the same value as {@link #size()}.
     * A directory returns the sum of the total sizes of all files and directories it contains.
     * Other files always return <em>zero</em>.
     */
    public final long totalSize() {
        switch (type()) {
        case REGULAR:
            return size();
        case DIRECTORY:
            return entries().map(FileEntry::totalSize).reduce(0L, Long::sum);
        default:
            return 0L;
        }
    }

    /**
     * Returns the content of the represented file if it {@link #isDirectory() is a directory}.
     *
     * @throws UnsupportedOperationException if the represented file is not a directory.
     */
    public final Stream<FileEntry> entries() {
        return lazyAttributes.get().entries();
    }

    @Override
    public final String toString() {
        return path.toString();
    }

    private interface FileAttributes extends BasicFileAttributes {

        String PROPERTY_NOT_AVAILABLE =
                "entries not available because the file is not a directory:%n%n" +
                "    path: %s%n%n";

        Path path();

        default Stream<FileEntry> entries() {
            throw new UnsupportedOperationException(format(PROPERTY_NOT_AVAILABLE, path()));
        }
    }

    private static class MissingFileAttributes implements FileAttributes {

        private static final String PROPERTY_NOT_AVAILABLE =
                "property not available because the file does not exist:%n%n" +
                "    path: %s%n%n";

        private final Path path;

        private MissingFileAttributes(final Path path) {
            this.path = path;
        }

        @Override
        public final Path path() {
            return path;
        }

        @Override
        public final FileTime lastModifiedTime() {
            throw new UnsupportedOperationException(format(PROPERTY_NOT_AVAILABLE, path));
        }

        @Override
        public final FileTime lastAccessTime() {
            throw new UnsupportedOperationException(format(PROPERTY_NOT_AVAILABLE, path));
        }

        @Override
        public final FileTime creationTime() {
            throw new UnsupportedOperationException(format(PROPERTY_NOT_AVAILABLE, path));
        }

        @Override
        public final boolean isRegularFile() {
            return false;
        }

        @Override
        public final boolean isDirectory() {
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
        public final long size() {
            throw new UnsupportedOperationException(format(PROPERTY_NOT_AVAILABLE, path));
        }

        @Override
        public final Object fileKey() {
            throw new UnsupportedOperationException(format(PROPERTY_NOT_AVAILABLE, path));
        }
    }

    private class ExistingFileAttributes implements FileAttributes {

        private final BasicFileAttributes backing;

        ExistingFileAttributes(final BasicFileAttributes backing) {
            this.backing = backing;
        }

        @Override
        public final Path path() {
            return path;
        }

        @Override
        public final FileTime lastModifiedTime() {
            return backing.lastModifiedTime();
        }

        @Override
        public final FileTime lastAccessTime() {
            return backing.lastAccessTime();
        }

        @Override
        public final FileTime creationTime() {
            return backing.creationTime();
        }

        @Override
        public final boolean isRegularFile() {
            return backing.isRegularFile();
        }

        @Override
        public final boolean isDirectory() {
            return backing.isDirectory();
        }

        @Override
        public final boolean isSymbolicLink() {
            return backing.isSymbolicLink();
        }

        @Override
        public final boolean isOther() {
            return backing.isOther();
        }

        @Override
        public final long size() {
            return backing.size();
        }

        @Override
        public final Object fileKey() {
            return backing.fileKey();
        }
    }

    private class DirectoryAttributes extends ExistingFileAttributes {

        private final Lazy<Set<FileEntry>> entrySet;

        DirectoryAttributes(final BasicFileAttributes backing) {
            super(backing);
            this.entrySet = Lazy.init(() -> {
                try (final Stream<Path> stream = Files.list(path())) {
                    return stream.map(path -> new FileEntry(path, Normality.DEFINITE, null))
                                 .map(entry -> isDistinct() ? entry : entry.resolved())
                                 .collect(Collectors.toCollection(() -> new TreeSet<>(ENTRY_ORDER)));
                } catch (final IOException caught) {
                    // TODO?: problems.add(caught);
                    return Collections.emptySet();
                }
            });
        }

        @Override
        public Stream<FileEntry> entries() {
            return entrySet.get().stream();
        }
    }
}
