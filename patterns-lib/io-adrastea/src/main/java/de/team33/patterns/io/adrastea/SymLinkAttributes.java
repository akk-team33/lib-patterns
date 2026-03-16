package de.team33.patterns.io.adrastea;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

record SymLinkAttributes(BasicFileAttributes disclosed,
                         BasicFileAttributes resolved) implements BasicFileAttributes {

    @Override
    public FileTime lastModifiedTime() {
        return resolved.lastModifiedTime();
    }

    @Override
    public FileTime lastAccessTime() {
        return disclosed.lastAccessTime();
    }

    @Override
    public FileTime creationTime() {
        return disclosed.creationTime();
    }

    @Override
    public boolean isRegularFile() {
        return resolved.isRegularFile();
    }

    @Override
    public boolean isDirectory() {
        return resolved.isDirectory();
    }

    @Override
    public boolean isSymbolicLink() {
        return disclosed.isSymbolicLink();
    }

    @Override
    public boolean isOther() {
        return resolved.isOther();
    }

    @Override
    public long size() {
        return resolved.size();
    }

    @Override
    public Object fileKey() {
        return disclosed.fileKey();
    }
}
