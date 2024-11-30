package de.team33.patterns.arbitrary.mimas;

import java.util.Locale;

enum Prefix {

    NEXT,
    ANY,
    GET,
    NEW,
    SET,
    IS,
    AS,
    TO,
    NONE("");

    final String value;

    Prefix(final String value) {
        this.value = value;
    }

    Prefix() {
        this.value = name().toLowerCase(Locale.ROOT);
    }
}
