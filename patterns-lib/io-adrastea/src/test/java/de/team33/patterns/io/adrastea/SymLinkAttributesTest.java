package de.team33.patterns.io.adrastea;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SymLinkAttributesTest {

    @Test
    void fileKey() throws IOException {
        final BasicFileAttributes disclosed =
                Files.readAttributes(Path.of("pom.xml"), BasicFileAttributes.class, TUtil.DISCLOSE_LINKS);
        final BasicFileAttributes resolved =
                Files.readAttributes(Path.of("src"), BasicFileAttributes.class, TUtil.RESOLVE_LINKS);
        final var symLinkAttributes = new SymLinkAttributes(disclosed, resolved);
        assertEquals(disclosed.fileKey(), symLinkAttributes.fileKey());
    }
}