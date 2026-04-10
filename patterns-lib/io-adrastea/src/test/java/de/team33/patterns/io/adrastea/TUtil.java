package de.team33.patterns.io.adrastea;

import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;

public final class TUtil {

    public static final Comparator<Path> PATH_ORDER = Util.PATH_ORDER;
    public static final LinkOption[] RESOLVE_LINKS = Util.RESOLVE_LINKS;
    public static final LinkOption[] ORIGINAL_LINKS = Util.ORIGINAL_LINKS;
    public static final BasicFileAttributes MISSING_FILE_ATTRIBUTES = Util.MISSING_FILE_ATTRIBUTES;
}
