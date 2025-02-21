package de.team33.patterns.matching.rhea;

import de.team33.patterns.io.deimos.TextIO;

import java.nio.file.Path;
import java.util.regex.Pattern;

public class NameMatcher {

    private final Pattern pattern;

    private NameMatcher(final Method method, final CaseSensitivity sensitivity, final String value) {
        this.pattern = sensitivity.toPattern(method.toRegEx(value));
    }

    /**
     * @throws ParseException if <em>pattern</em> is invalid.
     */
    public static NameMatcher parse(final String pattern) throws IllegalArgumentException {
        try {
            return parseEx(pattern);
        } catch (final InternalException e) {
            final String message = String.format(TextIO.read(NameMatcher.class, "NameMatcher.txt"),
                                                 pattern, e.getMessage());
            throw new ParseException(message, e);
        }
    }

    private static NameMatcher parseEx(final String pattern) throws InternalException {
        final String[] parts = pattern.split(":", -1);
        if (1 == parts.length) {
            return parseEx("", parts[0]);
        } else if (2 == parts.length) {
            return parseEx(parts[0], parts[1]);
        } else {
            throw new InternalException("The VALUE must not contain ':' (a colon)!");
        }
    }

    private static NameMatcher parseEx(final String head, final String tail) throws InternalException {
        final String[] parts = head.split("/", -1);
        if (1 == parts.length) {
            return parseEx(parts[0], "", tail);
        } else if (2 == parts.length) {
            return parseEx(parts[0], parts[1], tail);
        } else {
            throw new InternalException("\"" + head + "\" is not a valid METHOD/OPTION!");
        }
    }

    private static NameMatcher parseEx(final String method,
                                       final String option,
                                       final String value) throws InternalException {
        return new NameMatcher(Method.parse(method), CaseSensitivity.parse(option), value);
    }

    public final boolean matches(final String name) {
        return pattern.matcher(name).matches();
    }

    public final boolean matches(final Path path) {
        return matches(path.getFileName().toString());
    }
}
