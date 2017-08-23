package net.team33.patterns;

import java.util.Map;
import java.util.stream.Collectors;

public abstract class MultiLine {

    public static MultiLine of(final Map<?, ?> subject) {
        return new MAP(subject);
    }

    static String toString(final Map<?, ?> subject, final int indent) {
        //noinspection HardcodedLineSeparator
        return subject.entrySet().stream()
                .map(e -> e.getKey() + " = " + e.getValue())
                .collect(Collectors.joining(",\n    ", "{\n    ", "\n}"));
    }

    @Override
    public abstract String toString();

    private static class MAP extends MultiLine {

        private final Map<?, ?> subject;

        private MAP(final Map<?, ?> subject) {
            this.subject = subject;
        }

        @Override
        public final String toString() {
            return toString(subject, 0);
        }
    }
}
