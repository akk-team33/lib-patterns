package de.team33.patterns.io.phobos;

import java.nio.file.LinkOption;

/**
 * Specifies how to handle symbolic links.
 */
public enum LinkPolicy {

    /**
     * Specifies that symbolic links should be resolved.
     */
    RESOLVED(),

    /**
     * Specifies that symbolic links should be treated independently.
     */
    DISTINCT(LinkOption.NOFOLLOW_LINKS);

    private final LinkOption[] linkOptions;

    LinkPolicy(final LinkOption... linkOptions) {
        this.linkOptions = linkOptions;
    }

    final LinkOption[] linkOptions() {
        return linkOptions;
    }
}
