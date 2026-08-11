package de.team33.patterns.io.phobos;

import de.team33.patterns.enums.pan.Values;

import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/files-pluto/">files-pluto</a>
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/files-pluto/apidocs/">files-pluto/apidocs</a>
 * @deprecated consider class FileType from module
 * <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/files-pluto/apidocs/">files-pluto</a>
 * as a replacement.
 */
@Deprecated
public enum FileType {

    /**
     * @deprecated see {@link FileType}.
     */
    @Deprecated
    MISSING(Objects::isNull),

    /**
     * @deprecated see {@link FileType}.
     */
    @Deprecated
    REGULAR(BasicFileAttributes::isRegularFile),

    /**
     * @deprecated see {@link FileType}.
     */
    @Deprecated
    DIRECTORY(BasicFileAttributes::isDirectory),

    /**
     * @deprecated see {@link FileType}.
     */
    @Deprecated
    SYMBOLIC(BasicFileAttributes::isSymbolicLink),

    /**
     * @deprecated see {@link FileType}.
     */
    @Deprecated
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
