package de.team33.patterns.reflect.pandora;

import java.util.stream.Stream;

@SuppressWarnings("FieldNamingConvention")
enum Prefix {

    is,
    get,
    set,
    NONE(0);

    final int length;

    Prefix() {
        this.length = name().length();
    }

    Prefix(final int length) {
        this.length = length;
    }

    static Prefix of(final String name) {
        return Stream.of(values())
                     .filter(prefix -> name.startsWith(prefix.name()))
                     .findAny()
                     .orElse(NONE);
    }
}
