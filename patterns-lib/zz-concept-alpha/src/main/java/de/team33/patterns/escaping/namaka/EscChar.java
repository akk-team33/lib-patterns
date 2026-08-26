package de.team33.patterns.escaping.namaka;

import de.team33.patterns.enums.pan.Values;

import java.util.function.ToIntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
enum EscChar {

    SPECIAL(SpecialChar.REGEX, SpecialChar::toChar),
    OCTAL("[0-3][0-7][0-7]|[0-7][0-7]|[0-7]", octal -> Integer.parseInt(octal, Given.OCT_RADIX)),
    UNICODE("u[0-9A-Fa-f]{4}", unicode -> Integer.parseInt(unicode.substring(1), Given.HEX_RADIX)),
    ILLEGAL(".*", illegal -> -1);

    private static final Values<EscChar> VALUES = Values.of(EscChar.class);
    private static final String REGEX_BODY = VALUES.mapAll(value -> "(?<%s>%s)".formatted(value.name(), value.regex))
                                                   .collect(Collectors.joining("|"));
    private static final String REGEX = "\\\\(%s)".formatted(REGEX_BODY);
    static final Pattern PATTERN = Pattern.compile(REGEX);

    private final String regex;
    private final ToIntFunction<? super String> toInt;

    EscChar(final String regex, final ToIntFunction<? super String> toInt) {
        this.regex = regex;
        this.toInt = toInt;
    }

    @SuppressWarnings("NumericCastThatLosesPrecision")
    static char toChar(final Matcher matcher) {
        return (char) VALUES.stream()
                            .mapToInt(value -> value.toInt(matcher))
                            .filter(value -> (Character.MIN_VALUE <= value) && (value <= Character.MAX_VALUE))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Illegal escape sequence '%s'".formatted(matcher.group(0))));
    }

    private int toInt(final Matcher matcher) {
        final String group = matcher.group(name());
        if (null != group) {
            return toInt.applyAsInt(group);
        } else {
            return -1;
        }
    }

    private static class Given {

        private static final int OCT_RADIX = 8;
        private static final int HEX_RADIX = 16;
    }
}
