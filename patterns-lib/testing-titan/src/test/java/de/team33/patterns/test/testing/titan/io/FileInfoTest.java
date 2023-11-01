package de.team33.patterns.test.testing.titan.io;

import de.team33.patterns.testing.titan.io.FileIO;
import de.team33.patterns.testing.titan.io.FileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileInfoTest {

    private static final Class<FileInfoTest> CLASS = FileInfoTest.class;
    private static final Path TEST_PATH = Paths.get("target", "testing", CLASS.getSimpleName())
                                               .toAbsolutePath()
                                               .normalize();
    private static final Path REGULAR_PATH = TEST_PATH.resolve("regular.file");

    @BeforeAll
    static void init() throws IOException {
        Files.createDirectories(TEST_PATH);
        FileIO.copy(CLASS, "FileIOTest.file", REGULAR_PATH);
    }

    @Test
    final void test() throws IOException {
        final String expected = String.format("%s : REGULAR (%,d, %s);",
                                              REGULAR_PATH.getFileName(),
                                              Files.size(REGULAR_PATH),
                                              Files.getLastModifiedTime(REGULAR_PATH).toInstant());
        final String result = FileInfo.of(REGULAR_PATH).toString();
        assertEquals(expected, result);
    }
}
