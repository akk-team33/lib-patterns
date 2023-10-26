package de.team33.patterns.reflect.pandora;

import java.lang.reflect.Method;
import java.util.function.Predicate;
import java.util.stream.Stream;

enum Category {

    GETTER(Util::isGetter),
    SETTER(Util::isSetter),
    OTHER(method -> false);

    private final Predicate<Method> filter;

    Category(final Predicate<Method> filter) {
        this.filter = filter;
    }

    static Category of(final Method method) {
        return Stream.of(values())
                     .filter(value -> value.filter.test(method))
                     .findAny()
                     .orElse(OTHER);
    }
}
