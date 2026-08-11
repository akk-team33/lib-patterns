package de.team33.patterns.io.adrastea;

import de.team33.patterns.decision.thyone.Choices;
import de.team33.patterns.enums.pan.Values;
import de.team33.patterns.hierarchy.mab.Nodes;
import de.team33.patterns.lazy.narvi.Lazy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/files-pluto/">files-pluto</a>
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/files-pluto/apidocs/">files-pluto/apidocs</a>
 * @deprecated consider class FileEntry from module
 * <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/files-pluto/apidocs/">files-pluto</a>
 * as a replacement.
 */
@SuppressWarnings("unused")
@Deprecated
public class FileEntry {

    private final Path path;
    private final Lazy<BasicFileAttributes> lazyAttributes;
    private final Lazy<Type> lazyType;

    private FileEntry(final Path path, final Normality normality, final LinkHandling linkHandling) {
        this.path = normality.apply(path);
        this.lazyAttributes = Lazy.init(() -> newAttributes(linkHandling));
        this.lazyType = Lazy.init(() -> Type.of(this));
    }

    private static BasicFileAttributes newAttributes(final Path path, final LinkHandling handling) {
        try {
            return Files.readAttributes(path, BasicFileAttributes.class, handling.options());
        } catch (final IOException ignored) {
            return Util.MISSING_FILE_ATTRIBUTES;
        }
    }

    /**
     * @deprecated see {@link FileEntry}
     */
    @Deprecated
    public static FileEntry of(final Path path, final LinkHandling linkHandling) {
        return new FileEntry(path, Normality.UNKNOWN, linkHandling);
    }

    /**
     * @deprecated see {@link FileEntry}
     */
    @Deprecated
    public static FileEntry original(final Path path) {
        return of(path, LinkHandling.ORIGINAL);
    }

    /**
     * @deprecated see {@link FileEntry}
     */
    @Deprecated
    public static FileEntry resolved(final Path path) {
        return of(path, LinkHandling.RESOLVE);
    }

    private static FileEntry ofDefinite(final Path path, final LinkHandling linkHandling) {
        return new FileEntry(path, Normality.DEFINITE, linkHandling);
    }

    /**
     * @deprecated see {@link FileEntry}
     */
    @Deprecated
    public static Lister lister(final LinkHandling linkHandling) {
        return new Lister(linkHandling, Util.PATH_ORDER, Util.NO_ORDER);
    }

    /**
     * @deprecated see {@link FileEntry}
     */
    @Deprecated
    public static Streamer streamer(final LinkHandling linkHandling) {
        return streamer(lister(linkHandling));
    }

    /**
     * @deprecated see {@link FileEntry}
     */
    @Deprecated
    public static Streamer streamer(final Lister lister) {
        return new Streamer(lister, null);
    }

    private static BasicFileAttributes effective(final BasicFileAttributes attributes) {
        return (attributes instanceof LinkAttributes linkAttributes) ? linkAttributes.backing() : attributes;
    }

    private BasicFileAttributes newAttributes(final LinkHandling handling) {
        final BasicFileAttributes original = newAttributes(path, LinkHandling.ORIGINAL);
        if (original.isSymbolicLink()) {
            return newLinkAttributes(handling, original);
        } else {
            return original;
        }
    }

    private LinkAttributes newLinkAttributes(final LinkHandling handling, final BasicFileAttributes original) {
        if (LinkHandling.ORIGINAL == handling) {
            return new LinkAttributes(LinkHandling.ORIGINAL, original);
        } else {
            return new LinkAttributes(handling, newAttributes(path, handling));
        }
    }

    private BasicFileAttributes attributes() {
        return lazyAttributes.get();
    }

    /**
     * @deprecated see {@link FileEntry}
     */
    @Deprecated
    public final Path path() {
        return path;
    }

    /**
     * @deprecated see {@link FileEntry}
     */
    @Deprecated
    public final String name() {
        return Optional.ofNullable(path.getFileName()).orElse(path).toString();
    }

    /**
     * @deprecated see {@link FileEntry}
     */
    @Deprecated
    public final FileEntry original() {
        return isOriginal() ? this : new FileEntry(path, Normality.DEFINITE, LinkHandling.ORIGINAL);
    }

    /**
     * @deprecated see {@link FileEntry}
     */
    @Deprecated
    public final FileEntry resolved() {
        return isResolved() ? this : new FileEntry(path, Normality.DEFINITE, LinkHandling.RESOLVE);
    }

    /**
     * @deprecated see {@link FileEntry}
     */
    @Deprecated
    public final Type type() {
        return lazyType.get();
    }

    /**
     * @deprecated see {@link FileEntry}
     */
    @Deprecated
    public final boolean isOriginal() {
        if (attributes() instanceof LinkAttributes linkAttributes) {
            return LinkHandling.ORIGINAL == linkAttributes.handling();
        } else {
            return true;
        }
    }

    /**
     * @deprecated see {@link FileEntry}
     */
    @Deprecated
    public final boolean isResolved() {
        if (attributes() instanceof LinkAttributes linkAttributes) {
            return LinkHandling.RESOLVE == linkAttributes.handling();
        } else {
            return true;
        }
    }

    /**
     * @deprecated see {@link FileEntry}
     */
    @Deprecated
    public final boolean isDirectory() {
        return attributes().isDirectory();
    }

    /**
     * @deprecated see {@link FileEntry}
     */
    @Deprecated
    public final boolean isRegularFile() {
        return attributes().isRegularFile();
    }

    /**
     * @deprecated see {@link FileEntry}
     */
    @Deprecated
    public final boolean isSpecialFile() {
        return attributes().isOther();
    }

    /**
     * @deprecated see {@link FileEntry}
     */
    @Deprecated
    public final boolean isSymbolicLink() {
        return attributes().isSymbolicLink();
    }

    /**
     * @deprecated see {@link FileEntry}
     */
    @Deprecated
    public final boolean isMissing() {
        return effective(attributes()) == Util.MISSING_FILE_ATTRIBUTES;
    }

    /**
     * @deprecated see {@link FileEntry}
     */
    @Deprecated
    public final boolean isPresent() {
        return attributes() != Util.MISSING_FILE_ATTRIBUTES;
    }

    /**
     * @deprecated see {@link FileEntry}
     */
    @Deprecated
    public final Instant lastModified() {
        return attributes().lastModifiedTime().toInstant();
    }

    /**
     * @deprecated see {@link FileEntry}
     */
    @Deprecated
    public final Instant lastAccess() {
        return attributes().lastAccessTime().toInstant();
    }

    /**
     * @deprecated see {@link FileEntry}
     */
    @Deprecated
    public final Instant creation() {
        return attributes().creationTime().toInstant();
    }

    /**
     * @deprecated see {@link FileEntry}
     */
    @Deprecated
    public final long size() {
        return attributes().size();
    }

    /**
     * @deprecated see {@link FileEntry}
     */
    @Deprecated
    @Override
    public final String toString() {
        return path.toString();
    }

    /**
     * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/files-pluto/">files-pluto</a>
     * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/files-pluto/apidocs/">files-pluto/apidocs</a>
     * @deprecated consider class FileType from module
     * <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/files-pluto/apidocs/">files-pluto</a>
     * as a replacement
     */
    @Deprecated
    public enum Type {

        /**
         * @deprecated see {@link Type}.
         */
        @Deprecated
        REGULAR_FILE(FileEntry::isRegularFile),

        /**
         * @deprecated see {@link Type}.
         */
        @Deprecated
        DIRECTORY(FileEntry::isDirectory),

        /**
         * @deprecated see {@link Type}.
         */
        @Deprecated
        SPECIAL_FILE(FileEntry::isSpecialFile),

        /**
         * @deprecated see {@link Type}.
         */
        @Deprecated
        SYMBOLIC_LINK(Util.and(FileEntry::isOriginal, FileEntry::isSymbolicLink)),

        /**
         * @deprecated see {@link Type}.
         */
        @Deprecated
        MISSING(FileEntry::isMissing);

        private static final Values<Type> VALUES = Values.of(Type.class);
        private static final String UNKNOWN_TYPE = "Unknown type: <%s>";

        private final Predicate<FileEntry> predicate;

        Type(final Predicate<FileEntry> predicate) {
            this.predicate = predicate;
        }

        private static Type of(final FileEntry entry) {
            return VALUES.findFirst(type -> type.predicate.test(entry))
                         .orElseThrow(() -> new NoSuchElementException(UNKNOWN_TYPE.formatted(entry)));
        }
    }

    /**
     * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/files-styx/">files-styx</a>
     * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/files-styx/apidocs/">files-styx/apidocs</a>
     * @deprecated consider class Problem from module
     * <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/files-styx/apidocs/">files-styx</a>
     * as a replacement.
     */
    @Deprecated
    public record Problem(FileEntry node, IOException cause) implements Nodes.Problem<FileEntry> {
    }

    /**
     * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/files-styx/">files-styx</a>
     * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/files-styx/apidocs/">files-styx/apidocs</a>
     * @deprecated consider class Styx from module
     * <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/files-styx/apidocs/">files-styx</a>
     * as a replacement
     */
    @Deprecated
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
         * @deprecated see {@link Lister}.
         */
        @Deprecated
        public final Lister resolved() {
            return (LinkHandling.RESOLVE == linkHandling) ? this : new Lister(LinkHandling.RESOLVE, pathOrder, entryOrder);
        }

        /**
         * @deprecated see {@link Lister}.
         */
        @Deprecated
        public final Lister original() {
            return (LinkHandling.ORIGINAL == linkHandling) ? this : new Lister(LinkHandling.ORIGINAL, pathOrder, entryOrder);
        }

        /**
         * @deprecated see {@link Lister}.
         */
        @Deprecated
        public final List<FileEntry> list(final Path path) {
            return list(entryOf(path));
        }

        /**
         * @deprecated see {@link Lister}.
         */
        @Deprecated
        @Override
        public final List<FileEntry> list(final FileEntry entry) {
            return Nodes.Lister.super.list(entry);
        }

        /**
         * @deprecated see {@link Lister}.
         */
        @Deprecated
        public final List<FileEntry> list(final Path path, final Consumer<? super Problem> onProblem) {
            return list(entryOf(path), onProblem);
        }

        /**
         * @deprecated see {@link Lister}.
         */
        @Deprecated
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
         * @deprecated see {@link Lister}.
         */
        @Deprecated
        public final Lister noOrder() {
            return new Lister(linkHandling, Util.NO_ORDER, Util.NO_ORDER);
        }

        /**
         * @deprecated see {@link Lister}.
         */
        @Deprecated
        public final Lister pathOrder(final Comparator<? super Path> order) {
            return new Lister(linkHandling, order, entryOrder);
        }

        /**
         * @deprecated see {@link Lister}.
         */
        @Deprecated
        public final Lister entryOrder(final Comparator<? super FileEntry> order) {
            return new Lister(linkHandling, pathOrder, order);
        }
    }

    /**
     * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/files-styx/">files-styx</a>
     * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/files-styx/apidocs/">files-styx/apidocs</a>
     * @deprecated consider class Styx from module
     * <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/files-styx/apidocs/">files-styx</a>
     * as a replacement
     */
    @Deprecated
    public static final class Streamer extends Nodes.Streamer<FileEntry, Problem, Lister> {

        private Streamer(final Lister lister, final Predicate<FileEntry> skipCondition) {
            super(lister, skipCondition);
        }

        private FileEntry entryOf(final Path path) {
            return of(path, lister().linkHandling());
        }

        /**
         * @deprecated see {@link Streamer}.
         */
        @Deprecated
        public final Streamer resolved() {
            return (LinkHandling.RESOLVE == lister().linkHandling) ? this : new Streamer(lister().resolved(), skipCondition());
        }

        /**
         * @deprecated see {@link Streamer}.
         */
        @Deprecated
        public final Streamer original() {
            return (LinkHandling.ORIGINAL == lister().linkHandling) ? this : new Streamer(lister().original(), skipCondition());
        }

        /**
         * @deprecated see {@link Streamer}.
         */
        @Deprecated
        public final Streamer skip(final Predicate<? super FileEntry> condition) {
            return new Streamer(lister(), skipCondition().or(condition));
        }

        /**
         * @deprecated see {@link Streamer}.
         */
        @Deprecated
        public final Stream<FileEntry> stream(final Path path) {
            return stream(entryOf(path));
        }

        /**
         * @deprecated see {@link Streamer}.
         */
        @Deprecated
        @Override
        public final Stream<FileEntry> stream(final FileEntry entry) {
            return super.stream(entry);
        }

        /**
         * @deprecated see {@link Streamer}.
         */
        @Deprecated
        public final Stream<FileEntry> stream(final Path path, final Consumer<? super Problem> onProblem) {
            return stream(entryOf(path), onProblem);
        }

        /**
         * @deprecated see {@link Streamer}.
         */
        @Deprecated
        @Override
        public final Stream<FileEntry> stream(final FileEntry entry, final Consumer<? super Problem> onProblem) {
            return super.stream(entry, onProblem);
        }
    }
}
