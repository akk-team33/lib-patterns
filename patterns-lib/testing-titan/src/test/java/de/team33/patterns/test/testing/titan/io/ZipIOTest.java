package de.team33.patterns.test.testing.titan.io;

import de.team33.patterns.testing.titan.io.FileIO;
import de.team33.patterns.testing.titan.io.ZipIO;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ZipIOTest {

    private static final Path TEST_PATH = Paths.get("target", "testing")
                                               .toAbsolutePath()
                                               .normalize();
    private static final Path TARGET_PATH = TEST_PATH.resolve(UUID.randomUUID().toString());
    private static final String RESOURCE_ZIP = "ZipIOTest.zip";
    private static final String RESOURCE_NO_ZIP = "FileIOTest.file";

    @RepeatedTest(2) // must work no matter if already existing or not ...
    final void unzip_rsrc() {
        ZipIO.unzip(ZipIOTest.class, RESOURCE_ZIP, TARGET_PATH);
        assertTrue(Files.isDirectory(TARGET_PATH.resolve("fstool-testing")));
        assertTrue(Files.isDirectory(TARGET_PATH.resolve("fstool-testing")
                                                .resolve("src")));
        assertTrue(Files.isRegularFile(TARGET_PATH.resolve("fstool-testing")
                                                  .resolve("pom.xml")));
    }

    @Test
    final void unzip_rsrc_noZip() {
        final Path targetPath = TEST_PATH.resolve(UUID.randomUUID().toString());
        try {
            ZipIO.unzip(ZipIOTest.class, RESOURCE_NO_ZIP, targetPath);
            fail("should fail - but unzipped to <" + targetPath + ">");
        } catch (final IllegalArgumentException e) {
            // as expected, and ...
            final String message = e.getMessage();
            assertTrue(message.contains(ZipIOTest.class.getCanonicalName()));
            assertTrue(message.contains(RESOURCE_NO_ZIP));
            assertTrue(message.contains(targetPath.toString()));
        }
    }

    @Test
    final void unzip_rsrc_missing() {
        final String missing = UUID.randomUUID().toString();
        final Path targetPath = TEST_PATH.resolve(UUID.randomUUID().toString());
        try {
            ZipIO.unzip(ZipIOTest.class, missing, targetPath);
            fail("should fail - but unzipped to <" + targetPath + ">");
        } catch (final IllegalArgumentException e) {
            // as expected, and ...
            final String message = e.getMessage();
            assertTrue(message.contains(ZipIOTest.class.getCanonicalName()));
            assertTrue(message.contains(missing));
            assertTrue(message.contains(targetPath.toString()));
        }
    }

    @Test
    final void unzip_file() {
        final Path sourcePath = TEST_PATH.resolve(UUID.randomUUID().toString()).resolve(RESOURCE_ZIP);
        FileIO.copy(ZipIOTest.class, RESOURCE_ZIP, sourcePath);
        final Path targetPath = TEST_PATH.resolve(UUID.randomUUID().toString());
        ZipIO.unzip(sourcePath, targetPath);
        assertTrue(Files.isDirectory(targetPath.resolve("fstool-testing")));
        assertTrue(Files.isDirectory(targetPath.resolve("fstool-testing")
                                               .resolve("src")));
        assertTrue(Files.isRegularFile(targetPath.resolve("fstool-testing")
                                                 .resolve("pom.xml")));
    }

    @Test
    final void unzip_file_noZip() {
        final Path sourcePath = TEST_PATH.resolve(UUID.randomUUID().toString()).resolve(RESOURCE_ZIP);
        FileIO.copy(ZipIOTest.class, RESOURCE_NO_ZIP, sourcePath);
        final Path targetPath = TEST_PATH.resolve(UUID.randomUUID().toString());
        try {
            ZipIO.unzip(sourcePath, targetPath);
            fail("should fail - but unzipped to <" + targetPath + ">");
        } catch (final IllegalArgumentException e) {
            // as expected, and ...
            final String message = e.getMessage();
            assertTrue(message.contains(sourcePath.toString()));
            assertTrue(message.contains(targetPath.toString()));
        }
    }
}
