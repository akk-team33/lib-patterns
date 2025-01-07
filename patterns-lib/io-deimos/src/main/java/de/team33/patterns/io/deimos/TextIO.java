package de.team33.patterns.io.deimos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.stream.Collectors;

/**
 * Utility for reading text from files, mainly in test scenarios.
 */
public final class TextIO {
    private static final String NEW_LINE = String.format("%n");

    private TextIO() {
    }

    public static String read(final Class<?> referringClass, final String resourceName) {
        return Resource.by(referringClass, resourceName).readText();
    }

    public static String read(final Path path) {
        return Resource.by(path).readText();
    }
}
