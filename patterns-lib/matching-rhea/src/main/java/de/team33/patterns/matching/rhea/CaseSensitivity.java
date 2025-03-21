package de.team33.patterns.matching.rhea;

import de.team33.patterns.enums.pan.Values;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
enum CaseSensitivity {

    CS(Pattern::compile),
    CI(regex -> Pattern.compile(regex, Pattern.CASE_INSENSITIVE));

    private static final Values<CaseSensitivity> VALUES = Values.of(CaseSensitivity.class);

    private final Function<? super String, Pattern> toPattern;

    CaseSensitivity(final Function<? super String, Pattern> toPattern) {
        this.toPattern = toPattern;
    }

    static CaseSensitivity parse(final String name) throws InternalException {
        return name.isEmpty() ? CI : VALUES.findAny(value -> value.name().equalsIgnoreCase(name))
                                           .orElseThrow(newInternalException(name));
    }

    private static Supplier<InternalException> newInternalException(final String name) {
        return () -> new InternalException("\"" + name + "\" is not a valid OPTION!");
    }

    final Pattern toPattern(final String regex) {
        return toPattern.apply(regex);
    }
}
