package de.team33.patterns.normal.iocaste;

import java.util.Collection;

public abstract class Normal {

    public abstract Type type();

    public final boolean isSimple() {
        return Type.SIMPLE == type();
    }

    public final boolean isAggregate() {
        return Type.AGGREGATE == type();
    }

    public abstract String asSimple();

    public abstract Collection<Normal> asAggregate();

    public enum Type {

        SIMPLE,
        AGGREGATE,
        COMPOSITE;
    }
}
