package de.team33.patterns.records.rho;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

class UtilTest {

    @Test
    final void typeName() {
        assertNull(Util.typeName(null));
    }
}