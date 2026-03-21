package de.team33.patterns.io.adrastea;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import static org.junit.jupiter.api.Assertions.assertSame;

class LinkAttributesTest {

    @Test
    final void fileKey() throws IOException {
        final BasicFileAttributes attributes = Files.readAttributes(Path.of("src"), BasicFileAttributes.class);
        final Object expected = attributes.fileKey();
        final Object result = new LinkAttributes(LinkHandling.RESOLVE, attributes).fileKey();
        assertSame(expected, result);
    }
}