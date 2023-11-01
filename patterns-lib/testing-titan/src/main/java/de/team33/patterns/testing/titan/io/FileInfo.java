package de.team33.patterns.testing.titan.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileInfo {

    private final String name;
    private final Type type;
    private final long size;
    private final Instant lastModified;
    private final List<FileInfo> content;

    private FileInfo(final Path path, final LinkOption[] options) {
        final BasicFileAttributes attributes = basicFileAttributes(path, options);
        this.type = Type.of(attributes);
        this.name = path.getFileName().toString();
        this.size = (null != attributes) ? attributes.size() : 0L;
        this.lastModified = (null != attributes) ? attributes.lastModifiedTime().toInstant() : null;
        this.content = ((null != attributes) && attributes.isDirectory())
                       ? contentOf(path, options)
                       : Collections.emptyList();
    }

    private List<FileInfo> contentOf(final Path path, LinkOption[] options) {
        try (final Stream<Path> stream = Files.list(path)) {
            return stream.map(item -> new FileInfo(item, options))
                         .collect(Collectors.toList());
        } catch (final IOException e) {
            return Collections.emptyList();
        }
    }

    private static BasicFileAttributes basicFileAttributes(final Path path, final LinkOption[] options) {
        try {
            return Files.readAttributes(path, BasicFileAttributes.class, options);
        } catch (final IOException e) {
            return null;
        }
    }

    public static FileInfo of(final Path path, final LinkOption... options) {
        return new FileInfo(path, options);
    }

    private static String noDetail(final Long size, final Instant lastModified) {
        return "";
    }

    private static String regularDetail(final Long size, final Instant lastModified) {
        return String.format(" (%,d, %s)", size, lastModified);
    }

    private static String noTail(final Integer indent, final List<FileInfo> content) {
        return "";
    }

    private static String dirTail(Integer indent, List<FileInfo> content) {
        if (content.isEmpty()) {
            return " {}";
        } else {
            return String.format(" {%s%s}", dirTailBody(content, indent + 1), newLine(indent));
        }
    }

    private static String dirTailBody(final List<FileInfo> content, final int indent) {
        final String newLine = newLine(indent);
        return content.stream()
                      .map(fi -> fi.toString(indent))
                      .collect(Collectors.joining(newLine, newLine, ""));
    }

    private static String newLine(final int indent) {
        return String.format("%n%s", Stream.generate(() -> "    ")
                                           .limit(indent)
                                           .collect(Collectors.joining()));
    }

    @Override
    public final String toString() {
        return toString(0);
    }

    public final String toString(final int indent) {
        return String.format("%s : %s%s%s;",
                             name, type, type.details.apply(size, lastModified), type.toTail.apply(indent, content));
    }

    public enum Type {

        REGULAR(BasicFileAttributes::isRegularFile, FileInfo::regularDetail, FileInfo::noTail),
        DIRECTORY(BasicFileAttributes::isDirectory, FileInfo::noDetail, FileInfo::dirTail),
        SYMLINK(BasicFileAttributes::isSymbolicLink, FileInfo::noDetail, FileInfo::noTail),
        OTHER(Objects::nonNull, FileInfo::noDetail, FileInfo::noTail),
        MISSING(Objects::isNull, FileInfo::noDetail, FileInfo::noTail);

        private final Predicate<BasicFileAttributes> filter;
        private final BiFunction<Long, Instant, String> details;
        private final BiFunction<Integer, List<FileInfo>, String> toTail;

        Type(final Predicate<BasicFileAttributes> filter,
             final BiFunction<Long, Instant, String> details,
             final BiFunction<Integer, List<FileInfo>, String> toTail) {
            this.filter = filter;
            this.details = details;
            this.toTail = toTail;
        }

        static Type of(final BasicFileAttributes entry) {
            return Stream.of(values())
                         .filter(type -> (null != entry) && type.filter.test(entry))
                         .findAny()
                         .orElse(MISSING);
        }
    }
}
