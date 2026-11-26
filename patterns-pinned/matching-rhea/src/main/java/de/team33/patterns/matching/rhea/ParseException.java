package de.team33.patterns.matching.rhea;

public class ParseException extends RuntimeException {

    ParseException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
