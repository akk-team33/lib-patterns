package de.team33.patterns.io.phobos;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
public class FileIndex {

    private static final Predicate<FileEntry> NEVER = file -> false;

    private final List<FileEntry> roots;
    private Predicate<FileEntry> skipCondition = NEVER;

    private FileIndex(final Stream<? extends FileEntry> entries) {
        this.roots = entries.collect(Collectors.toList());
    }

    /**
     * @deprecated see {@link FileIndex}.
     */
    @Deprecated
    public static FileIndex of(final Collection<? extends Path> paths) {
        return new FileIndex(paths.stream().map(FileEntry::of));
    }

    /**
     * @deprecated see {@link FileIndex}.
     */
    @Deprecated
    public static FileIndex of(final Path... paths) {
        return of(Arrays.asList(paths));
    }

    /**
     * @deprecated see {@link FileIndex}.
     */
    @Deprecated
    public final FileIndex resolved() {
        return new FileIndex(roots.stream().map(FileEntry::resolved));
    }

    /**
     * @deprecated see {@link FileIndex}.
     */
    @Deprecated
    public final FileIndex distinct() {
        return new FileIndex(roots.stream().map(FileEntry::distinct));
    }

    /**
     * @deprecated see {@link FileIndex}.
     */
    @Deprecated
    public final Stream<FileEntry> entries() {
        return streamAll(roots.stream());
    }

    private Stream<FileEntry> streamAll(final Stream<FileEntry> entries) {
        return entries.flatMap(this::streamAll);
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

    /**
     * @deprecated see {@link FileIndex}.
     */
    @Deprecated
    public final FileIndex skipEntry(final Predicate<FileEntry> condition) {
        this.skipCondition = condition;
        return this;
    }

    /**
     * @deprecated see {@link FileIndex}.
     */
    @Deprecated
    public final FileIndex skipPath(final Predicate<Path> condition) {
        return skipEntry(file -> condition.test(file.path()));
    }

    /**
     * @deprecated see {@link FileIndex}.
     */
    @Deprecated
    public final List<FileEntry> roots() {
        return roots;
    }
}
