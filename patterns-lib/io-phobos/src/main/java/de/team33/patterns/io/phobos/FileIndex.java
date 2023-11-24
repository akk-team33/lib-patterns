package de.team33.patterns.io.phobos;

import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FileIndex {

    private final Path root;
    private final Predicate<Path> skipPath;
    private final Predicate<FileEntry> skipEntry;
    private final LinkOption[] options;

    private FileIndex(final Path root,
                      final Predicate<Path> skipPath,
                      final Predicate<FileEntry> skipEntry,
                      final LinkOption[] options) {
        this.root = root;
        this.skipPath = skipPath;
        this.skipEntry = skipEntry;
        this.options = options;
    }

    public static FileIndex of(final Path root, final LinkOption ... options) {
        return new FileIndex(root, path -> false, entry -> false, options);
    }

    public static FileIndex of(final String root, final LinkOption ... options) {
        return of(Paths.get(root), options);
    }

    public final FileIndex skipPath(final Predicate<? super Path> ignorable) {
        return new FileIndex(root, skipPath.or(ignorable), skipEntry, options);
    }

    public final FileIndex skipEntry(final Predicate<? super FileEntry> ignorable) {
        return new FileIndex(root, skipPath, skipEntry.or(ignorable), options);
    }

    public final Stream<FileEntry> stream() {
        return stream(root);
    }

    private Stream<FileEntry> stream(final Path path) {
        return skipPath.test(path) ? Stream.empty() : stream(FileEntry.of(path, options));
    }

    private Stream<FileEntry> stream(final FileEntry entry) {
        return skipEntry.test(entry) ? Stream.empty() : stream(entry, Stream.of(entry));
    }

    private Stream<FileEntry> stream(final FileEntry entry, final Stream<FileEntry> head) {
        return entry.isDirectory() ? Stream.concat(head, entry.content().stream().flatMap(this::stream)) : head;
    }
}
