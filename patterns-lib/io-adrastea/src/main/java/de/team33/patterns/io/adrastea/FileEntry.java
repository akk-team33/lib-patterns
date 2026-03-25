package de.team33.patterns.io.adrastea;

import de.team33.patterns.decision.thyone.Choices;
import de.team33.patterns.hierarchy.mab.Nodes;
import de.team33.patterns.lazy.narvi.Lazy;

import java.io.IOException;
import java.nio.file.Files;
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

import static de.team33.patterns.io.adrastea.LinkHandling.DISCLOSE;
import static de.team33.patterns.io.adrastea.LinkHandling.RESOLVE;

/**
 * Represents an entry from a virtual file index.
 * Includes some meta information about a file, particularly the file system path, file type, size,
 * and some timestamps.
 * <p>
 * Strictly speaking, the meta information only applies to the moment of instantiation.
 * Therefore, an instance should be short-lived. The longer an instance "lives", the more likely it is
 * that the meta information is out of date because the underlying file may have been changed in the meantime.
 * <p>
 * Use {@link #of(Path, LinkHandling)}, {@link #disclosed(Path)} or {@link #resolved(Path)}
 * to get a new instance.
 */
@SuppressWarnings("unused")
public class FileEntry {

    private final Path path;
    private final Lazy<BasicFileAttributes> lazyAttributes;

    private FileEntry(final Path path, final Normality normality, final LinkHandling linkHandling) {
        this.path = normality.apply(path);
        this.lazyAttributes = Lazy.init(() -> newAttributes(linkHandling));
    }

    private static BasicFileAttributes newAttributes(final Path path, final LinkHandling handling) {
        try {
            return Files.readAttributes(path, BasicFileAttributes.class, handling.options());
        } catch (final IOException ignored) {
            return Util.MISSING_FILE_ATTRIBUTES;
        }
    }

    /**
     * Returns a new {@link FileEntry} based on a given {@link Path} and a given {@link LinkHandling}.
     */
    public static FileEntry of(final Path path, final LinkHandling linkHandling) {
        return new FileEntry(path, Normality.UNKNOWN, linkHandling);
    }

    /**
     * Returns a new {@link FileEntry} based on a given {@link Path} that {@link #isDisclosed()}.
     */
    public static FileEntry disclosed(final Path path) {
        return of(path, DISCLOSE);
    }

    /**
     * Returns a new {@link FileEntry} based on a given {@link Path} that {@link #isResolved()}.
     */
    public static FileEntry resolved(final Path path) {
        return of(path, RESOLVE);
    }

    private static FileEntry ofDefinite(final Path path, final LinkHandling linkHandling) {
        return new FileEntry(path, Normality.DEFINITE, linkHandling);
    }

    /**
     * Returns a new {@link Lister} based on a given {@link LinkHandling}
     * that applies a default path order (by file name).
     */
    public static Lister lister(final LinkHandling linkHandling) {
        return new Lister(linkHandling, Util.PATH_ORDER, Util.NO_ORDER);
    }

    /**
     * Returns a new {@link Streamer} based on a given {@link LinkHandling}
     * that does not skip any entry.
     */
    public static Streamer streamer(final LinkHandling linkHandling) {
        return streamer(lister(linkHandling));
    }

    /**
     * Returns a new {@link Streamer} based on a given {@link Lister}
     * that does not skip any entry.
     */
    public static Streamer streamer(final Lister lister) {
        return new Streamer(lister, entry -> false);
    }

    private static BasicFileAttributes effective(final BasicFileAttributes attributes) {
        return (attributes instanceof LinkAttributes linkAttributes) ? linkAttributes.backing() : attributes;
    }

    private BasicFileAttributes newAttributes(final LinkHandling handling) {
        final BasicFileAttributes disclosed = newAttributes(path, DISCLOSE);
        if (!disclosed.isSymbolicLink()) {
            return disclosed;
        }
        if (DISCLOSE == handling) {
            return new LinkAttributes(DISCLOSE, disclosed);
        } else {
            return new LinkAttributes(handling, newAttributes(path, handling));
        }
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
     * Returns a {@link FileEntry} based on <em>this</em>' {@link #path()} that definitely {@link #isDisclosed()}.
     */
    public final FileEntry disclosed() {
        return isDisclosed() ? this : new FileEntry(path, Normality.DEFINITE, DISCLOSE);
    }

    /**
     * Returns a {@link FileEntry} based on <em>this</em>' {@link #path()} that definitely {@link #isResolved()}.
     */
    public final FileEntry resolved() {
        return isResolved() ? this : new FileEntry(path, Normality.DEFINITE, RESOLVE);
    }

    /**
     * Determines whether <em>this</em> {@link FileEntry} discloses its original attributes,
     * even if it {@linkplain #isSymbolicLink() is a symbolic link}.
     *
     * @see #isResolved()
     */
    public final boolean isDisclosed() {
        if (attributes() instanceof LinkAttributes linkAttributes) {
            return DISCLOSE == linkAttributes.handling();
        } else {
            return true;
        }
    }

    /**
     * Determines whether <em>this</em> {@link FileEntry} resolves its final attributes,
     * even if it {@linkplain #isSymbolicLink() is a symbolic link}.
     *
     * @see #isDisclosed()
     */
    public final boolean isResolved() {
        if (attributes() instanceof LinkAttributes linkAttributes) {
            return RESOLVE == linkAttributes.handling();
        } else {
            return true;
        }
    }

    /**
     * Determines if the represented file is a directory.
     * <p>
     * This may also be the case if it {@link #isSymbolicLink()} and {@link #isResolved()}.
     */
    public final boolean isDirectory() {
        return attributes().isDirectory();
    }

    /**
     * Determines if the represented file is a regular file.
     * <p>
     * This may also be the case if it {@link #isSymbolicLink()} and {@link #isResolved()}.
     */
    public final boolean isRegularFile() {
        return attributes().isRegularFile();
    }

    /**
     * Determines if the represented file is a special file (typically, a <em>device</em>).
     * <p>
     * This may also be the case if it {@link #isSymbolicLink()} and {@link #isResolved()}.
     */
    public final boolean isSpecialFile() {
        return attributes().isOther();
    }

    /**
     * Determines if the represented file is a symbolic link.
     * <p>
     * No matter if it {@link #isDisclosed()} or {@link #isResolved()}.
     */
    public final boolean isSymbolicLink() {
        return attributes().isSymbolicLink();
    }

    /**
     * Determines if the represented file is missing.
     * <p>
     * This may also be the case if it {@link #isSymbolicLink()} and {@link #isResolved()}.
     * NOTE that in this case, it {@link #isPresent()}, too!
     */
    public final boolean isMissing() {
        return effective(attributes()) == Util.MISSING_FILE_ATTRIBUTES;
    }

    /**
     * Determines if the represented file is present.
     * <p>
     * That is always the case if {@link #isRegularFile()}, {@link #isDirectory()}, {@link #isSpecialFile()}
     * or {@link #isSymbolicLink()}, and therefore especially if a symbolic link {@link #isMissing()}!
     */
    public final boolean isPresent() {
        return attributes() != Util.MISSING_FILE_ATTRIBUTES;
    }

    /**
     * Returns the timestamp of the last modification of the represented file as an {@link Instant}.
     *
     * @throws UnsupportedOperationException if <em>this</em> {@link #isMissing()}
     */
    public final Instant lastModified() {
        return attributes().lastModifiedTime().toInstant();
    }

    /**
     * Returns the timestamp of the last access to the represented file as an {@link Instant}.
     *
     * @throws UnsupportedOperationException if <em>this</em> {@link #isMissing()}
     */
    public final Instant lastAccess() {
        return attributes().lastAccessTime().toInstant();
    }

    /**
     * Returns the timestamp of the creation of the represented file as an {@link Instant}.
     *
     * @throws UnsupportedOperationException if <em>this</em> {@link #isMissing()}
     */
    public final Instant creation() {
        return attributes().creationTime().toInstant();
    }

    /**
     * Returns the size of the represented file.
     * <p>
     * If <em>this</em> {@link #isMissing()} returns {@code 0L}.
     */
    public final long size() {
        return attributes().size();
    }

    @Override
    public final String toString() {
        return path.toString();
    }

    public record Problem(FileEntry node, IOException cause) implements Nodes.Problem<FileEntry> {
    }

    /**
     * A tool that serves to list the immediate contents of any file represented by a {@link FileEntry}.
     */
    public static final class Lister implements Nodes.Lister<FileEntry, Problem> {

        private static final Choices<Lister> CHOICES = Choices.parallel(Lister::isPathOrder, Lister::isEntryOrder);

        private final LinkHandling linkHandling;
        private final Comparator<? super Path> pathOrder;
        private final Comparator<? super FileEntry> entryOrder;
        private final Lazy<Function<Stream<Path>, Stream<FileEntry>>> mapping;

        private Lister(final LinkHandling linkHandling,
                       final Comparator<? super Path> pathOrder,
                       final Comparator<? super FileEntry> entryOrder) {
            this.linkHandling = linkHandling;
            this.pathOrder = pathOrder;
            this.entryOrder = entryOrder;
            this.mapping = Lazy.init(this::newMapping);
        }

        private FileEntry entryOfDefinite(final Path path) {
            return ofDefinite(path, linkHandling);
        }

        private FileEntry entryOf(final Path path) {
            return of(path, linkHandling);
        }

        private Function<Stream<Path>, Stream<FileEntry>> newMapping() {
            return switch (CHOICES.apply(this)) {
                case 0b11 -> paths -> paths.sorted(pathOrder)
                                           .map(this::entryOfDefinite)
                                           .sorted(entryOrder);
                case 0b10 -> paths -> paths.sorted(pathOrder)
                                           .map(this::entryOfDefinite);
                case 0b01 -> paths -> paths.map(this::entryOfDefinite)
                                           .sorted(entryOrder);
                default -> paths -> paths.map(this::entryOfDefinite);
            };
        }

        private boolean isPathOrder() {
            return Util.NO_ORDER != pathOrder;
        }

        private boolean isEntryOrder() {
            return Util.NO_ORDER != entryOrder;
        }

        private LinkHandling linkHandling() {
            return linkHandling;
        }

        /**
         * Returns a {@link List} of the immediate contents of a given <em>path</em> from a directory structure.
         * <p>
         * Returns an empty {@link List} if the given <em>path</em> does not represent a directory
         * and thus cannot have any directory contents.
         * <p>
         * Also returns an empty {@link List} if the given <em>path</em> refuses access to its contents
         * and throws an exception. In that case, the problem will be logged to a {@link System.Logger}.
         * <p>
         * NOTE: an original {@link FileEntry} will be created from the given <em>path</em> using the associated
         * {@link LinkHandling}. If this does not meet your requirements, use {@link #list(FileEntry)} instead.
         *
         * @see #list(FileEntry)
         * @see #list(Path, Consumer)
         * @see #list(FileEntry, Consumer)
         */
        public final List<FileEntry> list(final Path path) {
            return list(entryOf(path));
        }

        /**
         * Returns a {@link List} of the immediate contents of a given <em>entry</em> from a directory structure.
         * <p>
         * Returns an empty {@link List} if the given <em>entry</em> does not represent a directory
         * and thus cannot have any directory contents.
         * <p>
         * Also returns an empty {@link List} if the given <em>entry</em> refuses access to its contents
         * and thus throws an exception. In that case, the problem will be logged to a {@link System.Logger}.
         *
         * @see #list(Path)
         * @see #list(Path, Consumer)
         * @see #list(FileEntry, Consumer)
         */
        @Override
        public final List<FileEntry> list(final FileEntry entry) {
            return Nodes.Lister.super.list(entry);
        }

        /**
         * Returns a {@link List} of the immediate contents of a given <em>path</em> from a directory structure.
         * <p>
         * Returns an empty {@link List} if the given <em>path</em> does not represent a directory
         * and thus cannot have any directory contents.
         * <p>
         * Also returns an empty {@link List} if the given <em>path</em> refuses access to its contents
         * and thus throws an exception. In that case, a corresponding {@link Problem} will be reported
         * to the given {@link Consumer}.
         * <p>
         * NOTE: an original {@link FileEntry} will be created from the given <em>path</em> using the associated
         * {@link LinkHandling}. If this does not meet your requirements, use {@link #list(FileEntry, Consumer)}
         * instead.
         *
         * @see #list(FileEntry, Consumer)
         * @see #list(Path)
         * @see #list(FileEntry)
         */
        public final List<FileEntry> list(final Path path, final Consumer<? super Problem> onProblem) {
            return list(entryOf(path), onProblem);
        }

        /**
         * Returns a {@link List} of the immediate contents of a given <em>entry</em> from a directory structure.
         * <p>
         * Returns an empty {@link List} if the given <em>entry</em> does not represent a directory
         * and thus cannot have any directory contents.
         * <p>
         * Also returns an empty {@link List} if the given <em>entry</em> refuses access to its contents
         * and throws an exception. In that case, a corresponding {@link Problem} will be reported
         * to the given {@link Consumer}.
         *
         * @see #list(Path, Consumer)
         * @see #list(FileEntry)
         * @see #list(Path)
         */
        @Override
        public final List<FileEntry> list(final FileEntry entry, final Consumer<? super Problem> onProblem) {
            if (entry.isDirectory()) {
                try (final Stream<Path> paths = Files.list(entry.path())) {
                    return mapping.get().apply(paths).toList();
                } catch (final IOException caught) {
                    onProblem.accept(new Problem(entry, caught));
                }
            }
            return List.of();
        }

        /**
         * Returns a copy of <em>this</em> {@link Lister}, with no order applied to the
         * {@linkplain #list(FileEntry, Consumer) listing}.
         */
        public final Lister noOrder() {
            return new Lister(linkHandling, Util.NO_ORDER, Util.NO_ORDER);
        }

        /**
         * Returns a copy of <em>this</em> {@link Lister}, with the given path order applied to the
         * {@linkplain #list(FileEntry, Consumer) listing}.
         */
        public final Lister pathOrder(final Comparator<? super Path> order) {
            return new Lister(linkHandling, order, entryOrder);
        }

        /**
         * Returns a copy of <em>this</em> {@link Lister}, with the given entry order applied to the
         * {@linkplain #list(FileEntry, Consumer) listing}.
         */
        public final Lister entryOrder(final Comparator<? super FileEntry> order) {
            return new Lister(linkHandling, pathOrder, order);
        }
    }

    /**
     * A tool that serves to stream the recursive contents of any directory represented by a {@link FileEntry}.
     */
    public static final class Streamer extends Nodes.Streamer<FileEntry, Problem, Lister> {

        private Streamer(final Lister lister, final Predicate<FileEntry> skipCondition) {
            super(lister, skipCondition);
        }

        private FileEntry entryOf(final Path path) {
            return of(path, lister().linkHandling());
        }

        /**
         * Returns a new {@link Streamer} that skips all entries that meet the given <em>condition</em>,
         * as well as their entire content.
         */
        public final Streamer skip(final Predicate<? super FileEntry> condition) {
            return new Streamer(lister(), skipCondition().or(condition));
        }

        /**
         * Returns a {@link Stream} starting with a {@link FileEntry} based on the given <em>path</em>
         * followed by its recursive contents.
         * <p>
         * If an involved file refuses access to its contents and thus throws an {@link IOException},
         * the problem will be logged to a {@link System.Logger}.
         * <p>
         * NOTE: the starting {@link FileEntry} will be created using the {@link LinkHandling} of the associated
         * {@link Lister}. If this does not meet your requirements, use {@link #stream(FileEntry)} instead.
         *
         * @see #stream(FileEntry)
         * @see #stream(Path, Consumer)
         * @see #stream(FileEntry, Consumer)
         */
        public final Stream<FileEntry> stream(final Path path) {
            return stream(entryOf(path));
        }

        /**
         * Returns a {@link Stream} starting with the given <em>entry</em> followed by its recursive contents.
         * <p>
         * If an involved <em>entry</em> refuses access to its contents and thus throws an exception,
         * the problem will be logged to a {@link System.Logger}.
         *
         * @see #stream(Path)
         * @see #stream(Path, Consumer)
         * @see #stream(FileEntry, Consumer)
         */
        @Override
        public final Stream<FileEntry> stream(final FileEntry entry) {
            return super.stream(entry);
        }

        /**
         * Returns a {@link Stream} starting with a {@link FileEntry} based on the given <em>path</em>
         * followed by its recursive contents.
         * <p>
         * If an involved file refuses access to its contents and thus throws an {@link IOException},
         * a corresponding {@link Problem} will be reported to the given {@link Consumer}.
         * <p>
         * NOTE: the starting {@link FileEntry} will be created using the {@link LinkHandling} of the associated
         * {@link Lister}. If this does not meet your requirements, use {@link #stream(FileEntry, Consumer)} instead.
         *
         * @see #stream(FileEntry, Consumer)
         * @see #stream(Path)
         * @see #stream(FileEntry)
         */
        public final Stream<FileEntry> stream(final Path path, final Consumer<? super Problem> onProblem) {
            return stream(entryOf(path), onProblem);
        }

        /**
         * Returns a {@link Stream} starting with the given <em>entry</em> followed by its recursive contents.
         * <p>
         * If an involved <em>entry</em> refuses access to its contents and thus throws an exception,
         * a corresponding {@link Problem} will be reported to the given {@link Consumer}.
         *
         * @see #stream(Path, Consumer)
         * @see #stream(FileEntry)
         * @see #stream(Path)
         */
        @Override
        public final Stream<FileEntry> stream(final FileEntry entry, final Consumer<? super Problem> onProblem) {
            return super.stream(entry, onProblem);
        }
    }
}
