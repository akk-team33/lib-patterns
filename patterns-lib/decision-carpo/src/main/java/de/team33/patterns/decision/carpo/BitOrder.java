package de.team33.patterns.decision.carpo;

import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

/**
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/decision-thyone/">decision-thyone</a>
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/decision-thyone/apidocs/">decision-thyone/apidocs</a>
 * @deprecated consider module
 * <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/decision-thyone/apidocs/">decision-thyone</a>
 * as a replacement.
 */
@Deprecated
public enum BitOrder {

    /**
     * @deprecated see {@link BitOrder}.
     */
    @Deprecated
    LSB_FIRST((maxIndex, index) -> 1 << index),

    /**
     * @deprecated see {@link BitOrder}.
     */
    @Deprecated
    MSB_FIRST((maxIndex, index) -> 1 << (maxIndex - index));

    private final IntBinaryOperator bitOperator;

    BitOrder(final IntBinaryOperator bitOperator) {
        this.bitOperator = bitOperator;
    }

    final IntUnaryOperator operator(final int maxIndex) {
        return index -> bitOperator.applyAsInt(maxIndex, index);
    }
}
