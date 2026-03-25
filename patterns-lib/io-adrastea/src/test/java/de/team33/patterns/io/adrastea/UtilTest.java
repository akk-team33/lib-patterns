package de.team33.patterns.io.adrastea;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UtilTest {

    @Test
    final void noOrder() {
        assertThrows(UnsupportedOperationException.class, () -> Util.NO_ORDER.compare(0, 1));
    }

    @Test
    final void missingFileAttributes() {
        assertThrows(UnsupportedOperationException.class, Util.MISSING_FILE_ATTRIBUTES::fileKey);
    }
}