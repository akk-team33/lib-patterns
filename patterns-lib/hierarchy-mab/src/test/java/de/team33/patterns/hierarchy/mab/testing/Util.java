package de.team33.patterns.hierarchy.mab.testing;

import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Comparator;

final class Util {

    static final LinkOption[] RESOLVE_LINKS = {};
    static final Comparator<Object> NO_ORDER = ((o1, o2) -> {
        throw new UnsupportedOperationException("This method should not actually be called!");
    });
    private static final Comparator<String> IGNORE_CASE = String::compareToIgnoreCase;
    private static final Comparator<String> RESPECT_CASE = String::compareTo;
    private static final Comparator<String> STRING_ORDER = IGNORE_CASE.thenComparing(RESPECT_CASE);
    private static final Comparator<Path> NAME_ORDER = Comparator.comparing(Path::toString, STRING_ORDER);
    static final Comparator<Path> PATH_ORDER = Comparator.comparing(Path::getFileName, NAME_ORDER);

    private Util() {
    }
}
