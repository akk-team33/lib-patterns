package de.team33.patterns.records.triton;

final class Util {

    private Util() {
    }

    @SuppressWarnings("ReturnOfNull")
    static String typeName(final Object obj) {
        return (null == obj) ? null : obj.getClass().getCanonicalName();
    }
}
