package de.team33.patterns.expiry.tethys.publics;

import de.team33.patterns.expiry.tethys.XRecent;
import de.team33.testing.bridging.styx.Bridger;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class XRecentTest extends Bridger {

    @Test
    final void get() throws Exception {
        final XRecent<Instant, Exception> recent = new XRecent<>(Instant::now, 100, 1000);
        final Instant first = recent.get();
        bridge(50);
        assertSame(first, recent.get());
        bridge(50);
        assertSame(first, recent.get());
        bridge(150);
        assertNotEquals(first, recent.get());
    }
}
