package de.team33.patterns.typing.atlas.publics;

import de.team33.patterns.typing.atlas.Type;
import de.team33.patterns.typing.atlas.testing.ListType;
import de.team33.patterns.typing.atlas.testing.StringListType;
import de.team33.patterns.typing.atlas.testing.MapType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings("EmptyClass")
class TypeTest {

    private static final Type<String> STRING_TYPE = Type.of(String.class);
    private static final Type<List<String>> LIST_TYPE = new Type<List<String>>() {};
    private static final Type<Map<String, List<String>>> MAP_TYPE = new Type<Map<String, List<String>>>() {};

    @Test
    final void genericDerivative() {
        try {
            final Type<Map<String, List<String>>> type = new MapType<>();
            fail("expected to fail - but was " + type);
        } catch (final IllegalStateException e) {
            // e.printStackTrace();
            assertTrue(e.getMessage().contains(MapType.class.getSimpleName()));
        }
    }

    @Test
    final void indirectAnonymousDerivative() {
        final Type<List<String>> type = new ListType<String>(){};
        assertEquals(LIST_TYPE, type);
    }

    @Test
    final void indirectDerivative() {
        final Type<List<String>> type = new StringListType(){};
        assertEquals(LIST_TYPE, type);
    }

    @Test
    final void multipleDerivation() {
        //noinspection EmptyClass
        final Type<Map<String, List<String>>> mapType = new MapType<String, List<String>>() {};
        assertEquals(MAP_TYPE, mapType);
    }

    @ParameterizedTest
    @EnumSource
    final void asClass(final TestCase testCase) {
        assertSame(testCase.asClass, testCase.type.asClass());
    }

    @ParameterizedTest
    @EnumSource
    final void getFormalParameters(final TestCase testCase) {
        assertEquals(testCase.formalParameters, testCase.type.getFormalParameters());
    }

    @Test
    final void getActualParameters() {
        assertEquals(emptyList(), STRING_TYPE.getActualParameters());
        assertEquals(Arrays.asList(STRING_TYPE, LIST_TYPE), MAP_TYPE.getActualParameters());
    }

    @Test
    final void getSuperType() {
        assertEquals(Optional.of(Type.of(Object.class)), STRING_TYPE.getSuperType());
        assertEquals(Optional.empty(), MAP_TYPE.getSuperType());
    }

    @Test
    final void getSuperTypes() {
        assertEquals(new HashSet<>(Arrays.asList(
                Type.of(Object.class),
                Type.of(Serializable.class),
                new Type<Comparable<String>>() {},
                Type.of(CharSequence.class)
                                                )), new HashSet<>(STRING_TYPE.getSuperTypes()));
        assertEquals(emptySet(), new HashSet<>(MAP_TYPE.getSuperTypes()));
    }

    @Test
    final void getInterfaces() {
        assertEquals(new HashSet<>(Arrays.asList(
                Type.of(Serializable.class),
                new Type<Comparable<String>>() {},
                Type.of(CharSequence.class))),
                     new HashSet<>(STRING_TYPE.getInterfaces()));
        assertEquals(emptySet(), new HashSet<>(MAP_TYPE.getSuperTypes()));
    }

    static class SuperTypeOf<T> {

        private final T field;

        SuperTypeOf(final T field) {
            this.field = field;
        }

        final T getField() {
            return field;
        }
    }

    static class TypeOf<T> extends SuperTypeOf<T> {

        TypeOf(final T field) {
            super(field);
        }
    }

    @Test
    final void typeOf() throws NoSuchFieldException {
        final Field field = SuperTypeOf.class.getDeclaredField("field");

        final Type<TypeOf<String>> typeOfStringType = new Type<TypeOf<String>>() {};
        assertEquals(STRING_TYPE, typeOfStringType.typeOf(field));

        final Type<TypeOf<List<String>>> typeOfListType = new Type<TypeOf<List<String>>>() {};
        assertEquals(LIST_TYPE, typeOfListType.typeOf(field));
    }

    @Test
    final void returnTypeOf() throws NoSuchMethodException {
        final Method method = SuperTypeOf.class.getDeclaredMethod("getField");

        final Type<TypeOf<String>> typeOfStringType = new Type<TypeOf<String>>() {};
        assertEquals(STRING_TYPE, typeOfStringType.returnTypeOf(method));

        final Type<TypeOf<List<String>>> typeOfListType = new Type<TypeOf<List<String>>>() {};
        assertEquals(LIST_TYPE, typeOfListType.returnTypeOf(method));
    }

    @Test
    final void parameterTypesOf() throws NoSuchMethodException {
        final Method method = SuperTypeOf.class.getDeclaredMethod("getField");
        final Type<TypeOf<String>> typeOfStringType = new Type<TypeOf<String>>() {};
        assertEquals(emptyList(), typeOfStringType.parameterTypesOf(method));
    }

    @Test
    final void exceptionTypesOf() throws NoSuchMethodException {
        final Method method = SuperTypeOf.class.getDeclaredMethod("getField");
        final Type<TypeOf<List<String>>> typeOfListType = new Type<TypeOf<List<String>>>() {};
        assertEquals(emptyList(), typeOfListType.exceptionTypesOf(method));
    }

    @SuppressWarnings("AnonymousInnerClassMayBeStatic")
    @Test
    final void testEquals() {
        assertEquals(STRING_TYPE, new Type<String>() {});
        assertEquals(MAP_TYPE, new Type<Map<String, List<String>>>() {});
    }

    @SuppressWarnings("AnonymousInnerClassMayBeStatic")
    @Test
    final void testHashCode() {
        assertEquals(STRING_TYPE.hashCode(), new Type<String>() {}.hashCode());
        assertEquals(MAP_TYPE.hashCode(), new Type<Map<String, List<String>>>() {}.hashCode());
    }

    @ParameterizedTest
    @EnumSource
    final void testToString(final TestCase testCase) {
        assertEquals(testCase.string, testCase.type.toString());
    }

    @SuppressWarnings({"unused", "QuestionableName"})
    enum TestCase {
        INTEGER(Type.of(Integer.class), "java.lang.Integer", emptyList(), Integer.class),
        STRING(STRING_TYPE, "java.lang.String", emptyList(), String.class),
        LIST(LIST_TYPE, "java.util.List<java.lang.String>", singletonList("E"), List.class),
        MAP(MAP_TYPE,
            "java.util.Map<java.lang.String, java.util.List<java.lang.String>>",
            Arrays.asList("K", "V"),
            Map.class),

        INT_ARRAY(Type.of(int[].class), "int[]", singletonList("E"), int[].class),

        INTEGER_ARRAY(Type.of(Integer[].class), "java.lang.Integer[]", singletonList("E"), Integer[].class),

        LIST_ARRAY(new Type<List<String>[]>(){},
                   "java.util.List<java.lang.String>[]",
                   singletonList("E"),
                   List[].class);

        private final Type<?> type;
        private final String string;
        private final List<String> formalParameters;
        private final Class<?> asClass;

        TestCase(final Type<?> type,
                 final String string,
                 final List<String> formalParameters,
                 final Class<?> asClass) {
            this.type = type;
            this.string = string;
            this.formalParameters = formalParameters;
            this.asClass = asClass;
        }
    }
}
