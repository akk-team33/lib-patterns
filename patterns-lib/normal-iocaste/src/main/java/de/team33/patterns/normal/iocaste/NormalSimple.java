package de.team33.patterns.normal.iocaste;

import java.util.Collection;

class NormalSimple extends Normal {

    private final String value;

    NormalSimple(final CharSequence stage) {
        this.value = stage.toString();
    }

    @Override
    public final Type type() {
        return Type.SIMPLE;
    }

    @Override
    public final String asSimple() {
        return value;
    }

    @Override
    public final Collection<Normal> asAggregate() {
        throw new UnsupportedOperationException("this is not an aggregate");
    }
}
