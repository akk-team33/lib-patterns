package de.team33.patterns.lazy.narvi.publics;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.lazy.narvi.sample.HostSample;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class HostSampleTest {

    private final HostSample host;

    HostSampleTest() {
        final Generator generator = Generator.of(new SecureRandom());
        this.host = new HostSample().setIntValue(generator.anyInt())
                                    .setStringValue(generator.anyString())
                                    .setInstantValue(Instant.now().plusNanos(generator.anyInt()));
    }

    @Test
    void toList() {
        final List<?> expected = List.of(host.getIntValue(), host.getStringValue(), host.getInstantValue());
        assertEquals(expected, host.toList());
    }

    @Test
    void testEquals() {
        final HostSample other = new HostSample().setIntValue(host.getIntValue())
                                                 .setStringValue(host.getStringValue())
                                                 .setInstantValue(host.getInstantValue());
        assertEquals(host, other);
    }

    @Test
    void testHashCode() {
        final int expected = List.of(host.getIntValue(), host.getStringValue(), host.getInstantValue())
                                 .hashCode();
        assertEquals(expected, host.hashCode());
    }

    @Test
    void testToString() {
        final String expected = HostSample.class.getSimpleName() +
                                List.of(host.getIntValue(), host.getStringValue(), host.getInstantValue());
        assertEquals(expected, host.toString());
    }
}