package de.team33.patterns.properties.e1;

import java.lang.reflect.Method;
import java.util.stream.Stream;

public class Methods {

    public static Stream<Method> streamOf(final Class<?> cls) {
        return Stream.of(cls.getMethods());
    }
}
