package de.team33.patterns.files.pluto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UtilTest {

    @Test
    final void missingFileAttributes() {
        assertThrows(UnsupportedOperationException.class, Util.MISSING_FILE_ATTRIBUTES::fileKey);
    }
}