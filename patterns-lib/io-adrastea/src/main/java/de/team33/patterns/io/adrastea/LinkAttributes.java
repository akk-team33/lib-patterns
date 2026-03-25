package de.team33.patterns.io.adrastea;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

record LinkAttributes(LinkHandling handling, BasicFileAttributes backing) implements BasicFileAttributes {

    @Override
    public final FileTime lastModifiedTime() {
        return backing.lastModifiedTime();
    }

    @Override
    public final FileTime lastAccessTime() {
        return backing.lastAccessTime();
    }

    @Override
    public final FileTime creationTime() {
        return backing.creationTime();
    }

    @Override
    public final boolean isRegularFile() {
        return backing.isRegularFile();
    }

    @Override
    public final boolean isDirectory() {
        return backing.isDirectory();
    }

    @Override
    public final boolean isSymbolicLink() {
        return true;
    }

    @Override
    public final boolean isOther() {
        return backing.isOther();
    }

    @Override
    public final long size() {
        return backing.size();
    }

    @Override
    public final Object fileKey() {
        return backing.fileKey();
    }
}
