package de.team33.patterns.random.tarvos;

/**
 * A {@link RuntimeException} reflecting the unfit condition of a component involved in an operation.
 */
public class UnfitConditionException extends RuntimeException {

    UnfitConditionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
