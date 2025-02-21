package de.team33.patterns.matching.rhea;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class WildcardString {

    private static final Pattern WILDCARD = Pattern.compile("[*?]");

    static String parse(final String origin) {
        final Matcher matcher = WILDCARD.matcher(origin);
        final List<String> result = new LinkedList<>();
        final Index index = new Index();
//        matcher.results().forEach(matchResult -> {
//            final int start = matchResult.start();
//            result.add(origin.substring(index.value, start));
//            result.add(origin.substring(start, index.value = matchResult.end()));
//        });
        result.add(origin.substring(index.value));
        return result.stream()
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

    private static class Index {
        private int value = 0;
    }
}
