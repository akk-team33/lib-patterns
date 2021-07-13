package de.team33.patterns.lazy.v1;

import de.team33.patterns.exceptional.v1.XSupplier;

class LazyBase<T, X extends Exception> {

    @SuppressWarnings({"FieldAccessedSynchronizedAndUnsynchronized", "PackageVisibleField"})
    XSupplier<T, X> backing;

    LazyBase(final XSupplier<T, X> initial) {
        this.backing = provisional(initial);
    }

    private static <T, X extends Exception> XSupplier<T, X> definite(final T result) {
        return () -> result;
    }

    @SuppressWarnings("NonPrivateFieldAccessedInSynchronizedContext")
    private XSupplier<T, X> provisional(final XSupplier<T, X> initial) {
        //noinspection AnonymousInnerClass
        return new XSupplier<T, X>() {
            @Override
            public synchronized T get() throws X {
                if (backing == this) {
                    backing = definite(initial.get());
                }
                return backing.get();
            }
        };
    }
}
