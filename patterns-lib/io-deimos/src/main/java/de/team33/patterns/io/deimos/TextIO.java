package de.team33.patterns.io.deimos;

import java.nio.file.Path;

/**
 * Utility for reading text from files, mainly in test scenarios.
 */
public final class TextIO {

    private TextIO() {
    }

    public static String read(final Class<?> referringClass, final String resourceName) {
        return Resource.by(referringClass, resourceName).readText();
    }

    public static String read(final Path path) {
        return Resource.by(path).readText();
    }
}
