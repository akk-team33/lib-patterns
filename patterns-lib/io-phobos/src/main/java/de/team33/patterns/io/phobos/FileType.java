package de.team33.patterns.io.phobos;

import java.nio.file.attribute.BasicFileAttributes;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

public enum FileType {

    MISSING(Objects::isNull),
    DIRECTORY(BasicFileAttributes::isDirectory),
    REGULAR(BasicFileAttributes::isRegularFile),
    SYMBOLIC(BasicFileAttributes::isSymbolicLink),
    OTHER(BasicFileAttributes::isOther);

    private final Predicate<BasicFileAttributes> filter;

    FileType(final Predicate<BasicFileAttributes> filter) {
        this.filter = filter;
    }

    static FileType map(final BasicFileAttributes attributes) {
        return Stream.of(values())
                     .filter(fileType -> fileType.filter.test(attributes))
                     .findAny()
                     .orElseThrow(() -> new NoSuchElementException("failed: " + attributes));
    }
}
