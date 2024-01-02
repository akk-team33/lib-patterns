package de.team33.patterns.normal.iocaste;

public abstract class Normal {

    public abstract Type type();

    public final boolean isSimple() {
        return Type.SIMPLE == type();
    }

    public abstract String asSimple();

    public enum Type {

        SIMPLE,
        AGGREGATE,
        COMPOSITE;
    }
}
