package de.team33.patterns.testing.titan.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Utility for unpacking zip files in test scenarios.
 */
public class ZipIO {

    private static final String CANNOT_UNZIP_RESOURCE = "cannot unzip resource%n" +
            "    resource name   : %s%n" +
            "    referring class : %s%n" +
            "    target path     : %s%n";
    private static final String CANNOT_UNZIP_FILE = "cannot unzip file%n" +
            "    zip file    : %s%n" +
            "    target path : %s%n";

    /**
     * Unzip a resource to a given target directory.
     */
    public static void unzip(final Class<?> refClass, final String rsrcName, final Path target) {
        try (final InputStream in = refClass.getResourceAsStream(rsrcName)) {
            unzip(in, target);
        } catch (final IOException | NullPointerException e) {
            throw new IllegalArgumentException(String.format(CANNOT_UNZIP_RESOURCE, rsrcName, refClass, target), e);
        }
    }

    /**
     * Unzip a source file to a given target directory.
     */
    public static void unzip(final Path srcZip, final Path target) {
        try (final InputStream in = Files.newInputStream(srcZip)) {
            unzip(in, target);
        } catch (final IOException e) {
            throw new IllegalArgumentException(String.format(CANNOT_UNZIP_FILE, srcZip, target), e);
        }
    }

    private static void unzip(final InputStream in, final Path targetRoot) throws IOException {
        try (final ZipInputStream zipIn = new ZipInputStream(in)) {
            int counter = 0;
            ZipEntry entry = zipIn.getNextEntry();
            while (null != entry) {
                // Empty directories will be skipped ...
                if (!entry.isDirectory()) {
                    final Path target = targetRoot.resolve(entry.getName());
                    Files.createDirectories(target.getParent());
                    Files.copy(zipIn, target, StandardCopyOption.REPLACE_EXISTING);
                    Files.setLastModifiedTime(target, entry.getLastModifiedTime());
                }
                counter += 1;
                entry = zipIn.getNextEntry();
            }
            if (0 == counter) {
                throw new IOException("zip file is empty or no zip file");
            }
        }
    }
}
