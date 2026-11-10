package de.team33.patterns.files.pluto;

import java.nio.file.LinkOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

record LinkAttributes(LinkOption[] options, BasicFileAttributes backing) implements BasicFileAttributes {

    static BasicFileAttributes effective(final BasicFileAttributes attributes) {
        return (attributes instanceof LinkAttributes linkAttributes) ? linkAttributes.backing() : attributes;
    }

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
