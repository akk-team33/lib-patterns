package de.team33.patterns.hierarchy.mab.testing;

import de.team33.patterns.hierarchy.mab.Hierarchy;

import java.nio.file.Path;
import java.util.List;

public record FileEntry(Path path) implements Directory.Item, Hierarchy.Item<FileEntry> {

    private static final Directory.Lister<FileEntry> LISTER = Directory.lister(FileEntry::new);

    @Override
    public final List<FileEntry> list() {
        return LISTER.list(this);
    }
}
