package de.team33.patterns.testing.titan.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Utility for copying files in test scenarios.
 */
public final class FileIO {

    private static final String CANNOT_COPY_RESOURCE = "cannot copy resource%n" +
            "    resource name   : %s%n" +
            "    referring class : %s%n" +
            "    target path     : %s%n";
    private static final String CANNOT_COPY_FILE = "cannot copy file%n" +
            "    source path : %s%n" +
            "    target path : %s%n";

    private FileIO() {
    }

    /**
     * Copy a resource file to a given target directory.
     */
    public static void copy(final Class<?> refClass, final String rsrcName, final Path target) {
        try (final InputStream in = refClass.getResourceAsStream(rsrcName)) {
            copy(in, target);
        } catch (final IOException | NullPointerException e) {
            throw new IllegalArgumentException(String.format(CANNOT_COPY_RESOURCE, rsrcName, refClass, target), e);
        }
    }

    /**
     * Copy a source file to a given target directory.
     */
    public static void copy(final Path source, final Path target) {
        try (final InputStream in = Files.newInputStream(source)) {
            copy(in, target);
            Files.setLastModifiedTime(target, Files.getLastModifiedTime(source));
            Files.setLastModifiedTime(target, Files.getLastModifiedTime(source));
        } catch (final IOException e) {
            throw new IllegalArgumentException(String.format(CANNOT_COPY_FILE, source, target), e);
        }
    }

    /**
     * Copy a source input stream to a given target directory.
     */
    private static void copy(final InputStream in, final Path target) throws IOException {
        Files.createDirectories(target.getParent());
        Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
    }
}
