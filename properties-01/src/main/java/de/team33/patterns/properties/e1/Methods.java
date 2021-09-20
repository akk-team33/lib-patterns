package de.team33.patterns.properties.e1;

import java.lang.reflect.Method;
import java.util.stream.Stream;

public class Methods {

    public static Stream<Method> streamOf(final Class<?> cls) {
        return Stream.of(cls.getMethods());
    }

    public static boolean isGetter(final Method method) {
        return MethodType.optionalOf(method).map(MethodType::isGetter).orElse(false);
    }
}
