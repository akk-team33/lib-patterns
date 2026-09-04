package de.team33.patterns.files.pluto;

import de.team33.patterns.lazy.narvi.Lazy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.Optional;

import static de.team33.patterns.files.pluto.LinkAttributes.effective;

/**
 * Represents an entry in the file system.
 * <p>
 * Besides its file system path and simple name, a {@code FileEntry} exposes metadata
 * such as its type, size and timestamps.
 * <p>
 * The metadata represents a snapshot taken when the instance is created.
 * Consequently, changes to the underlying file system are not reflected by this instance.
 * Therefore, instances are intended to be short-lived.
 * <p>
 * Use {@link #original(Path)} or {@link #resolved(Path)} to create a new instance.
 */
public class FileEntry {

    private static final LinkOption[] RESOLVE_LINKS = {};
    private static final LinkOption[] ORIGINAL_LINKS = {LinkOption.NOFOLLOW_LINKS};

    private final Path path;
    private final Lazy<BasicFileAttributes> lazyAttributes;
    private final Lazy<FileType> lazyType;

    private FileEntry(final Path path, final LinkOption[] linkOptions) {
        this.path = path.toAbsolutePath().normalize();
        this.lazyAttributes = Lazy.init(() -> newAttributes(linkOptions));
        this.lazyType = Lazy.init(() -> FileType.of(attributes()));
    }

    /**
     * Returns a new {@link FileEntry} for the given {@link Path} exposing the original file attributes.
     *
     * @see #isOriginal()
     */
    public static FileEntry original(final Path path) {
        return new FileEntry(path, ORIGINAL_LINKS);
    }

    /**
     * Returns a new {@link FileEntry} for the given {@link Path} exposing the resolved file attributes.
     *
     * @see #isResolved()
     */
    public static FileEntry resolved(final Path path) {
        return new FileEntry(path, RESOLVE_LINKS);
    }

    private static BasicFileAttributes basicAttributes(final Path path, final LinkOption[] options) {
        try {
            return Files.readAttributes(path, BasicFileAttributes.class, options);
        } catch (final IOException ignored) {
            return Util.MISSING_FILE_ATTRIBUTES;
        }
    }

    /**
     * Returns the {@linkplain Path#toAbsolutePath() absolute} {@linkplain Path#normalize() normalized}
     * {@link Path} of the represented file system entry.
     */
    public final Path path() {
        return path;
    }

    /**
     * Returns the simple name of the represented file system entry.
     */
    public final String name() {
        return Optional.ofNullable(path.getFileName()).orElse(path).toString();
    }

    /**
     * Determines whether <em>this</em> instance exposes the original file attributes,
     * even if the represented entry {@linkplain #isSymbolicLink() is a symbolic link}.
     *
     * @see #isResolved()
     */
    public final boolean isOriginal() {
        if (attributes() instanceof LinkAttributes linkAttributes) {
            return ORIGINAL_LINKS == linkAttributes.options();
        } else {
            return true;
        }
    }

    /**
     * Determines whether <em>this</em> instance exposes the resolved file attributes,
     * even if the represented entry {@linkplain #isSymbolicLink() is a symbolic link}.
     *
     * @see #isOriginal()
     */
    public final boolean isResolved() {
        if (attributes() instanceof LinkAttributes linkAttributes) {
            return RESOLVE_LINKS == linkAttributes.options();
        } else {
            return true;
        }
    }

    /**
     * Returns a {@link FileEntry} representing the same file system entry and exposing the original file attributes.
     * <p>
     * Returns <em>this</em> instance if it already exposes original file attributes.
     *
     * @see #isOriginal()
     */
    public final FileEntry original() {
        return isOriginal() ? this : new FileEntry(path, ORIGINAL_LINKS);
    }

    /**
     * Returns a {@link FileEntry} representing the same file system entry and exposing the resolved file attributes.
     * <p>
     * Returns <em>this</em> instance if it already exposes resolved file attributes.
     *
     * @see #isResolved()
     */
    public final FileEntry resolved() {
        return isResolved() ? this : new FileEntry(path, RESOLVE_LINKS);
    }

    /**
     * Returns the {@link FileType} of the represented file system entry.
     */
    public final FileType type() {
        return lazyType.get();
    }

    /**
     * Determines whether the represented file system entry is a directory.
     * <p>
     * This may also be the case if the represented entry {@linkplain #isSymbolicLink() is a symbolic link}
     * and <em>this</em> instance {@linkplain #isResolved() exposes resolved attributes}.
     */
    public final boolean isDirectory() {
        return attributes().isDirectory();
    }

    /**
     * Determines whether the represented file system entry is a regular file.
     * <p>
     * This may also be the case if the represented entry {@linkplain #isSymbolicLink() is a symbolic link}
     * and <em>this</em> instance {@linkplain #isResolved() exposes resolved attributes}.
     */
    public final boolean isRegularFile() {
        return attributes().isRegularFile();
    }

    /**
     * Determines whether the represented file system entry is a special file (typically a <em>device</em>).
     * <p>
     * This may also be the case if the represented entry {@linkplain #isSymbolicLink() is a symbolic link}
     * and <em>this</em> instance {@linkplain #isResolved() exposes resolved attributes}.
     */
    public final boolean isSpecialFile() {
        return attributes().isOther();
    }

    /**
     * Determines whether the represented file system entry is a symbolic link.
     * <p>
     * NOTE:
     * This is independent of whether <em>this</em> instance {@linkplain #isOriginal() exposes original}
     * or {@linkplain #isResolved() resolved} file attributes.
     */
    public final boolean isSymbolicLink() {
        return attributes().isSymbolicLink();
    }

    /**
     * Determines whether the represented file system entry is missing.
     * <p>
     * This may also be the case if the represented entry {@linkplain #isSymbolicLink() is a symbolic link}
     * and <em>this</em> instance {@linkplain #isResolved() exposes resolved attributes}.
     * <p>
     * NOTE:
     * A resolved symbolic link whose target is missing is still {@linkplain #isPresent() present}!
     */
    public final boolean isMissing() {
        return effective(attributes()) == Util.MISSING_FILE_ATTRIBUTES;
    }

    /**
     * Determines whether the represented file system entry is present.
     * <p>
     * This is always the case if the represented entry is a {@linkplain #isRegularFile() regular file},
     * {@linkplain #isDirectory() directory}, {@linkplain #isSpecialFile() special file},
     * or {@linkplain #isSymbolicLink() symbolic link}.
     * <p>
     * NOTE:
     * A resolved symbolic link whose target {@linkplain #isMissing() is missing} is still present!
     */
    public final boolean isPresent() {
        return attributes() != Util.MISSING_FILE_ATTRIBUTES;
    }

    /**
     * Returns the timestamp of the last modification of the represented file system entry as an {@link Instant}.
     *
     * @throws UnsupportedOperationException if <em>this</em> {@link #isMissing()}
     */
    public final Instant lastModified() {
        return attributes().lastModifiedTime().toInstant();
    }

    /**
     * Returns the timestamp of the last access to the represented file system entry as an {@link Instant}.
     *
     * @throws UnsupportedOperationException if <em>this</em> {@link #isMissing()}.
     */
    public final Instant lastAccess() {
        return attributes().lastAccessTime().toInstant();
    }

    /**
     * Returns the timestamp of the creation of the represented file system entry as an {@link Instant}.
     *
     * @throws UnsupportedOperationException if <em>this</em> {@link #isMissing()}.
     */
    public final Instant creation() {
        return attributes().creationTime().toInstant();
    }

    /**
     * Returns the size of the represented file system entry.
     * <p>
     * Returns {@code 0L} if <em>this</em> {@link #isMissing()}.
     */
    public final long size() {
        return attributes().size();
    }

    @Override
    public final String toString() {
        return path.toString();
    }

    private BasicFileAttributes attributes() {
        return lazyAttributes.get();
    }

    private BasicFileAttributes newAttributes(final LinkOption[] options) {
        final BasicFileAttributes original = basicAttributes(path, ORIGINAL_LINKS);
        if (original.isSymbolicLink()) {
            return newLinkAttributes(options, original);
        } else {
            return original;
        }
    }

    private LinkAttributes newLinkAttributes(final LinkOption[] options, final BasicFileAttributes original) {
        if (ORIGINAL_LINKS == options) {
            return new LinkAttributes(ORIGINAL_LINKS, original);
        } else {
            return new LinkAttributes(options, basicAttributes(path, options));
        }
    }
}
