package de.team33.patterns.io.phobos;

import de.team33.patterns.lazy.narvi.Lazy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a virtual file index. Mainly used to produce {@link FileEntry file entries}.
 */
@SuppressWarnings("unused")
public class FileIndex {

    private static final LinkOption[] NO_OPTIONS = {};
    private static final BiConsumer<Path, Exception> IGNORE_FAILED = (path, caught) -> {
    };

    /**
     * A default instance of a {@link FileIndex} that will follow symbolic links.
     */
    public static final FileIndex DEFAULT = new FileIndex(IGNORE_FAILED, entry -> true, NO_OPTIONS);

    /**
     * A default instance of a {@link FileIndex} that will not follow symbolic links.
     */
    public static final FileIndex NO_FOLLOW_LINKS = DEFAULT.withOptions(LinkOption.NOFOLLOW_LINKS);

    private final BiConsumer<Path, Exception> onReadDirFailed;
    private final Predicate<FileEntry> readContent;
    private final LinkOption[] options;

    private FileIndex(final BiConsumer<Path, Exception> onReadDirFailed,
                      final Predicate<FileEntry> readContent,
                      final LinkOption[] options) {
        this.options = options;
        this.readContent = readContent;
        this.onReadDirFailed = onReadDirFailed;
    }

    /**
     * Results in a new {@link FileIndex} with modified behavior if directories cannot be read
     * (for example because the executing user has insufficient access rights).
     * <p>
     * The standard behavior is that corresponding read errors are ignored and affected directories are treated as
     * empty.
     *
     * @param onReadDirFailed A {@link BiConsumer method} that receives the affected {@link FileEntry}
     *                        and the caught {@link Exception} as parameters.
     *                        A typical implementation will log the problem or throw an appropriate exception.
     */
    @SuppressWarnings("ParameterHidesMemberVariable")
    public final FileIndex whenReadDirFailing(final BiConsumer<Path, Exception> onReadDirFailed) {
        return new FileIndex(onReadDirFailed, readContent, options);
    }

    @SuppressWarnings("ParameterHidesMemberVariable")
    public final FileIndex withOptions(final LinkOption... options) {
        return new FileIndex(onReadDirFailed, readContent, options);
    }

    public final FileIndex setReadDirectory(final Predicate<Path> readContent) {
        return setReadDirectoryEntry(entry -> readContent.test(entry.path()));
    }

    public final FileIndex setReadDirectoryEntry(final Predicate<FileEntry> readContent) {
        return new FileIndex(onReadDirFailed, readContent, options);
    }

    public final FileEntry entry(final Path path) {
        final Path normalPath = path.toAbsolutePath().normalize();
        try {
            final BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class, options);
            return attributes.isDirectory() ? new DirEntry(normalPath, attributes) : new Entry(normalPath, attributes);
        } catch (final IOException ignored) {
            return () -> normalPath;
        }
    }

    public final Stream<FileEntry> stream(final Path path) {
        return stream(path, Integer.MAX_VALUE);
    }

    public final Stream<FileEntry> stream(final Path path, final int depth) {
        final FileEntry entry = entry(path);
        final Stream<FileEntry> result = Stream.of(entry);
        if (depth > 0 && entry.isDirectory() && readContent.test(entry)) {
            return Stream.concat(result, content(entry, depth - 1));
        } else {
            return result;
        }
    }

    private Stream<FileEntry> content(final FileEntry entry, final int depth) {
        return entry.content()
                    .stream()
                    .map(FileEntry::path)
                    .flatMap(path -> stream(path, depth));
    }

    private class DirEntry extends Entry {

        private final Lazy<List<FileEntry>> content;

        DirEntry(final Path path, final BasicFileAttributes attributes) {
            super(path, attributes);
            content = Lazy.init(() -> {
                //noinspection OverlyBroadCatchBlock
                try (final Stream<Path> paths = Files.list(path)) {
                    return paths.map(FileIndex.this::entry)
                                .collect(Collectors.toList());
                } catch (final Exception caught) {
                    return contentFailed(caught);
                }
            });
        }

        @Override
        public final List<FileEntry> content() {
            return content.get();
        }

        private List<FileEntry> contentFailed(final Exception caught) {
            onReadDirFailed.accept(path(), caught);
            return Collections.emptyList();
        }
    }

    private static class Entry implements FileEntry {

        private final Path path;
        private final BasicFileAttributes attributes;

        Entry(final Path path, final BasicFileAttributes attributes) {
            this.path = path;
            this.attributes = attributes;
        }

        @Override
        public final Path path() {
            return path;
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
}
