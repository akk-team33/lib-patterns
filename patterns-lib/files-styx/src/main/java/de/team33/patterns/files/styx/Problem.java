package de.team33.patterns.files.styx;

import de.team33.patterns.files.pluto.FileEntry;
import de.team33.patterns.lazy.narvi.Lazy;

import java.io.IOException;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.WARNING;

/**
 * Represents a problem encountered while accessing a file-system entry
 * during a {@link Styx} traversal.
 *
 * @param entry the file-system entry that could not be accessed
 * @param cause the exception describing the access problem
 */
public record Problem(FileEntry entry, IOException cause) {

    private static final System.Logger LOGGER = System.getLogger(Problem.class.getCanonicalName());
    private static final String MESSAGE = "Cannot access file entry ...%n" +
                                          "    path:      <%s>%n" +
                                          "    exception: <%s>%n" +
                                          "    message:   '%s'%n";

    final void log() {
        final Lazy<String> lazyMessage = Lazy.init(() -> MESSAGE.formatted(entry.path(),
                                                                           cause.getClass().getCanonicalName(),
                                                                           cause.getMessage()));
        LOGGER.log(WARNING, lazyMessage);
        LOGGER.log(DEBUG, lazyMessage, cause());
    }
}
