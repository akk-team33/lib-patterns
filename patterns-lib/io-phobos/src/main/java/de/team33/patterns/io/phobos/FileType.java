package de.team33.patterns.io.phobos;

import de.team33.patterns.enums.pan.Values;

import java.nio.file.attribute.BasicFileAttributes;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Symbolizes different file types
 */
public enum FileType {

    /**
     * Symbolizes a missing file.
     */
    MISSING(Objects::isNull),

    /**
     * Symbolizes a regular file.
     */
    REGULAR(BasicFileAttributes::isRegularFile),

    /**
     * Symbolizes a directory.
     */
    DIRECTORY(BasicFileAttributes::isDirectory),

    /**
     * Symbolizes a symbolic link.
     */
    SYMBOLIC(BasicFileAttributes::isSymbolicLink),

    /**
     * Symbolizes a special file.
     */
    SPECIAL(BasicFileAttributes::isOther);

    private static final Values<FileType> VALUES = Values.of(FileType.class);

    private final Predicate<BasicFileAttributes> filter;

    FileType(final Predicate<BasicFileAttributes> filter) {
        this.filter = filter;
    }

    static FileType map(final BasicFileAttributes attributes) {
        return VALUES.findAny(fileType -> fileType.filter.test(attributes))
                     .orElse(MISSING);
    }
}
