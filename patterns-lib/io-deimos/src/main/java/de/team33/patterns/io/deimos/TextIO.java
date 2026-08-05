package de.team33.patterns.io.deimos;

import java.lang.Class;
import java.lang.String;
import java.nio.file.Path;
import java.nio.file.Path;

/**
 * @deprecated use {@link de.team33.patterns.io.thalassa.TextIO} instead.
 */
@Deprecated
public final class TextIO {

    private TextIO() {
    }

    /**
     * @deprecated use {@link de.team33.patterns.io.thalassa.TextIO#read(java.lang.Class, java.lang.String)} instead.
     */
    @Deprecated
    public static String read(final Class<?> referringClass, final String resourceName) {
        return Resource.by(referringClass, resourceName).readText();
    }

    /**
     * @deprecated use {@link de.team33.patterns.io.thalassa.TextIO#read(java.nio.file.Path)} instead.
     */
    @Deprecated
    public static String read(final Path path) {
        return Resource.by(path).readText();
    }
}
