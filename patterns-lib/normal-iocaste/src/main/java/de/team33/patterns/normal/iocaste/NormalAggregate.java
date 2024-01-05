package de.team33.patterns.normal.iocaste;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableList;

class NormalAggregate extends Normal {

    private final Collection<Normal> values;

    NormalAggregate(final Collection<?> values, final Normalizer normalizer) {
        this.values = unmodifiableList(values.stream()
                                             .map(normalizer::normal)
                                             .collect(Collectors.toList()));
    }

    @Override
    public final Type type() {
        return Type.AGGREGATE;
    }

    @Override
    public final String asSimple() {
        throw new UnsupportedOperationException("this is not a simple");
    }

    @Override
    public final Collection<Normal> asAggregate() {
        //noinspection AssignmentOrReturnOfFieldWithMutableType
        return values;
    }
}
