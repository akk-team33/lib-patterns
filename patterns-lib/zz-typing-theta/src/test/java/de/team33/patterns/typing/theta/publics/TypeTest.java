package de.team33.patterns.typing.theta.publics;

import de.team33.patterns.typing.theta.Type;
import de.team33.patterns.typing.theta.testing.ListType;
import de.team33.patterns.typing.theta.testing.MapType;
import de.team33.patterns.typing.theta.testing.StringListType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.Serializable;
import java.lang.constant.Constable;
import java.lang.constant.ConstantDesc;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

class TypeTest {

    private static final Type<String> STRING_TYPE = Type.of(String.class);
    private static final Type<List<String>> LIST_TYPE = new Type<List<String>>() {
    };
    private static final Type<Map<String, List<String>>> MAP_TYPE = new Type<Map<String, List<String>>>() {
    };

    static Stream<Case<?>> cases() {
        return Stream.of(new Case<>(STRING_TYPE, Set.of(Type.of(Object.class),
                                                        Type.of(Serializable.class),
                                                        new Type<Comparable<String>>() {
                                                        },
                                                        Type.of(CharSequence.class),
                                                        Type.of(Constable.class),
                                                        Type.of(ConstantDesc.class))),
                         new Case<>(MAP_TYPE, Set.of()));
    }

    @Test
    void genericDerivative() {
        try {
            final Type<Map<String, List<String>>> type = new MapType<>();
            fail("expected to fail - but was " + type);
        } catch (final IllegalStateException e) {
            e.printStackTrace();
            assertTrue(e.getMessage().contains(MapType.class.getSimpleName()));
        }
    }

    @Test
    void indirectAnonymousDerivative() {
        final Type<List<String>> type = new ListType<String>() {
        };
        assertEquals(LIST_TYPE, type);
    }

    @Test
    void indirectDerivative() {
        final Type<List<String>> type = new StringListType() {
        };
        assertEquals(LIST_TYPE, type);
    }

    @Test
    final void multipleDerivation() {
        //noinspection EmptyClass
        final Type<Map<String, List<String>>> mapType = new MapType<String, List<String>>() {
        };
        assertEquals(MAP_TYPE, mapType);
    }

    @ParameterizedTest
    @EnumSource
    final void core(final ToStringCase testCase) {
        assertSame(testCase.asClass, testCase.type.core());
    }

    @ParameterizedTest
    @EnumSource
    final void formalParameters(final ToStringCase testCase) {
        assertEquals(testCase.formalParameters, testCase.type.formalParameters());
    }

    @Test
    final void actualParameters() {
        assertEquals(emptyList(), STRING_TYPE.actualParameters());
        assertEquals(Arrays.asList(STRING_TYPE, LIST_TYPE), MAP_TYPE.actualParameters());
    }

    @Test
    final void superType() {
        assertEquals(Optional.of(Type.of(Object.class)), STRING_TYPE.superType());
        assertEquals(Optional.empty(), MAP_TYPE.superType());
    }

    @ParameterizedTest
    @MethodSource("cases")
    final <T> void superTypes(final Case<T> given) {
        assertEquals(given.superTypes, Set.copyOf(given.type.superTypes()));
    }

    @ParameterizedTest
    @MethodSource("cases")
    final <T> void interfaces(final Case<T> given) {
        assertEquals(given.interfaces(), Set.copyOf(given.type.interfaces()));
    }

    @Test
    final void typeOf() throws NoSuchFieldException {
        final Field field = SuperTypeOf.class.getDeclaredField("field");

        final Type<TypeOf<String>> typeOfStringType = new Type<TypeOf<String>>() {
        };
        assertEquals(STRING_TYPE, typeOfStringType.typeOf(field));

        final Type<TypeOf<List<String>>> typeOfListType = new Type<TypeOf<List<String>>>() {
        };
        assertEquals(LIST_TYPE, typeOfListType.typeOf(field));
    }

    @Test
    final void returnTypeOf() throws NoSuchMethodException {
        final Method method = SuperTypeOf.class.getDeclaredMethod("getField");

        final Type<TypeOf<String>> typeOfStringType = new Type<TypeOf<String>>() {
        };
        assertEquals(STRING_TYPE, typeOfStringType.returnTypeOf(method));

        final Type<TypeOf<List<String>>> typeOfListType = new Type<TypeOf<List<String>>>() {
        };
        assertEquals(LIST_TYPE, typeOfListType.returnTypeOf(method));
    }

    @Test
    final void parameterTypesOf() throws NoSuchMethodException {
        final Method method = SuperTypeOf.class.getDeclaredMethod("getField");
        final Type<TypeOf<String>> typeOfStringType = new Type<TypeOf<String>>() {
        };
        assertEquals(emptyList(), typeOfStringType.parameterTypesOf(method));
    }

    @Test
    final void exceptionTypesOf() throws NoSuchMethodException {
        final Method method = SuperTypeOf.class.getDeclaredMethod("getField");
        final Type<TypeOf<List<String>>> typeOfListType = new Type<TypeOf<List<String>>>() {
        };
        assertEquals(emptyList(), typeOfListType.exceptionTypesOf(method));
    }

    @SuppressWarnings("AnonymousInnerClassMayBeStatic")
    @Test
    final void testEquals() {
        assertEquals(STRING_TYPE, new Type<String>() {
        });
        assertEquals(MAP_TYPE, new Type<Map<String, List<String>>>() {
        });
    }

    @SuppressWarnings("AnonymousInnerClassMayBeStatic")
    @Test
    final void testHashCode() {
        assertEquals(STRING_TYPE.hashCode(), new Type<String>() {
        }.hashCode());
        assertEquals(MAP_TYPE.hashCode(), new Type<Map<String, List<String>>>() {
        }.hashCode());
    }

    @ParameterizedTest
    @EnumSource
    final void testToString(final ToStringCase testCase) {
        assertEquals(testCase.string, testCase.type.toString());
    }

    enum ToStringCase {
        INTEGER(Type.of(Integer.class), "java.lang.Integer", emptyList(), Integer.class),
        STRING(STRING_TYPE, "java.lang.String", emptyList(), String.class),
        LIST(LIST_TYPE, "java.util.List<java.lang.String>", singletonList("E"), List.class),
        MAP(MAP_TYPE,
            "java.util.Map<java.lang.String, java.util.List<java.lang.String>>",
            Arrays.asList("K", "V"),
            Map.class),

        INT_ARRAY(Type.of(int[].class), "int[]", singletonList("E"), int[].class),

        INTEGER_ARRAY(Type.of(Integer[].class), "java.lang.Integer[]", singletonList("E"), Integer[].class),

        LIST_ARRAY(new Type<List<String>[]>() {
        },
                   "java.util.List<java.lang.String>[]",
                   singletonList("E"),
                   List[].class);

        private final Type<?> type;
        private final String string;
        private final List<String> formalParameters;
        private final Class<?> asClass;

        ToStringCase(final Type<?> type,
                     final String string,
                     final List<String> formalParameters,
                     final Class<?> asClass) {
            this.type = type;
            this.string = string;
            this.formalParameters = formalParameters;
            this.asClass = asClass;
        }
    }

    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    record Case<T>(Type<T> type, Set<Type<?>> superTypes) {

        final Set<Type<?>> interfaces() {
            return superTypes.stream()
                             .filter(t -> t.core().isInterface())
                             .collect(Collectors.toSet());
        }
    }

    static class SuperTypeOf<T> {

        private final T field;

        SuperTypeOf(T field) {
            this.field = field;
        }

        final T getField() {
            return field;
        }
    }

    static class TypeOf<T> extends SuperTypeOf<T> {

        TypeOf(T field) {
            super(field);
        }
    }
}
