package de.team33.patterns.io.phobos.test;

import de.team33.patterns.io.phobos.FileEntry;
import de.team33.patterns.io.phobos.FileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
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

    @BeforeEach
    final void setUp() throws IOException {
        final Path testPath = BASE_PATH.resolve(UUID.randomUUID().toString());
        final Path dirPath = testPath.resolve("directory");
        final Path regularPath = testPath.resolve("regular.file");

        regLinkPath = testPath.resolve("regular.link");
        dirLinkPath = testPath.resolve("directory.link");
        specLinkPath = testPath.resolve("special.link");
        linkLinkPath = testPath.resolve("indirect.link");

        Files.createDirectories(dirPath);
        Files.write(regularPath, UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));
        Files.createSymbolicLink(regLinkPath, regularPath.getFileName());
        Files.createSymbolicLink(dirLinkPath, dirPath.getFileName());
        Files.createSymbolicLink(specLinkPath, SPECIAL_PATH);
        Files.createSymbolicLink(linkLinkPath, regLinkPath.getFileName());
    }

    @Test
    final void linkRegularPrimary() {
        final FileEntry result = FileEntry.of(regLinkPath, LinkOption.NOFOLLOW_LINKS);
        assertEquals(FileType.SYMBOLIC, result.type());
        assertTrue(result.isSymbolicLink());
        assertFalse(result.isRegularFile());
    }

    @Test
    final void linkRegularEvaluated() {
        final FileEntry result = FileEntry.of(regLinkPath);
        assertEquals(FileType.REGULAR, result.type());
        assertFalse(result.isSymbolicLink());
        assertTrue(result.isRegularFile());
    }

    @Test
    final void linkDirectoryPrimary() {
        final FileEntry result = FileEntry.of(dirLinkPath, LinkOption.NOFOLLOW_LINKS);
        assertEquals(FileType.SYMBOLIC, result.type());
        assertTrue(result.isSymbolicLink());
        assertFalse(result.isDirectory());
    }

    @Test
    final void linkDirectoryEvaluated() {
        final FileEntry result = FileEntry.of(dirLinkPath);
        assertEquals(FileType.DIRECTORY, result.type());
        assertFalse(result.isSymbolicLink());
        assertTrue(result.isDirectory());
    }

    @Test
    final void linkSpecialPrimary() {
        final FileEntry result = FileEntry.of(specLinkPath, LinkOption.NOFOLLOW_LINKS);
        assertEquals(FileType.SYMBOLIC, result.type());
        assertTrue(result.isSymbolicLink());
        assertFalse(result.isOther());
    }

    @Test
    final void linkSpecialEvaluated() {
        final FileEntry result = FileEntry.of(specLinkPath);
        assertEquals(FileType.OTHER, result.type());
        assertFalse(result.isSymbolicLink());
        assertTrue(result.isOther());
    }

    @Test
    final void linkLinkRegularPrimary() {
        final FileEntry result = FileEntry.of(linkLinkPath, LinkOption.NOFOLLOW_LINKS);
        assertEquals(FileType.SYMBOLIC, result.type());
        assertTrue(result.isSymbolicLink());
        assertFalse(result.isRegularFile());
    }

    @Test
    final void linkLinkRegularEvaluated() {
        final FileEntry result = FileEntry.of(linkLinkPath);
        assertEquals(FileType.REGULAR, result.type());
        assertFalse(result.isSymbolicLink());
        assertTrue(result.isRegularFile());
    }
}
