package de.team33.patterns.io.phobos;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.singleton;

public class FileIndex {

    private static final Predicate<FileEntry> NEVER = file -> false;

    private final List<FileEntry> roots;
    private Predicate<FileEntry> skipCondition = NEVER;

    private FileIndex(final Stream<? extends FileEntry> entries) {
        this.roots = entries.collect(Collectors.toList());
    }

    public static FileIndex of(final Collection<? extends Path> paths) {
        return new FileIndex(paths.stream().map(FileEntry::of));
    }

    public static FileIndex of(final Path... paths) {
        return of(Arrays.asList(paths));
    }

    public final FileIndex resolved() {
        return new FileIndex(roots.stream().map(FileEntry::resolved));
    }

    public final Stream<FileEntry> entries() {
        return streamAll(roots);
    }

    private Stream<FileEntry> streamAll(final List<FileEntry> entries) {
        return entries.stream()
                      .flatMap(this::streamAll);
    }

    private Stream<FileEntry> streamAll(final FileEntry entry) {
        if (skipCondition.test(entry)) {
            return Stream.empty();
        }

        final Stream<FileEntry> head = Stream.of(entry);
        if (entry.isDirectory()) {
            return Stream.concat(head, streamAll(entry.entries()));
        } else {
            return head;
        }
    }

    public final FileIndex skipEntry(final Predicate<FileEntry> condition) {
        this.skipCondition = condition;
        return this;
    }

    public final FileIndex skipPath(final Predicate<Path> condition) {
        return skipEntry(file -> condition.test(file.path()));
    }

    public final List<FileEntry> roots() {
        return roots;
    }
}
