package de.team33.java;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@Disabled
final class WildcardTypeTest {

    private static WildcardType wildcard(final String fieldName) throws NoSuchFieldException {
        final Field field = Samples.class.getDeclaredField(fieldName);
        final ParameterizedType parameterized =
                (ParameterizedType) field.getGenericType();

        return (WildcardType) parameterized.getActualTypeArguments()[0];
    }

    @Test
    void unbounded() throws NoSuchFieldException {
        final WildcardType wildcard = wildcard("unbounded");

        assertEquals(List.of(Object.class), List.of(wildcard.getUpperBounds()));
        assertEquals(List.of(), List.of(wildcard.getLowerBounds()));
    }

    @Test
    void upperBound() throws NoSuchFieldException {
        final WildcardType wildcard = wildcard("upper");

        assertEquals(List.of(Number.class), List.of(wildcard.getUpperBounds()));
        assertEquals(List.of(), List.of(wildcard.getLowerBounds()));
    }

    @Test
    void lowerBound() throws NoSuchFieldException {
        final WildcardType wildcard = wildcard("lower");

        assertEquals(List.of(Object.class), List.of(wildcard.getUpperBounds()));
        assertEquals(List.of(Number.class), List.of(wildcard.getLowerBounds()));
    }

    @Test
    void upperBoundWithTypeVariable() throws NoSuchFieldException {
        final WildcardType wildcard = wildcard("upperTypeVariable");

        assertEquals(1, wildcard.getUpperBounds().length);
        assertEquals(0, wildcard.getLowerBounds().length);
        assertInstanceOf(TypeVariable.class, wildcard.getUpperBounds()[0]);
    }

    @Test
    void lowerBoundWithTypeVariable() throws NoSuchFieldException {
        final WildcardType wildcard = wildcard("lowerTypeVariable");

        assertEquals(1, wildcard.getUpperBounds().length);
        assertEquals(1, wildcard.getLowerBounds().length);
        assertEquals(Object.class, wildcard.getUpperBounds()[0]);
        assertInstanceOf(TypeVariable.class, wildcard.getLowerBounds()[0]);
    }

    @Test
    void nested() throws NoSuchFieldException {
        final WildcardType wildcard = wildcard("nested");

        assertEquals(1, wildcard.getUpperBounds().length);
        assertEquals(0, wildcard.getLowerBounds().length);
        assertEquals(Number.class, wildcard.getUpperBounds()[0]);
    }

    @Test
    void arrayUpperBound() throws NoSuchFieldException {
        final WildcardType wildcard = wildcard("arrayUpper");

        assertEquals(1, wildcard.getUpperBounds().length);
        assertEquals(0, wildcard.getLowerBounds().length);
        assertInstanceOf(java.lang.reflect.GenericArrayType.class, wildcard.getUpperBounds()[0]);
    }

    @Test
    void arrayLowerBound() throws NoSuchFieldException {
        final WildcardType wildcard = wildcard("arrayLower");

        assertEquals(1, wildcard.getUpperBounds().length);
        assertEquals(1, wildcard.getLowerBounds().length);
        assertEquals(Object.class, wildcard.getUpperBounds()[0]);
        assertInstanceOf(java.lang.reflect.GenericArrayType.class, wildcard.getLowerBounds()[0]);
    }

    private static final class Samples<T extends Number & Comparable<T>> {

        List<?> unbounded;
        List<? extends Number> upper;
        List<? super Number> lower;

        List<? extends T> upperTypeVariable;
        List<? super T> lowerTypeVariable;

        List<List<? extends Number>> nested;

        List<? extends Number[]> arrayUpper;
        List<? super Number[]> arrayLower;
    }
}
