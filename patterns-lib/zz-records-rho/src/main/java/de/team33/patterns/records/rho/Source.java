package de.team33.patterns.records.rho;

import java.util.Objects;

class Source {

    private final String text;
    private int index;

    Source(final String text) {
        this(text, 0);
    }

    private Source(final String text, final int index) {
        this.text = Objects.requireNonNull(text);
        this.index = index;
    }

    final int index() {
        return index;
    }

    final boolean hasMore() {
        return index < text.length();
    }

    /**
     * Returns <em>this</em> {@link Source}.
     */
    @SuppressWarnings("UnusedReturnValue")
    final Source failIfMore() {
        if (hasMore()) {
            throw new IllegalArgumentException(
                    "expected end of source text at index %d".formatted(index));
        }
        return this;
    }

    /**
     * Returns <em>this</em> {@link Source}.
     */
    final Source failIfEOT() {
        if (!hasMore()) {
            throw new IllegalArgumentException(
                    "unexpected end of source text at index %d".formatted(index));
        }
        return this;
    }

    final char peek() {
        return failIfEOT().text.charAt(index);
    }

    /**
     * Returns <em>this</em> {@link Source}.
     */
    final Source skip() {
        return skip(1);
    }

    /**
     * Returns <em>this</em> {@link Source}.
     */
    final Source skip(final int offset) {
        index += offset;
        return this;
    }

    /**
     * Returns <em>this</em> {@link Source}.
     */
    final Source expect(final char expected) {
        final char c = failIfEOT().peek();
        if (expected == c) {
            return skip();
        } else {
            throw new IllegalArgumentException(
                    "expected '%c' - but was '%c' at index %d".formatted(expected, c, index));
        }
    }

    /**
     * Returns <em>this</em> {@link Source}.
     */
    @SuppressWarnings("UnusedReturnValue")
    final Source skipWhitespace() {
        while (hasMore() && Character.isWhitespace(peek())) {
            skip();
        }
        return this;
    }

    final String peekUntil(final CharPredicate predicate) {
        final Source fork = fork();
        while (fork.hasMore() && !predicate.test(fork.peek())) {
            fork.skip();
        }
        return text.substring(index, fork.index);
    }

    private Source fork() {
        return new Source(text, index);
    }

    @FunctionalInterface
    interface CharPredicate {

        boolean test(char c);
    }
}
