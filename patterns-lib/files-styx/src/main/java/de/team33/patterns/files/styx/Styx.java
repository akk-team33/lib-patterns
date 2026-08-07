package de.team33.patterns.files.styx;

import de.team33.patterns.files.pluto.FileEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;

public final class Styx {

    private static final Comparator<String> IGNORE_CASE = String::compareToIgnoreCase;
    private static final Comparator<String> RESPECT_CASE = String::compareToIgnoreCase;
    private static final Comparator<String> STRING_ORDER = IGNORE_CASE.thenComparing(RESPECT_CASE);
    private static final Comparator<FileEntry> ENTRY_ORDER = Comparator.comparing(FileEntry::name, STRING_ORDER);
    private static final Supplier<TreeSet<FileEntry>> NEW_TREE_SET = () -> new TreeSet<>(ENTRY_ORDER);

    private final int start;
    private final int limit;
    private final Options options;

    private Styx(final int start, final int limit, final Options options) {
        this.start = start;
        this.limit = limit;
        this.options = options;
    }

    public static Stream<FileEntry> stream(final FileEntry head) {
        return stream(head, Options.DEFAULT);
    }

    public static Stream<FileEntry> stream(final FileEntry head, final Options options) {
        return new Styx(0, Integer.MAX_VALUE, options).stream(0, head);
    }

    public static Stream<FileEntry> descendants(final FileEntry head) {
        return descendants(head, Options.DEFAULT);
    }

    public static Stream<FileEntry> descendants(final FileEntry head, final Options options) {
        return new Styx(1, Integer.MAX_VALUE, options).stream(0, head);
    }

    public static Stream<FileEntry> children(final FileEntry head) {
        return children(head, Options.DEFAULT);
    }

    public static Stream<FileEntry> children(final FileEntry head, final Options options) {
        return new Styx(1, 2, options).stream(0, head);
    }

    public static Streamer streamer(final Options options) {
        return new Streamer(options);
    }

    private Stream<FileEntry> stream(final int level, final FileEntry entry) {
        if (level < limit) {
            final Stream<FileEntry> head = (level < start) ? Stream.empty() : Stream.of(entry);
            return options.skipCondition.test(entry) ? head : stream(level, head, list(entry));
        } else {
            return Stream.empty();
        }
    }

    private Stream<FileEntry> stream(final int level, final Stream<FileEntry> head, final List<FileEntry> tail) {
        return tail.isEmpty() ? head : Stream.concat(head, tail.stream()
                                                               .flatMap(entry -> stream(level + 1, entry)));
    }

    private List<FileEntry> list(final FileEntry entry) {
        if (entry.isDirectory()) {
            try (final Stream<Path> paths = Files.list(entry.path())) {
                final TreeSet<FileEntry> sorted = paths.map(options.toEntry)
                                                       .collect(toCollection(NEW_TREE_SET));
                return List.copyOf(sorted);
            } catch (final IOException caught) {
                options.onProblem.accept(new Problem(entry, caught));
            }
        }
        return List.of();
    }

    public static final class Options {

        public static final Options DEFAULT = new Options(FileEntry::original, Predicates.reject(), Problem::log);
        public static final Options RESOLVE = new Options(FileEntry::resolved, Predicates.reject(), Problem::log);

        private final Function<Path, FileEntry> toEntry;
        private final Predicate<FileEntry> skipCondition;
        private final Consumer<Problem> onProblem;

        private Options(final Function<Path, FileEntry> toEntry,
                        final Predicate<FileEntry> skipCondition,
                        final Consumer<Problem> onProblem) {
            this.toEntry = toEntry;
            this.skipCondition = skipCondition;
            this.onProblem = onProblem;
        }

        public final Options skip(final Predicate<? super FileEntry> condition) {
            return new Options(toEntry, skipCondition.or(condition), onProblem);
        }

        public final Options onProblem(final Consumer<? super Problem> consumer) {
            return new Options(toEntry, skipCondition, consumer::accept);
        }
    }

    public static final class Streamer {

        private final Options options;

        private Streamer(final Options options) {
            this.options = options;
        }

        public final Stream<FileEntry> stream(final FileEntry head) {
            return Styx.stream(head, options);
        }

        public final Stream<FileEntry> descendants(final FileEntry head) {
            return Styx.descendants(head, options);
        }

        public final Stream<FileEntry> children(final FileEntry head) {
            return Styx.children(head, options);
        }
    }
}
