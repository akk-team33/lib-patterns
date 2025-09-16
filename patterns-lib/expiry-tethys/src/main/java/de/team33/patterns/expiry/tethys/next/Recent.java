package de.team33.patterns.expiry.tethys.next;

import java.util.function.Supplier;

public class Recent<T> {

    public static <T> Recent<T> init(final Supplier<T> initial, final long maxIdle, final long maxLiving) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public final T get() {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
