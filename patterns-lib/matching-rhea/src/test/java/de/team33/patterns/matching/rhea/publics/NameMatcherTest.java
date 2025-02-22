package de.team33.patterns.matching.rhea.publics;

import de.team33.patterns.matching.rhea.NameMatcher;
import de.team33.patterns.matching.rhea.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class NameMatcherTest {

    @Test
    final void parse_WC0() {
        final NameMatcher matcher = NameMatcher.parse("");
        assertTrue(matcher.matches(""));
        assertFalse(matcher.matches(Paths.get("yourImage.jpg")));
    }

    @Test
    final void parse_WC1() {
        final NameMatcher matcher = NameMatcher.parse("my*.jpg");
        assertTrue(matcher.matches(Paths.get("myImage.jpg")));
        assertFalse(matcher.matches("yourImage.jpg"));
    }

    @Test
    final void parse_WC2() {
        final NameMatcher matcher = NameMatcher.parse("*image.*");
        assertTrue(matcher.matches("myImage.jpg"));
        assertTrue(matcher.matches(Paths.get("photos", "yourImage.jpg")));
        assertFalse(matcher.matches("myImagine.jpg"));
    }

    @Test
    final void parse_WC3() {
        final NameMatcher matcher = NameMatcher.parse("??imag*.???");
        assertTrue(matcher.matches("myImage.jpg"));
        assertFalse(matcher.matches("yourImage.jpg"));
        assertTrue(matcher.matches("myImagine.jpg"));
        assertFalse(matcher.matches("myImagine.jpeg"));
    }

    @Test
    final void parse_RX() {
        final NameMatcher matcher = NameMatcher.parse("rx/cs:myImage\\.(jpg|jpe|jpeg)");
        assertTrue(matcher.matches("myImage.jpg"));
        assertFalse(matcher.matches("MyImage.jpg"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "my:*:jpg", "my:*.jpg", "/my:*.jpg", "my/:*.jpg", "wc/cs/my:*.jpg", "wc/cs/:my*.jpg"})
    final void parse_fail(final String given) {
        try {
            final NameMatcher matcher = NameMatcher.parse(given);
            fail("expected to fail - but was " + matcher);
        } catch (final ParseException e) {
            // as expected -> OK!
            // e.printStackTrace();
        }
    }
}
