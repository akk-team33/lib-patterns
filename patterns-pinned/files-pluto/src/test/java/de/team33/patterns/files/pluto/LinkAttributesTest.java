package de.team33.patterns.files.pluto;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import static org.junit.jupiter.api.Assertions.assertSame;

class LinkAttributesTest {

    private static final LinkOption[] RESOLVE = {};

    @Test
    final void fileKey() throws IOException {
        final BasicFileAttributes attributes = Files.readAttributes(Path.of("src"), BasicFileAttributes.class);
        final Object expected = attributes.fileKey();
        final Object result = new LinkAttributes(RESOLVE, attributes).fileKey();
        assertSame(expected, result);
    }
}