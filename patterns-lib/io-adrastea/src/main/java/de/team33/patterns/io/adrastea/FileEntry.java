package de.team33.patterns.io.adrastea;

import de.team33.patterns.decision.thyone.Choices;
import de.team33.patterns.lazy.narvi.Lazy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Represents an entry from a virtual file index.
 * Includes some meta information about a file, particularly the file system path, file type, size,
 * and some timestamps.
 * <p>
 * Strictly speaking, the meta information only applies to the moment of instantiation.
 * Therefore, an instance should be short-lived. The longer an instance "lives", the more likely it is
 * that the meta information is out of date because the underlying file may have been changed in the meantime.
 */
public class FileEntry {

    /**
     * A {@link Lister} that applies a default path order (by file name)
     */
    public static final Lister LISTER = new Lister(Util.PATH_ORDER, Util.NO_ORDER);

    /**
     * A {@link Streamer} that does not skip any entry
     */
    public static final Streamer STREAMER = streamer(LISTER);

    private final Path path;
    private final Lazy<BasicFileAttributes> lazyAttributes;

    private FileEntry(final Path path, final Normality normality) {
        this.path = normality.apply(path);
        this.lazyAttributes = Lazy.init(this::newAttributes);
    }

    private static BasicFileAttributes newAttributes(final Path path, final LinkOption[] linkOptions) {
        try {
            return Files.readAttributes(path, BasicFileAttributes.class, linkOptions);
        } catch (IOException e) {
            return Util.MISSING_FILE_ATTRIBUTES;
        }
    }

    private static BasicFileAttributes resolved(final BasicFileAttributes attributes) {
        return (attributes instanceof SymLinkAttributes slAttributes) ? slAttributes.resolved() : attributes;
    }

    /**
     * Returns a new {@link FileEntry} based on a given {@link Path}.
     */
    public static FileEntry of(final Path path) {
        return new FileEntry(path, Normality.UNKNOWN);
    }

    private static FileEntry ofDefinite(final Path path) {
        return new FileEntry(path, Normality.DEFINITE);
    }

    public static Streamer streamer(final Lister lister) {
        return new Streamer(lister, entry -> false);
    }

    private BasicFileAttributes newAttributes() {
        final BasicFileAttributes disclosed = newAttributes(path, Util.DISCLOSE_LINKS);
        if (disclosed.isSymbolicLink()) {
            final BasicFileAttributes resolved = newAttributes(path, Util.RESOLVE_LINKS);
            return new SymLinkAttributes(disclosed, resolved);
        }
        return disclosed;
    }

    private BasicFileAttributes attributes() {
        return lazyAttributes.get();
    }

    /**
     * Returns the file system path of the represented file as an
     * {@linkplain Path#toAbsolutePath() absolute} {@linkplain Path#normalize() normalized} {@link Path}.
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
     * Determines if the represented file is a directory.
     * <p>
     * This is also the case if it {@link #isSymbolicLink()} and the finally linked file is a directory.
     */
    public final boolean isDirectory() {
        return attributes().isDirectory();
    }

    /**
     * Determines if the represented file is a regular file.
     * <p>
     * This is also the case if it {@link #isSymbolicLink()} and the finally linked file is a regular file.
     */
    public final boolean isRegularFile() {
        return attributes().isRegularFile();
    }

    /**
     * Determines if the represented file is a special file. Typically, a <em>device</em>.
     * <p>
     * This is also the case if it {@link #isSymbolicLink()} and the finally linked file is a special file.
     */
    public final boolean isSpecialFile() {
        return attributes().isOther();
    }

    /**
     * Determines if the represented file is a symbolic link.
     */
    public final boolean isSymbolicLink() {
        return attributes().isSymbolicLink();
    }

    /**
     * Determines if the represented file is missing.
     * <p>
     * This is also the case if it {@link #isSymbolicLink()} and the finally linked file is missing.
     * <p>
     * NOTE that a symbolic link may be <em>missing</em> and <em>present</em> at the same time
     * if and only if the finally linked file is missing!
     *
     * @see #isSymbolicLink()
     * @see #isPresent()
     */
    public final boolean isMissing() {
        return resolved(attributes()) == Util.MISSING_FILE_ATTRIBUTES;
    }

    /**
     * Determines if the represented file is present.
     * <p>
     * This is also the case if it {@link #isSymbolicLink()} and the finally linked file is missing.
     * <p>
     * NOTE that a symbolic link may be <em>missing</em> and <em>present</em> at the same time
     * if and only if the finally linked file is missing!
     *
     * @see #isSymbolicLink()
     * @see #isMissing()
     */
    public final boolean isPresent() {
        return attributes() != Util.MISSING_FILE_ATTRIBUTES;
    }

    /**
     * Returns the timestamp of the last modification of the represented file as an {@link Instant}.
     * <p>
     * If it {@link #isSymbolicLink()} returns that timestamp of the linked file!
     * <p>
     * If it {@link #isMissing()} returns {@link Instant#MAX}.
     */
    public final Instant lastModified() {
        return attributes().lastModifiedTime().toInstant();
    }

    /**
     * Returns the timestamp of the last access to the represented file as an {@link Instant}.
     * <p>
     * If it not {@link #isPresent()} returns {@link Instant#MAX}.
     */
    public final Instant lastAccess() {
        return attributes().lastAccessTime().toInstant();
    }

    /**
     * Returns the timestamp of the creation of the represented file as an {@link Instant}.
     * <p>
     * If it not {@link #isPresent()} returns {@link Instant#MAX}.
     */
    public final Instant creation() {
        return attributes().creationTime().toInstant();
    }

    /**
     * Returns the size of the represented file.
     * <p>
     * If it {@link #isSymbolicLink()} returns the size of the linked file!
     * <p>
     * If it {@link #isMissing()} returns {@code 0L}.
     */
    public final long size() {
        return attributes().size();
    }

    @Override
    public final String toString() {
        return path.toString();
    }

    /**
     * A tool that serves to list the immediate contents of any directory represented by a {@link FileEntry}.
     */
    public static final class Lister {

        private static final Choices<Lister> CHOICES = Choices.parallel(Lister::isPathOrder, Lister::isEntryOrder);

        private final Comparator<? super Path> pathOrder;
        private final Comparator<? super FileEntry> entryOrder;
        private final Lazy<Function<Stream<Path>, Stream<FileEntry>>> mapping;

        private Lister(final Comparator<? super Path> pathOrder, final Comparator<? super FileEntry> entryOrder) {
            this.pathOrder = pathOrder;
            this.entryOrder = entryOrder;
            this.mapping = Lazy.init(this::newMapping);
        }

        private Function<Stream<Path>, Stream<FileEntry>> newMapping() {
            return switch (CHOICES.apply(this)) {
                case 0b11 -> paths -> paths.sorted(pathOrder)
                                           .map(FileEntry::ofDefinite)
                                           .sorted(entryOrder);
                case 0b10 -> paths -> paths.sorted(pathOrder)
                                           .map(FileEntry::ofDefinite);
                case 0b01 -> paths -> paths.map(FileEntry::ofDefinite)
                                           .sorted(entryOrder);
                default -> paths -> paths.map(FileEntry::ofDefinite);
            };
        }

        private boolean isPathOrder() {
            return Util.NO_ORDER != pathOrder;
        }

        private boolean isEntryOrder() {
            return Util.NO_ORDER != entryOrder;
        }

        /**
         * Returns a {@link List} of the immediate contents of a given directory <em>entry</em>.
         * <p>
         * Returns an empty {@link List} if the given <em>entry</em> does not represent a directory.
         * <p>
         * Also returns an empty {@link List} if the given <em>entry</em> represents a directory but cannot be read,
         * e.g., due to insufficient permissions.
         * <p>
         * In the latter case, the problem is reported to the given {@link Consumer}.
         *
         * @see FileEntry#isDirectory()
         */
        public final List<FileEntry> list(final FileEntry entry, final Consumer<IOProblem> onProblem) {
            if (entry.isDirectory()) {
                try (final Stream<Path> paths = Files.list(entry.path())) {
                    return mapping.get().apply(paths).toList();
                } catch (final IOException caught) {
                    onProblem.accept(new IOProblem(entry.path(), caught));
                }
            }
            return List.of();
        }

        /**
         * Returns a copy of <em>this</em> {@link Lister}, with no order applied to the
         * {@linkplain #list(FileEntry, Consumer) listing}.
         */
        public final Lister noOrder() {
            return new Lister(Util.NO_ORDER, Util.NO_ORDER);
        }

        /**
         * Returns a copy of <em>this</em> {@link Lister}, with the given path order applied to the
         * {@linkplain #list(FileEntry, Consumer) listing}.
         */
        public final Lister pathOrder(final Comparator<? super Path> order) {
            return new Lister(order, entryOrder);
        }

        /**
         * Returns a copy of <em>this</em> {@link Lister}, with the given entry order applied to the
         * {@linkplain #list(FileEntry, Consumer) listing}.
         */
        public final Lister entryOrder(final Comparator<? super FileEntry> order) {
            return new Lister(pathOrder, order);
        }
    }

    /**
     * A tool that serves to stream the recursive contents of any directory represented by a {@link FileEntry}.
     */
    public static final class Streamer {

        private final Lister lister;
        private final Predicate<FileEntry> skipCondition;

        private Streamer(final Lister lister, final Predicate<FileEntry> skipCondition) {
            this.lister = lister;
            this.skipCondition = skipCondition;
        }

        /**
         * Returns a {@link Stream} starting with the given <em>entry</em>,
         * followed by its recursive content if it is a directory.
         * <p>
         * If a problem occurs during the process, such as insufficient permissions,
         * the problem is reported to the given {@link Consumer}.
         *
         * @see FileEntry#isDirectory()
         */
        public final Stream<FileEntry> stream(final FileEntry entry, final Consumer<IOProblem> onProblem) {
            return new Worker(onProblem).stream(entry);
        }

        public final Streamer skip(final Predicate<? super FileEntry> condition) {
            return new Streamer(lister, skipCondition.or(condition));
        }

        private class Worker {

            private final Consumer<IOProblem> onProblem;

            private Worker(final Consumer<IOProblem> onProblem) {
                this.onProblem = onProblem;
            }

            private Stream<FileEntry> stream(final FileEntry entry) {
                if (skipCondition.test(entry)) {
                    return Stream.empty();
                }
                return Stream.concat(Stream.of(entry), lister.list(entry, onProblem).stream().flatMap(this::stream));
            }
        }
    }
}
