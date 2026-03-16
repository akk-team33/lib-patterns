package de.team33.patterns.io.adrastea.publics;

import de.team33.patterns.io.adrastea.TUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NewClassNamingConvention")
class SymbolicLinkTrial {

    private static final Path BASE_PATH = Paths.get("target", "testing", SymbolicLinkTrial.class.getSimpleName())
                                               .toAbsolutePath()
                                               .normalize();

    private Path regLinkPath;

    @BeforeEach
    final void setUp() throws IOException {
        final Path testPath = BASE_PATH.resolve(UUID.randomUUID().toString());
        final Path dirPath = testPath.resolve("directory");
        final Path regularPath = testPath.resolve("regular.file");

        regLinkPath = testPath.resolve("regular.link");

        Files.createDirectories(dirPath);
        Files.writeString(regularPath, UUID.randomUUID().toString());
        Files.createSymbolicLink(regLinkPath, regularPath.getFileName());
    }

    @Test
    final void linkRegular() throws IOException {
        final Path path = regLinkPath;
        final Path realPath = regLinkPath.toRealPath();
        final BasicFileAttributes disclosed =
                Files.readAttributes(path, BasicFileAttributes.class, TUtil.DISCLOSE_LINKS);
        final BasicFileAttributes resolved =
                Files.readAttributes(path, BasicFileAttributes.class, TUtil.RESOLVE_LINKS);
        final BasicFileAttributes real =
                Files.readAttributes(realPath, BasicFileAttributes.class, TUtil.DISCLOSE_LINKS);

        assertTrue(resolved.isRegularFile());
        assertFalse(resolved.isSymbolicLink());

        assertFalse(disclosed.isRegularFile());
        assertTrue(disclosed.isSymbolicLink());

        assertTrue(real.isRegularFile());
        assertFalse(real.isSymbolicLink());

        assertNotEquals(disclosed.lastModifiedTime(), real.lastModifiedTime());
        assertNotEquals(disclosed.size(), real.size());

        assertEquals(resolved.lastModifiedTime(), real.lastModifiedTime());
        assertEquals(resolved.size(), real.size());
    }
}