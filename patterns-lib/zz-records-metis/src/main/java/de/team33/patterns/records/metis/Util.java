package de.team33.patterns.records.metis;

final class Util {

    private Util() {
    }

    @SuppressWarnings("ReturnOfNull")
    static String typeName(final Object obj) {
        return (null == obj) ? null : obj.getClass().getCanonicalName();
    }

    static <R, X extends Exception> R fail(final X exception) throws X {
        throw exception;
    }
}
