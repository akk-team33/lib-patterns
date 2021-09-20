package de.team33.patterns.properties.e1;

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

enum MethodType {

    GETTER("get", method -> 0 == method.getParameterCount(), false),
    BOOLEAN_GETTER("is", method -> 0 == method.getParameterCount(), false),
    SETTER("set", method -> 1 == method.getParameterCount(), true);

    private static final String FMT_NO_MATCH = "the given method does not match any of the specified types%n" +
            "- method: %s%n";

    final String prefix;
    private final Predicate<Method> filter;
    private final boolean setter;

    MethodType(final String prefix, final Predicate<Method> filter, final boolean setter) {
        this.prefix = prefix;
        this.filter = filter;
        this.setter = setter;
    }

    static MethodType of(final Method method) {
        return optionalOf(method)
                .orElseThrow(() -> new IllegalArgumentException(String.format(FMT_NO_MATCH, method)));
    }

    static Optional<MethodType> optionalOf(final Method method) {
        return Stream.of(values())
                     .filter(value -> method.getName().startsWith(value.prefix))
                     .filter(value -> value.filter.test(method))
                     .findAny();
    }

    final String formalName(final Method method) {
        final String name = method.getName();
        final int index0 = prefix.length();
        final int index1 = (index0 < name.length()) ? (index0 + 1) : index0;
        final String head = name.substring(index0, index1);
        final String tail = name.substring(index1);
        return head.toLowerCase(Locale.ROOT) + tail;
    }

    final boolean isGetter() {
        return !setter;
    }

    final boolean isSetter() {
        return setter;
    }
}
