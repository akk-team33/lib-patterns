package de.team33.patterns.expiry.tethys;

import de.team33.patterns.exceptional.dione.XSupplier;
import de.team33.patterns.lazy.narvi.XReLazy;

import java.time.Duration;
import java.time.Instant;

class Mutual<T, X extends Exception> {

    private final XReLazy<? extends T, ? extends X> backing;
    private final Duration maxIdle;
    private final Duration maxLiving;
    private volatile Instant idleTimeout = Instant.MIN;
    private volatile Instant lifeTimeout = Instant.MIN;

    /**
     * Initializes a new instance of this container type given an {@link XSupplier} for the type to be handled and
     * an intended lifetime of such instances.
     * <p>
     * CAUTION: The given lifetime should be significantly smaller than the actually expected
     * life span of an instance to be handled, otherwise there may not be enough time to use a
     * {@linkplain #get() provided} instance successfully!
     *
     * @param newSubject the {@link XSupplier} for the instances to handle
     * @param maxIdle    the maximum idle time in milliseconds
     * @param maxLiving  the maximum lifetime in milliseconds
     */
    Mutual(final XSupplier<? extends T, ? extends X> newSubject, final Duration maxIdle, final Duration maxLiving) {
        this.backing = XReLazy.init(newSubject);
        this.maxIdle = maxIdle;
        this.maxLiving = maxLiving;
    }

    @SuppressWarnings("DesignForExtension")
    T get() throws X {
        return (isTimeout(Instant.now()) ? backing.reset() : backing).get();
    }

    private boolean isTimeout(final Instant now) {
        synchronized (backing) {
            final Instant soFarLifeTimeout = lifeTimeout;
            if (now.isAfter(lifeTimeout) || now.isAfter(idleTimeout)) {
                lifeTimeout = now.plus(maxLiving);
            }
            idleTimeout = now.plus(maxIdle);
            return soFarLifeTimeout != lifeTimeout;
        }
    }
}
