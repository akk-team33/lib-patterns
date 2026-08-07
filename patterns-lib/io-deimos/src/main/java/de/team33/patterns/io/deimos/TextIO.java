package de.team33.patterns.io.deimos;

import java.nio.file.Path;

/**
 * @deprecated consider
 * <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/io-thalassa/apidocs/de/team33/patterns/io/thalassa/TextIO.html">TextIO</a>
 * from module
 * <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/io-thalassa/apidocs/">io-thalassa</a>
 * as a replacement.
 */
@Deprecated
public final class TextIO {

    private TextIO() {
    }

    @Deprecated
    public static String read(final Class<?> referringClass, final String resourceName) {
        return Resource.by(referringClass, resourceName).readText();
    }

    @Deprecated
    public static String read(final Path path) {
        return Resource.by(path).readText();
    }
}
