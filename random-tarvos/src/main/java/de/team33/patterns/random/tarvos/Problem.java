package de.team33.patterns.random.tarvos;

class Problem extends Exception {

    Problem(final String message) {
        super(message);
    }

    Problem(final String message, final Throwable cause) {
        super(message, cause);
    }
}
