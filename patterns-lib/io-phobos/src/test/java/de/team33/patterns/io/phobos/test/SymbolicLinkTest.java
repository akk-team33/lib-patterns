package de.team33.patterns.io.phobos.test;

import de.team33.patterns.io.phobos.FileEntry;
import de.team33.patterns.io.phobos.LinkPolicy;
import de.team33.patterns.io.phobos.FileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SymbolicLinkTest {

    private static final Path BASE_PATH = Paths.get("target", "testing", SymbolicLinkTest.class.getSimpleName())
                                               .toAbsolutePath()
                                               .normalize();
    private static final Path SPECIAL_PATH = Paths.get("/dev/null");

    private Path regLinkPath;
    private Path dirLinkPath;
    private Path specLinkPath;
    private Path linkLinkPath;
    private Path missingLinkPath;

    @BeforeEach
    final void setUp() throws IOException {
        final Path testPath = BASE_PATH.resolve(UUID.randomUUID().toString());
        final Path dirPath = testPath.resolve("directory");
        final Path regularPath = testPath.resolve("regular.file");
        final Path missingPath = testPath.resolve("missing.file");

        regLinkPath = testPath.resolve("regular.link");
        dirLinkPath = testPath.resolve("directory.link");
        specLinkPath = testPath.resolve("special.link");
        linkLinkPath = testPath.resolve("indirect.link");
        missingLinkPath = testPath.resolve("missing.link");

        Files.createDirectories(dirPath);
        Files.write(regularPath, UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));
        Files.createSymbolicLink(regLinkPath, regularPath.getFileName());
        Files.createSymbolicLink(dirLinkPath, dirPath.getFileName());
        Files.createSymbolicLink(missingLinkPath, missingPath.getFileName());
        Files.createSymbolicLink(specLinkPath, SPECIAL_PATH);
        Files.createSymbolicLink(linkLinkPath, regLinkPath.getFileName());
    }

    @Test
    final void linkRegularPrimary() {
        final FileEntry result = FileEntry.of(regLinkPath, LinkPolicy.DISTINCT);
        assertEquals(FileType.SYMBOLIC, result.type());
        assertTrue(result.isSymbolicLink());
        assertFalse(result.isRegularFile());
    }

    @Test
    final void linkRegularEvaluated() {
        final FileEntry result = FileEntry.of(regLinkPath, LinkPolicy.RESOLVED);
        assertEquals(FileType.REGULAR, result.type());
        assertFalse(result.isSymbolicLink());
        assertTrue(result.isRegularFile());
    }

    @Test
    final void linkDirectoryPrimary() {
        final FileEntry result = FileEntry.of(dirLinkPath, LinkPolicy.DISTINCT);
        assertEquals(FileType.SYMBOLIC, result.type());
        assertTrue(result.isSymbolicLink());
        assertFalse(result.isDirectory());
    }

    @Test
    final void linkDirectoryEvaluated() {
        final FileEntry result = FileEntry.of(dirLinkPath, LinkPolicy.RESOLVED);
        assertEquals(FileType.DIRECTORY, result.type());
        assertFalse(result.isSymbolicLink());
        assertTrue(result.isDirectory());
    }

    @Test
    final void linkMissingPrimary() {
        final FileEntry result = FileEntry.of(missingLinkPath, LinkPolicy.DISTINCT);
        assertEquals(FileType.SYMBOLIC, result.type());
        assertTrue(result.isSymbolicLink());
        assertTrue(result.exists());
    }

    @Test
    final void linkMissingEvaluated() {
        final FileEntry result = FileEntry.of(missingLinkPath, LinkPolicy.RESOLVED);
        assertEquals(FileType.MISSING, result.type());
        assertFalse(result.isSymbolicLink());
        assertFalse(result.exists());
    }

    @Test
    final void linkSpecialPrimary() {
        final FileEntry result = FileEntry.of(specLinkPath, LinkPolicy.DISTINCT);
        assertEquals(FileType.SYMBOLIC, result.type());
        assertTrue(result.isSymbolicLink());
        assertFalse(result.isSpecial());
    }

    @Test
    final void linkSpecialEvaluated() {
        final FileEntry result = FileEntry.of(specLinkPath, LinkPolicy.RESOLVED);
        assertEquals(FileType.SPECIAL, result.type());
        assertFalse(result.isSymbolicLink());
        assertTrue(result.isSpecial());
    }

    @Test
    final void linkLinkRegularPrimary() {
        final FileEntry result = FileEntry.of(linkLinkPath, LinkPolicy.DISTINCT);
        assertEquals(FileType.SYMBOLIC, result.type());
        assertTrue(result.isSymbolicLink());
        assertFalse(result.isRegularFile());
    }

    @Test
    final void linkLinkRegularEvaluated() {
        final FileEntry result = FileEntry.of(linkLinkPath, LinkPolicy.RESOLVED);
        assertEquals(FileType.REGULAR, result.type());
        assertFalse(result.isSymbolicLink());
        assertTrue(result.isRegularFile());
    }
}
