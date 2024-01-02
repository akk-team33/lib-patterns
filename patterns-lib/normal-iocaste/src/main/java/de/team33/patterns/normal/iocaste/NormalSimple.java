package de.team33.patterns.normal.iocaste;

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
}
