package de.team33.patterns.io.phobos;

import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a file index.
 */
public class FileIndex {

    private static final String NEW_LINE = String.format("%n");

    private final Path root;
    private final Predicate<Path> skipPath;
    private final Predicate<FileEntry> skipEntry;
    private final Function<Path, FileEntry> toFileEntry;

    private FileIndex(final Path root,
                      final Predicate<Path> skipPath,
                      final Predicate<FileEntry> skipEntry,
                      final Function<Path, FileEntry> toFileEntry) {
        this.root = root.toAbsolutePath().normalize();
        this.skipPath = skipPath;
        this.skipEntry = skipEntry;
        this.toFileEntry = toFileEntry;
    }

    /**
     * Returns a file index starting at a given {@link Path root path}.
     * <p>
     * If a {@link Path} points to a symbolic link,
     * a resulting {@link FileEntry#type()} is {@link FileType#SYMBOLIC}.
     */
    public static FileIndex primary(final Path root) {
        return of(root, FileEntry::primary);
    }

    /**
     * Returns a file index starting at a given root path.
     * <p>
     * If a {@link Path} points to a symbolic link,
     * a resulting {@link FileEntry#type()} is {@link FileType#SYMBOLIC}.
     */
    public static FileIndex primary(final String root) {
        return primary(Paths.get(root));
    }

    /**
     * Returns a file index starting at a given {@link Path root path}.
     * <p>
     * If a {@link Path} points to a symbolic link,
     * a resulting {@link FileEntry#type()} corresponds to the linked file.
     */
    public static FileIndex evaluated(final Path root) {
        return of(root, FileEntry::evaluated);
    }

    /**
     * Returns a file index starting at a given root path.
     * <p>
     * If a {@link Path} points to a symbolic link,
     * a resulting {@link FileEntry#type()} corresponds to the linked file.
     */
    public static FileIndex evaluated(final String root) {
        return evaluated(Paths.get(root));
    }

    private static FileIndex of(final Path root, final Function<Path, FileEntry> toFileEntry) {
        return new FileIndex(root, path -> false, entry -> false, toFileEntry);
    }

    /**
     * @deprecated use {@link #primary(Path)} or {@link #evaluated(Path)} instead.
     */
    @Deprecated
    public static FileIndex of(final Path root, final LinkOption ... options) {
        return Arrays.asList(options)
                     .contains(LinkOption.NOFOLLOW_LINKS) ? primary(root) : evaluated(root);
    }

    /**
     * @deprecated use {@link #primary(String)} or {@link #evaluated(String)} instead.
     */
    @Deprecated
    public static FileIndex of(final String root, final LinkOption ... options) {
        return of(Paths.get(root), options);
    }

    /**
     * Returns a new file index based on <em>this</em> file index that skips each path that matches the given
     * {@link Predicate}. If a skipped path is a directory path, this will also skip its contents.
     */
    public final FileIndex skipPath(final Predicate<? super Path> ignorable) {
        return new FileIndex(root, skipPath.or(ignorable), skipEntry, toFileEntry);
    }

    /**
     * Returns a new file index based on <em>this</em> file index that skips each file entry that matches the given
     * {@link Predicate}. If a skipped file entry represents a directory, this will also skip its contents.
     */
    public final FileIndex skipEntry(final Predicate<? super FileEntry> ignorable) {
        return new FileIndex(root, skipPath, skipEntry.or(ignorable), toFileEntry);
    }

    /**
     * Streams all entries of <em>this</em> file index.
     */
    public final Stream<FileEntry> stream() {
        return stream(root);
    }

    private Stream<FileEntry> stream(final Path path) {
        return skipPath.test(path) ? Stream.empty() : stream(toFileEntry.apply(path));
    }

    private Stream<FileEntry> stream(final FileEntry entry) {
        return skipEntry.test(entry) ? Stream.empty() : stream(entry, Stream.of(entry));
    }

    private Stream<FileEntry> stream(final FileEntry entry, final Stream<FileEntry> head) {
        return entry.isDirectory() ? Stream.concat(head, entry.content().stream().flatMap(this::stream)) : head;
    }

    @Override
    public final String toString() {
        return stream().map(this::toString)
                       .collect(Collectors.joining(NEW_LINE));
    }

    private String toString(final FileEntry entry) {
        final int count = entry.path().getNameCount() - root.getNameCount();
        final String name = root.equals(entry.path()) ? root.toString() : entry.name();
        return String.join("", indent(count), name, " : ", entry.type().name(), details(entry), tail(entry));
    }

    private static String indent(final int count) {
        return Stream.generate(() -> "    ")
                     .limit(count)
                     .collect(Collectors.joining());
    }

    private static String tail(final FileEntry entry) {
        return entry.isDirectory() ? " ..." : ";";
    }

    private static String details(final FileEntry entry) {
        return entry.isRegularFile() ? String.format(" (%,d - %s)", entry.size(), entry.lastModified()) : "";
    }
}
