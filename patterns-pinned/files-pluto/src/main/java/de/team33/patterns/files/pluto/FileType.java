package de.team33.patterns.files.pluto;

import de.team33.patterns.enums.pan.Values;

import java.nio.file.attribute.BasicFileAttributes;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

/**
 * Identifies the type of file system entry represented by a {@link FileEntry}.
 */
public enum FileType {

    /**
     * Identifies a regular file.
     * <p>
     * This also applies to a symbolic link if the associated {@link FileEntry}
     * is not {@linkplain FileEntry#isOriginal() original}.
     */
    REGULAR_FILE(BasicFileAttributes::isRegularFile),

    /**
     * Identifies a directory.
     * <p>
     * This also applies to a symbolic link if the associated {@link FileEntry}
     * is not {@linkplain FileEntry#isOriginal() original}.
     */
    DIRECTORY(BasicFileAttributes::isDirectory),

    /**
     * Identifies a special file (typically a <em>device</em>).
     * <p>
     * This also applies to a symbolic link if the associated {@link FileEntry}
     * is not {@linkplain FileEntry#isOriginal() original}.
     */
    SPECIAL_FILE(BasicFileAttributes::isOther),

    /**
     * Identifies a symbolic link if the associated {@link FileEntry}
     * is not {@linkplain FileEntry#isResolved() resolved}.
     */
    SYMBOLIC_LINK(attributes -> LinkAttributes.effective(attributes).isSymbolicLink()),

    /**
     * Identifies a missing file.
     * <p>
     * This also applies to a symbolic link if the associated {@link FileEntry}
     * is not {@linkplain FileEntry#isOriginal() original}.
     */
    MISSING(attributes -> LinkAttributes.effective(attributes) == Util.MISSING_FILE_ATTRIBUTES);

    private static final Values<FileType> VALUES = Values.of(FileType.class);
    private static final String UNKNOWN_TYPE = "Unsupported file attributes: <%s>";

    private final Predicate<BasicFileAttributes> predicate;

    FileType(final Predicate<BasicFileAttributes> predicate) {
        this.predicate = predicate;
    }

    static FileType of(final BasicFileAttributes attributes) {
        return VALUES.findFirst(type -> type.predicate.test(attributes))
                     .orElseThrow(() -> new NoSuchElementException(UNKNOWN_TYPE.formatted(attributes)));
    }
}
