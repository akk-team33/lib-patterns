package de.team33.patterns.hierarchy.mab.testing;

import de.team33.patterns.decision.thyone.Choices;
import de.team33.patterns.lazy.narvi.Lazy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.lang.System.Logger.Level.WARNING;

/**
 * A utility class for handling directories, especially their content.
 */
public final class Directory {

    private static final System.Logger LOGGER = System.getLogger(Directory.class.getCanonicalName());

    private Directory() {
    }

    private static void logProblem(final Problem problem) {
        LOGGER.log(WARNING,
                   () -> "Cannot read directory ...%n    path: %s%n".formatted(problem.path),
                   problem.cause);
    }

    /**
     * Returns a new {@link Mapper} that uses the given <em>mapping</em>.
     *
     * @param <R> The result type of the resulting {@link Mapper}.
     */
    public static <R> Mapper<R> mapper(final Function<? super Stream<Path>, ? extends R> mapping) {
        return new Mapper<>(mapping::apply, Directory::logProblem);
    }

    public static <E> Lister<E> lister(final Function<? super Path, ? extends E> mapping) {
        return new Lister<>(Util.PATH_ORDER, mapping::apply, Util.NO_ORDER, Directory::logProblem);
    }

    /**
     * Represents a potential directory.
     */
    @FunctionalInterface
    public interface Item {

        /**
         * The {@link Path} that identifies <em>this</em> {@link Item}.
         */
        Path path();

        /**
         * Determines if <em>this</em> {@link Item} is actually a directory.
         * <p>
         * The default implementation uses {@link Files#isDirectory(Path, LinkOption...)}
         * resolving a symbolic link, if any.
         * <p>
         * If possible, this method should be overridden with a more efficient implementation.
         */
        default boolean isDirectory() {
            return Files.isDirectory(path(), Util.RESOLVE_LINKS);
        }
    }

    /**
     * Represents a tool that serves to encapsulate the method {@link Files#list(Path)} by mapping the resulting
     * {@link Stream} into a stable result of type {@code <R>} and then closing the {@link Stream}.
     * <p>
     * Files that are not directories can be processed without problems and are treated like empty directories.
     * <p>
     * Directories that cannot be read, for example, due to insufficient permissions,
     * are also treated as empty directories after the problem has been reported to an associated listener.
     * <p>
     * The default listener will simply log any problems using a {@link System.Logger}.
     * Use {@link Mapper#onProblem(Consumer)} to specify an alternative listener.
     * <p>
     * Use {@link Directory#mapper(Function)} to get an (initial) instance.
     *
     * @param <R> The type of stable result.
     */
    public static final class Mapper<R> {

        private final Function<Stream<Path>, R> mapping;
        private final Consumer<Problem> onProblem;

        private Mapper(final Function<Stream<Path>, R> mapping, final Consumer<Problem> onProblem) {
            this.mapping = mapping;
            this.onProblem = onProblem;
        }

        /**
         * Streams the contents of a (presumed) directory that is specified by a given {@link Item} and
         * uses the initially associated <em>mapping</em> to produce a stable result.
         * The stream itself will be closed and thus terminated within this method.
         */
        public final R map(final Item item) {
            if (item.isDirectory()) {
                try (final Stream<Path> paths = Files.list(item.path())) {
                    return mapping.apply(paths);
                } catch (final IOException caught) {
                    onProblem.accept(new Problem(item.path(), caught));
                }
            }
            return mapping.apply(Stream.empty());
        }

        /**
         * Returns a new {@link Mapper} that uses <em>this</em>' <em>mapping</em> but an alternative
         * <em>listener</em> when a problem occurs while reading a directory.
         */
        public final Mapper<R> onProblem(final Consumer<? super Problem> listener) {
            return new Mapper<>(mapping, listener::accept);
        }
    }

    public static class Lister<E> {

        private static final Choices<Lister<?>> CHOICES = Choices.parallel(Lister::isPathOrder, Lister::isEOrder);

        private final Comparator<? super Path> pathOrder;
        private final Function<? super Path, E> mapping;
        private final Comparator<? super E> eOrder;
        private final Consumer<? super Problem> onProblem;
        private final Lazy<Mapper<List<E>>> mapper;

        private Lister(final Comparator<? super Path> pathOrder,
                       final Function<? super Path, E> mapping,
                       final Comparator<? super E> eOrder,
                       final Consumer<? super Problem> onProblem) {
            this.pathOrder = pathOrder;
            this.mapping = mapping;
            this.eOrder = eOrder;
            this.onProblem = onProblem;
            this.mapper = Lazy.init(this::newMapper);
        }

        private Mapper<List<E>> newMapper() {
            return Directory.mapper(newListing()).onProblem(onProblem);
        }

        private Function<Stream<Path>, List<E>> newListing() {
            return switch (CHOICES.apply(this)) {
                case 0b11 -> paths -> paths.sorted(pathOrder).map(mapping).sorted(eOrder).toList();
                case 0b10 -> paths -> paths.sorted(pathOrder).map(mapping).toList();
                case 0b01 -> paths -> paths.map(mapping).sorted(eOrder).toList();
                // case 0b00 ...
                default -> paths -> paths.map(mapping).toList();
            };
        }

        private boolean isPathOrder() {
            return Util.NO_ORDER != pathOrder;
        }

        private boolean isEOrder() {
            return Util.NO_ORDER != eOrder;
        }

        public final List<E> list(final Item item) {
            return mapper.get().map(item);
        }
    }

    /**
     * Represents a problem that may occur while accessing a directory.
     *
     * @param path  The {@link Path} that represents the directory in question.
     * @param cause An {@link IOException} that is caught while accessing the directory in question.
     */
    public record Problem(Path path, IOException cause) {
    }
}
