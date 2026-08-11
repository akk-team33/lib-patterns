package de.team33.patterns.io.phobos;

import java.nio.file.Path;
import java.util.function.Function;

/**
 * @see de.team33.patterns.io.phobos package
 * @deprecated Useless outside of this module.
 */
@Deprecated
interface Normality extends Function<Path, Path> {

    /**
     * @deprecated see {@link Normality}.
     */
    @Deprecated
    Normality UNKNOWN = path -> path.toAbsolutePath().normalize();

    /**
     * @deprecated see {@link Normality}.
     */
    @Deprecated
    Normality DEFINITE = path -> path;
}
