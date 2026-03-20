package de.team33.patterns.io.adrastea;

import java.nio.file.LinkOption;

/**
 * Controls the handling of symbolic links.
 */
public enum LinkHandling {

    /**
     * Discloses symbolic links.
     */
    DISCLOSE(Util.DISCLOSE_LINKS),

    /**
     * Resolves symbolic links.
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
