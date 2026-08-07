package de.team33.patterns.files.pluto;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

final class Util {

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

        @Override
        public String toString() {
            return "MISSING_FILE_ATTRIBUTES";
        }
    };

    private Util() {
    }
}
