package de.team33.patterns.normal.iocaste.publics;

import de.team33.patterns.normal.iocaste.Element;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class ElementTest {

    @ParameterizedTest
    @EnumSource
    final void testIntegerCase(final IntegerCase given) {
        try {
            final int result = Integer.parseInt(given.origin);
            assertTrue(given.expectedIsInteger);
        } catch (final NumberFormatException e) {
            assertFalse(given.expectedIsInteger);
        }
    }

    @ParameterizedTest
    @EnumSource
    final void isInteger(final IntegerCase given) {
        final Element element = Element.of(given.origin);
        assertEquals(given.expectedIsInteger, element.isInteger(),
                     () -> String.format("\"%s\".isInteger() is expected to be %s - but was %s",
                                         given.origin, given.expectedIsInteger, !given.expectedIsInteger));
    }

    @ParameterizedTest
    @EnumSource
    final void asInteger(final IntegerCase given) {
        final Element element = Element.of(given.origin);
        assertEquals(given.expectedInteger, element.asInteger());
    }

    enum IntegerCase {

        PLAIN_NUMBER("278", true, 278),
        PLAIN_ZERO("0", true, 0),
        MIN_INTEGER(String.valueOf(Integer.MIN_VALUE), true, Integer.MIN_VALUE),
        MAX_INTEGER(String.valueOf(Integer.MAX_VALUE), true, Integer.MAX_VALUE),
        LONG_BUT_ZERO("00000000000000000000000000000", true, 0),
        LONG_BUT_SMALL("00000000000000000000000000007", true, 7),
        TOO_LARGE(String.valueOf(1L + Integer.MAX_VALUE), false, null),
        TOO_SMALL(String.valueOf(-1L + Integer.MIN_VALUE), false, null),
        NO_NUMBER("wdlbrmpft", false, null),
        EMPTY("", false, null);

        final String origin;
        final boolean expectedIsInteger;
        final Integer expectedInteger;

        IntegerCase(final String origin, final boolean expectedIsInteger, final Integer expectedInteger) {
            this.origin = origin;
            this.expectedIsInteger = expectedIsInteger;
            this.expectedInteger = expectedInteger;
        }
    }
}
