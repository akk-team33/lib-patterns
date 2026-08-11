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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.Comparator.comparing;

/**
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/files-pluto/">files-pluto</a>
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/files-pluto/apidocs/">files-pluto/apidocs</a>
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/files-styx/">files-styx</a>
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/files-styx/apidocs/">files-styx/apidocs</a>
 * @deprecated consider class FileEntry from module
 * <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/files-pluto/apidocs/">files-pluto</a>
 * and/or class Styx from module
 * <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/files-styx/apidocs/">files-styx</a>
 * as replacements.
 */
@Deprecated
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
     * @deprecated see {@link FileEntry}.
     */
    @Deprecated
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
            return new MissingFileAttributes();
        }
    }

    private FileType newType() {
        return FileType.map(lazyAttributes.get());
    }

    /**
     * @deprecated see {@link FileEntry}.
     */
    @Deprecated
    public final FileEntry resolved() {
        if (isDistinct() && (isSymbolicLink() || isDirectory())) {
            return new FileEntry(path, Normality.DEFINITE, this);
        } else {
            return this;
        }
    }

    /**
     * @deprecated see {@link FileEntry}.
     */
    @Deprecated
    public final FileEntry distinct() {
        return isDistinct() ? this : distinct;
    }

    final boolean isDistinct() {
        return (null == distinct);
    }

    /**
     * @deprecated see {@link FileEntry}.
     */
    @Deprecated
    public final Path path() {
        return path;
    }

    /**
     * @deprecated see {@link FileEntry}.
     */
    @Deprecated
    public final String name() {
        return Optional.ofNullable(path.getFileName()).orElse(path).toString();
    }

    /**
     * @deprecated see {@link FileEntry}.
     */
    @Deprecated
    public final FileType type() {
        return lazyType.get();
    }

    /**
     * @deprecated see {@link FileEntry}.
     */
    @Deprecated
    public final boolean isDirectory() {
        return lazyAttributes.get().isDirectory();
    }

    /**
     * @deprecated see {@link FileEntry}.
     */
    @Deprecated
    public final boolean isRegularFile() {
        return lazyAttributes.get().isRegularFile();
    }

    /**
     * @deprecated see {@link FileEntry}.
     */
    @Deprecated
    public final boolean isSymbolicLink() {
        return lazyAttributes.get().isSymbolicLink();
    }

    /**
     * @deprecated see {@link FileEntry}.
     */
    @Deprecated
    public final boolean isSpecial() {
        return lazyAttributes.get().isOther();
    }

    /**
     * @deprecated see {@link FileEntry}.
     */
    @Deprecated
    public final boolean isMissing() {
        return type() == FileType.MISSING;
    }

    /**
     * @deprecated see {@link FileEntry}.
     */
    @Deprecated
    public final boolean exists() {
        return type() != FileType.MISSING;
    }

    /**
     * @deprecated see {@link FileEntry}.
     */
    @Deprecated
    public final Instant lastModified() {
        return lazyAttributes.get().lastModifiedTime().toInstant();
    }

    /**
     * @deprecated see {@link FileEntry}.
     */
    @Deprecated
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
     * @deprecated see {@link FileEntry}.
     */
    @Deprecated
    public final Instant lastAccess() {
        return lazyAttributes.get().lastAccessTime().toInstant();
    }

    /**
     * @deprecated see {@link FileEntry}.
     */
    @Deprecated
    public final Instant creation() {
        return lazyAttributes.get().creationTime().toInstant();
    }

    /**
     * @deprecated see {@link FileEntry}.
     */
    @Deprecated
    public final long size() {
        return lazyAttributes.get().size();
    }

    /**
     * @deprecated see {@link FileEntry}.
     */
    @Deprecated
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
     * @deprecated see {@link FileEntry}.
     */
    @Deprecated
    public final Stream<FileEntry> entries() {
        return lazyAttributes.get().entries();
    }

    /**
     * @deprecated see {@link FileEntry}.
     */
    @Deprecated
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

    private class MissingFileAttributes implements FileAttributes {

        private static final String PROPERTY_NOT_AVAILABLE =
                "property not available because the file does not exist:%n%n" +
                "    path: %s%n%n";

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
