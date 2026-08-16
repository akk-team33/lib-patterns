package de.team33.patterns.escaping.namaka;

final class Condition {

    private Condition() {
    }

    static boolean isCharLength(final CharSequence sequence) {
        return 1 == sequence.length();
    }
}
