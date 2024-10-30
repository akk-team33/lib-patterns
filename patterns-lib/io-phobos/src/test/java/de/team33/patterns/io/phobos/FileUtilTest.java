package de.team33.patterns.io.phobos;

import de.team33.patterns.io.deimos.TextIO;
import de.team33.patterns.io.phobos.testing.IoTestBase;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilTest extends IoTestBase {

    @Test
    void report_FileIndex() {
        final String expected = String.format(TextIO.read(FileUtilTest.class, "report_FileIndex.txt"),
                                              testPath().toAbsolutePath().normalize());
        final Path localTestPath = testPath().resolve("exceptional-dione")
                                             .resolve("src");
        final List<Path> paths = Arrays.asList(localTestPath.resolve("main"),
                                               localTestPath.resolve("test"));

        final String result = FileUtil.report(FileIndex.of(paths));
        System.out.println(result);

        assertEquals(expected, result);
    }

    @Test
    void report_FileEntry() {
        final String expected = String.format(TextIO.read(FileUtilTest.class, "report_FileEntry.txt"),
                                              testPath().toAbsolutePath().normalize());

        final String result = FileUtil.report(FileEntry.of(testPath()));
        System.out.println(result);

        assertEquals(expected, result);
    }
}
