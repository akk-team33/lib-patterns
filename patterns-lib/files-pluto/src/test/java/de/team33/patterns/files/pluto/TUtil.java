package de.team33.patterns.files.pluto;

import java.nio.file.LinkOption;
import java.nio.file.attribute.BasicFileAttributes;

public class TUtil {

    public static final BasicFileAttributes MISSING_FILE_ATTRIBUTES = Util.MISSING_FILE_ATTRIBUTES;
    public static final LinkOption[] RESOLVE_LINKS = {};
    public static final LinkOption[] ORIGINAL_LINKS = {LinkOption.NOFOLLOW_LINKS};
}
