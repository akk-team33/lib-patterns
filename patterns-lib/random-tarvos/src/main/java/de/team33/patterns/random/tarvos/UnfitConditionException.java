package de.team33.patterns.random.tarvos;

/**
 * A {@link RuntimeException} reflecting the unfit condition of a component involved in an operation.
 *
 * @deprecated Further development is discontinued and this package/module may be removed in a future release.
 * Use {@link de.team33.patterns.arbitrary.mimas.UnfitConditionException} instead.
 */
@Deprecated
public class UnfitConditionException extends RuntimeException {

    UnfitConditionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
