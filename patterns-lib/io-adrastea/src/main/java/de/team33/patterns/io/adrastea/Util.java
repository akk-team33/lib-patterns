package de.team33.patterns.io.adrastea;

import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Comparator;

final class Util {

    static final Comparator<Object> NO_ORDER = ((o1, o2) -> {
        throw new UnsupportedOperationException("This method should not actually be called!");
    });
    static final LinkOption[] RESOLVE_LINKS = {};
    static final LinkOption[] DISCLOSE_LINKS = {LinkOption.NOFOLLOW_LINKS};
    static final BasicFileAttributes MISSING_FILE_ATTRIBUTES = new BasicFileAttributes() {

        private FileTime missingTime() {
            throw new UnsupportedOperationException("File is missing - timestamp not available!");
        }

        @Override
        public FileTime lastModifiedTime() {
            return missingTime();
        }

        @Override
        public FileTime lastAccessTime() {
            return missingTime();
        }

        @Override
        public FileTime creationTime() {
            return missingTime();
        }

        @Override
        public boolean isRegularFile() {
            return false;
        }

        @Override
        public boolean isDirectory() {
            return false;
        }

        @Override
        public boolean isSymbolicLink() {
            return false;
        }

        @Override
        public boolean isOther() {
            return false;
        }

        @Override
        public long size() {
            return 0;
        }

        @Override
        public Object fileKey() {
            throw new UnsupportedOperationException("File is missing - file key not available!");
        }
    };
    private static final Comparator<String> IGNORE_CASE = String::compareToIgnoreCase;
    private static final Comparator<String> RESPECT_CASE = String::compareTo;
    private static final Comparator<String> STRING_ORDER = IGNORE_CASE.thenComparing(RESPECT_CASE);
    private static final Comparator<Path> NAME_ORDER = Comparator.comparing(Path::toString, STRING_ORDER);
    static final Comparator<Path> PATH_ORDER = Comparator.comparing(Path::getFileName, NAME_ORDER);

    private Util() {
    }
}
