package de.team33.test.patterns.reflect.luna;

import de.team33.patterns.exceptional.e1.Conversion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class SupplyTest {

    private static final Supply SUPPLY = new Supply();

    static Stream<Method> methods() {
        return Stream.of(Supply.class.getMethods())
                     .filter(method -> method.getParameterCount() == 0)
                     .filter(method -> method.getName().startsWith("next"))
                     .filter(method -> method.getDeclaringClass().equals(Supply.class));
    }

    /**
     * REMARK: It is unlikely, but not entirely impossible, that this test will fail because two results are equal
     * by pure chance.
     */
    @ParameterizedTest
    @MethodSource("methods")
    final void supply(final Method method) throws InvocationTargetException, IllegalAccessException {
        final Object expected = method.invoke(SUPPLY);
        final Object result = method.invoke(SUPPLY);
        assertNotEquals(expected, result);
    }
}
