package de.team33.patterns.matching.rhea;

import org.junit.jupiter.api.Test;

import static java.util.regex.Pattern.quote;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WildcardStringTest {

    static final String NO_WILDCARD_HERE = "no wildcard here";

    @Test
    void parse_STAR() {
        final String expected = ".*";
        final String result = WildcardString.parse("*");
        assertEquals(expected, result);
    }

    @Test
    void parse_QM() {
        final String expected = ".";
        final String result = WildcardString.parse("?");
        assertEquals(expected, result);
    }

    @Test
    void parse_NO_WILDCARD_HERE() {
        final String expected = quote(NO_WILDCARD_HERE);
        final String result = WildcardString.parse(NO_WILDCARD_HERE);
        assertEquals(expected, result);
    }

    @Test
    void parse() {
        final String expected = ".*" + quote("wdl") + "." + quote(".brm.") + ".*";
        final String result = WildcardString.parse("*wdl?.brm.*");
        assertEquals(expected, result);
    }
}
