package de.team33.patterns.io.charon;

import java.nio.file.attribute.BasicFileAttributes;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Symbolizes different file types
 */
public enum FileType {

    /**
     * Symbolizes a missing file.
     */
    MISSING(Objects::isNull),

    /**
     * Symbolizes a directory.
     */
    DIRECTORY(BasicFileAttributes::isDirectory),

    /**
     * Symbolizes a regular file.
     */
    REGULAR(BasicFileAttributes::isRegularFile),

    /**
     * Symbolizes a symbolic link.
     */
    SYMBOLIC(BasicFileAttributes::isSymbolicLink),

    /**
     * Symbolizes a special file.
     */
    SPECIAL(BasicFileAttributes::isOther);

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
