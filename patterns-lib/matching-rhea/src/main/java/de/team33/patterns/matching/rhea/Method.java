package de.team33.patterns.matching.rhea;

import de.team33.patterns.enums.pan.Values;

import java.util.function.Function;
import java.util.function.Supplier;

enum Method {

    RX(Function.identity()),
    WC(WildcardString::toRegEx);

    private static final Values<Method> VALUES = Values.of(Method.class);

    private final Function<String, String> toRegEx;

    Method(Function<String, String> toRegEx) {
        this.toRegEx = toRegEx;
    }

    static Method parse(final String name) throws InternalException {
        return name.isEmpty() ? WC : VALUES.findAny(value -> value.name().equalsIgnoreCase(name))
                                           .orElseThrow(newInternalException(name));
    }

    private static Supplier<InternalException> newInternalException(String name) {
        return () -> new InternalException("\"" + name + "\" is not a valid METHOD!");
    }

    final String toRegEx(final String origin) {
        return toRegEx.apply(origin);
    }
}
