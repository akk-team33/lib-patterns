package de.team33.patterns.matching.rhea;

import de.team33.patterns.io.deimos.TextIO;

import java.nio.file.Path;
import java.util.regex.Pattern;

public final class NameMatcher {

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
        return parseEx(pattern.split(":", 2));
    }

    private static NameMatcher parseEx(final String[] parts) throws InternalException {
        return (1 == parts.length) ? parseEx("", parts[0]) : parseEx(parts[0], parts[1]);
    }

    private static NameMatcher parseEx(final String head, final String value) throws InternalException {
        return parseEx(head.split("/", 2), value);
    }

    private static NameMatcher parseEx(final String[] parts, final String value) throws InternalException {
        return (1 == parts.length) ? parseEx(parts[0], "", value) : parseEx(parts[0], parts[1], value);
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
