package de.team33.patterns.reflect.pandora;

import java.util.stream.Stream;

enum Prefix {

    is(true, false),
    get(true, false),
    set(false, true),
    none(0, false, false);

    final int length;
    final boolean isGetter;
    final boolean isSetter;

    Prefix(final boolean isGetter, final boolean isSetter) {
        this.length = name().length();
        this.isGetter = isGetter;
        this.isSetter = isSetter;
    }

    Prefix(int length, final boolean isGetter, final boolean isSetter) {
        this.length = length;
        this.isGetter = isGetter;
        this.isSetter = isSetter;
    }

    static Prefix of(final String name) {
        return Stream.of(values())
                     .filter(value -> name.startsWith(value.name()))
                     .findAny()
                     .orElse(none);
    }
}
