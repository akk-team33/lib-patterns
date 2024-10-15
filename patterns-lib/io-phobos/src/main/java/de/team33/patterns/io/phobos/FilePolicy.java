package de.team33.patterns.io.phobos;

import java.nio.file.LinkOption;

public enum FilePolicy {

    RESOLVE_SYMLINKS(),
    DISTINCT_SYMLINKS(LinkOption.NOFOLLOW_LINKS);

    private final LinkOption[] linkOptions;

    FilePolicy(final LinkOption... linkOptions) {
        this.linkOptions = linkOptions;
    }

    final LinkOption[] linkOptions() {
        return linkOptions;
    }
}
