package de.team33.test.patterns.properties.methods;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ATrial {

    @Test
    final void getFirst() {
        final List<Method> methods = Stream.of(SubClass.class.getMethods())
                                           .filter(method -> "getFirst".equals(method.getName()))
                                           .collect(Collectors.toList());
        // Due to the type hierarchy, Class.getMethods () returns five versions of the "getFirst" method,
        // each with a different result type ...
        assertEquals(5, methods.size());

        // All five versions are considered to be declared in <SubClass> (!) ...
        methods.forEach(method -> {
            assertEquals(SubClass.class, method.getDeclaringClass());
        });

        // When the five versions are invoked, the same implementation (at the top level) is effectively always called.
        // This can be seen in the result that is always the same ...
        final SubClass sample = new SubClass();
        methods.forEach(methodA -> methods.forEach(methodB -> {
            try {
                final Object resultA = methodA.invoke(sample);
                final Object resultB = methodB.invoke(sample);
                assertEquals(resultA, resultB);
            } catch (final IllegalAccessException |  InvocationTargetException e) {
                throw new AssertionError(e.getMessage(), e);
            }
        }));
    }

    @Test
    final void getFirst_byBaseClass() {
        final List<Method> methods = Stream.of(BaseClass.class.getMethods())
                                           .filter(method -> "getFirst".equals(method.getName()))
                                           .collect(Collectors.toList());
        // Due to the type hierarchy of <BaseClass>, Class.getMethods() returns two versions of the "getFirst" method,
        // each with a different result type ...
        assertEquals(2, methods.size());

        // All two versions are considered to be declared in <BaseClass> ...
        methods.forEach(method -> {
            assertEquals(BaseClass.class, method.getDeclaringClass());
        });

        // When the two versions are invoked with an instance of <SubClass>, the same implementation (declared in
        // SubClass) is effectively always called. This can be seen in the result and that it is always the same ...
        final SubClass sample = new SubClass();
        methods.forEach(method -> {
            try {
                final Object resultA = method.invoke(sample);
                assertEquals("[de.team33.test.patterns.properties.methods.SubClass.getFirst()]", resultA.toString());
            } catch (final IllegalAccessException |  InvocationTargetException e) {
                throw new AssertionError(e.getMessage(), e);
            }
        });
    }

    @Test
    final void setFirst() {
        final List<Method> methods = Stream.of(SubClass.class.getMethods())
                                           .filter(method -> "setFirst".equals(method.getName()))
                                           .collect(Collectors.toList());
        assertEquals(4, methods.size());
    }

    @Test
    final void getSecond() {
        final List<Method> methods = Stream.of(SubClass.class.getMethods())
                                           .filter(method -> "getSecond".equals(method.getName()))
                                           .collect(Collectors.toList());
        assertEquals(1, methods.size());

        final SubClass sample = new SubClass();
        methods.forEach(methodA -> methods.forEach(methodB -> {
            try {
                final Object resultA = methodA.invoke(sample);
                final Object resultB = methodB.invoke(sample);
                assertEquals(resultA, resultB);
            } catch (final IllegalAccessException |  InvocationTargetException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }));
    }

    @Test
    final void getFirst_methodByName() throws NoSuchMethodException {
        final Method getter = SubClass.class.getMethod("getFirst");
        assertEquals("public final java.util.ArrayList de.team33.test.patterns.properties.methods.SubClass.getFirst()",
                     getter.toString());
        final Method setter = SubClass.class.getMethod("setFirst", getter.getReturnType());
        assertEquals("public final de.team33.test.patterns.properties.methods.SubClass " +
                             "de.team33.test.patterns.properties.methods.SubClass.setFirst(java.util.ArrayList)",
                     setter.toString());
    }

    @Test
    final void getThird_methodByName() throws NoSuchMethodException {
        final Method getter = SubClass.class.getMethod("getThird");
        assertEquals("public java.math.BigDecimal de.team33.test.patterns.properties.methods.SubClass.getThird()",
                     getter.toString());
        final Method setter = Stream.of(SubClass.class.getMethods())
                                    .filter(method -> "setThird".equals(method.getName()))
                                    .filter(method -> method.getParameterTypes()[0].isAssignableFrom(getter.getReturnType()))
                                    .peek(method -> System.out.println(method))
                                    .reduce((left, right) -> left.getParameterTypes()[0].isAssignableFrom(right.getParameterTypes()[0]) ? right : left)
                                    .orElse(null);
        assertNotNull(setter);
        assertEquals("public void de.team33.test.patterns.properties.methods.SubClass.setThird(java.lang.Number)",
                     setter.toString());
    }
}
