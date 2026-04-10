package de.team33.patterns.io.adrastea;

import java.nio.file.LinkOption;

/**
 * Controls the handling of symbolic links.
 */
public enum LinkHandling {

    /**
     * Handles symbolic links including all their original attributes.
     */
    ORIGINAL(Util.ORIGINAL_LINKS),

    /**
     * Resolves symbolic links including all their resolved attributes.
     */
    RESOLVE(Util.RESOLVE_LINKS);

    private final LinkOption[] options;

    LinkHandling(final LinkOption[] options) {
        this.options = options;
    }

    final LinkOption[] options() {
        return options;
    }
}
