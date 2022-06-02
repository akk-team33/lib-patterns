package de.team33.patterns.maintenance.e1;

import java.util.function.Supplier;

public class Updated<T> implements Supplier<T> {

    private final long lifetime;
    private final Supplier<T> newSubject;
    private volatile long timeout = Long.MIN_VALUE;
    private volatile T current = null;

    public Updated(final long lifetime, final Supplier<T> newSubject) {
        this.lifetime = lifetime;
        this.newSubject = newSubject;
    }

    @Override
    public final T get() {
        final long now = System.currentTimeMillis();
        return (now > timeout) ? updated(now) : current;
    }

    private synchronized T updated(final long now) {
        if (now > timeout) {
            timeout = now + lifetime;
            current = newSubject.get();
        }
        return current;
    }
}
