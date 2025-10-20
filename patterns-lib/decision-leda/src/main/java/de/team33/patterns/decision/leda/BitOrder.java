package de.team33.patterns.decision.leda;

import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

/**
 * @deprecated since 1.26.0 - consider module <em>decision-carpo</em> as replacement.
 *
 * @see <a href="https://www.team33.de/dev/patterns/1.x/patterns-lib/decision-carpo/">decision-carpo (1.x)</a>
 * @see <a href="https://www.team33.de/dev/patterns/2.x/patterns-lib/decision-carpo/">decision-carpo (2.x)</a>
 */
@Deprecated
public enum BitOrder {

    @Deprecated
    LSB_FIRST((maxIndex, index) -> 1 << index),

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
