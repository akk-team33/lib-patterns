package de.team33.patterns.matching.rhea.publics;

import de.team33.patterns.matching.rhea.WildcardString;
import org.junit.jupiter.api.Test;

import static java.util.regex.Pattern.quote;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WildcardStringTest {

    static final String NO_WILDCARD_HERE = "no wildcard here";

    @Test
    void toRegEx_STAR() {
        final String expected = ".*";
        final String result = WildcardString.toRegEx("*");
        assertEquals(expected, result);
    }

    @Test
    void toRegEx_QM() {
        final String expected = ".";
        final String result = WildcardString.toRegEx("?");
        assertEquals(expected, result);
    }

    @Test
    void toRegEx_NO_WILDCARD_HERE() {
        final String expected = quote(NO_WILDCARD_HERE);
        final String result = WildcardString.toRegEx(NO_WILDCARD_HERE);
        assertEquals(expected, result);
    }

    @Test
    void toRegEx_mixed() {
        final String expected = ".*" + quote("wdl") + "." + quote(".brm.") + ".*";
        final String result = WildcardString.toRegEx("*wdl?.brm.*");
        assertEquals(expected, result);
    }
}
