package de.team33.patterns.matching.rhea;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WildcardString {

    private static final Pattern WILDCARD = Pattern.compile("[*?]");

    private final String origin;
    private final Matcher matcher;

    private WildcardString(final String origin) {
        this.origin = origin;
        this.matcher = WILDCARD.matcher(origin);
    }

    private Stream<String> stream(final int head) {
        if (matcher.find(head)) {
            final int start = matcher.start();
            final int end = matcher.end();
            return Stream.concat(Stream.of(origin.substring(head, start),
                                           origin.substring(start, end)),
                                 stream(end));
        } else {
            return Stream.of(origin.substring(head));
        }
    }

    public static String toRegEx(final String origin) {
        return new WildcardString(origin).stream(0)
                                         .filter(Util::isNotEmpty)
                                         .map(Util::toRegEx)
                                         .collect(Collectors.joining());
    }

    private static final class Util {

        static boolean isNotEmpty(final String subs) {
            return !subs.isEmpty();
        }

        static String toRegEx(final String subs) {
            switch (subs) {
            case "*":
                return ".*";
            case "?":
                return ".";
            default:
                return Pattern.quote(subs);
            }
        }
    }
}
