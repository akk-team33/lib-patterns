package de.team33.patterns.decision.leda;

import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

public enum BitOrder {

    LSB_FIRST((maxIndex, index) -> 1 << index),
    MSB_FIRST((maxIndex, index) -> 1 << (maxIndex - index));

    private final IntBinaryOperator bitOperator;

    BitOrder(final IntBinaryOperator bitOperator) {
        this.bitOperator = bitOperator;
    }

    final IntUnaryOperator operator(final int maxIndex) {
        return index -> bitOperator.applyAsInt(maxIndex, index);
    }
}
