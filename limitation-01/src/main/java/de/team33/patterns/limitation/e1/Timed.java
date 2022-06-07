package de.team33.patterns.limitation.e1;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Timed<T> implements Supplier<T> {

    private final long lifetime;
    private final Supplier<T> newSubject;
    private final Consumer<T> oldSubject;
    private volatile long timeout = Long.MIN_VALUE;
    private volatile T current = null;

    public Timed(final long lifetime, final Supplier<T> newSubject) {
        this(lifetime, newSubject, any -> {});
    }

    public Timed(final long lifetime, final Supplier<T> newSubject, final Consumer<T> oldSubject) {
        this.lifetime = lifetime;
        this.newSubject = newSubject;
        this.oldSubject = oldSubject;
    }

    @Override
    public final T get() {
        final long now = System.currentTimeMillis();
        return (now > timeout) ? updated(now) : current;
    }

    private synchronized T updated(final long now) {
        if (now > timeout) {
            timeout = now + lifetime;
            Optional.ofNullable(current)
                    .ifPresent(oldSubject);
            current = newSubject.get();
        }
        return current;
    }
}
