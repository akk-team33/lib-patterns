package de.team33.patterns.test.testing.titan.io;

import de.team33.patterns.testing.titan.io.FileIO;
import de.team33.patterns.testing.titan.io.FileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileInfoTest {

    private static final Class<FileInfoTest> CLASS = FileInfoTest.class;
    private static final Path TEST_PATH = Paths.get("target", "testing", CLASS.getSimpleName())
                                               .toAbsolutePath()
                                               .normalize();
    private static final Path MISSING_PATH = TEST_PATH.resolve("missing.file");
    private static final Path REGULAR_PATH = TEST_PATH.resolve("regular.file");
    private static final Path EMPTY_DIRECTORY_PATH = TEST_PATH.resolve("empty.dir");

    @BeforeAll
    static void init() throws IOException {
        Files.createDirectories(EMPTY_DIRECTORY_PATH);
        FileIO.copy(CLASS, "FileIOTest.file", REGULAR_PATH);
    }

    @Test
    final void of_missing() throws IOException {
        final String expected = String.format("%s : MISSING;",
                                              MISSING_PATH.getFileName());
        final String result = FileInfo.of(MISSING_PATH).toString();
        assertEquals(expected, result);
    }

    @Test
    final void of_regular() throws IOException {
        final String expected = String.format("%s : REGULAR (%,d, %s);",
                                              REGULAR_PATH.getFileName(),
                                              Files.size(REGULAR_PATH),
                                              Files.getLastModifiedTime(REGULAR_PATH).toInstant());
        final String result = FileInfo.of(REGULAR_PATH).toString();
        assertEquals(expected, result);
    }

    @Test
    final void of_directory_empty() throws IOException {
        final String expected = String.format("%s : DIRECTORY {};", EMPTY_DIRECTORY_PATH.getFileName());
        final String result = FileInfo.of(EMPTY_DIRECTORY_PATH).toString();
        assertEquals(expected, result);
    }

    @Test
    final void of_directory_not_empty() throws IOException {
        final String expected = String.format("%s : DIRECTORY {%n" +
                                              "    %s : DIRECTORY {};%n" +
                                              "    %s : REGULAR (%,d, %s);%n" +
                                              "};",
                                              TEST_PATH.getFileName(),
                                              EMPTY_DIRECTORY_PATH.getFileName(),
                                              REGULAR_PATH.getFileName(),
                                              Files.size(REGULAR_PATH),
                                              Files.getLastModifiedTime(REGULAR_PATH).toInstant());
        final String result = FileInfo.of(TEST_PATH).toString();
        assertEquals(expected, result);
    }
}
