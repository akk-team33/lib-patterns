package de.team33.patterns.version.beta;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
class VersionTest {

    @Test
    void getVersion() {
        final String expected = "2.8.3";
        final Version version = Version.parse(expected);
        assertEquals(expected, version.toString());
    }
}