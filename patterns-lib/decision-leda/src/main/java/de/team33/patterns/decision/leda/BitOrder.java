package de.team33.patterns.decision.leda;

import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

/**
 * Defines the bit-order to be used for converting a series of boolean results into an <em>int</em> value.
 */
public enum BitOrder {

    /**
     * The first of a series of boolean results will be the least significant bit of a resulting <em>int</em> value.
     */
    LSB_FIRST((maxIndex, index) -> 1 << index),

    /**
     * The first of a series of boolean results will be the most significant bit of a resulting <em>int</em> value.
     */
    MSB_FIRST((maxIndex, index) -> 1 << (maxIndex - index));

    private final IntBinaryOperator bitOperator;

    BitOrder(final IntBinaryOperator bitOperator) {
        this.bitOperator = bitOperator;
    }

    final IntUnaryOperator operator(final int maxIndex) {
        return index -> bitOperator.applyAsInt(maxIndex, index);
    }
}
