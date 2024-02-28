package de.team33.patterns.typing.atlas.publics;

import de.team33.patterns.typing.atlas.Typedef;
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

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@SuppressWarnings("ClassWithTooManyFields")
class TypedefTest {

    @SuppressWarnings("unused")
    private static final List<String> STRING_LIST = null;
    @SuppressWarnings("unused")
    private static final Map<String, List<String>> STRING_LIST_MAP = null;
    @SuppressWarnings("unused")
    private static final Comparable<String> COMPARABLE = null;
    public static final Class<TypedefTest> CLASS = TypedefTest.class;
    private static final Typedef TYPE = Typedef.by(CLASS);
    private static final Field COMPARABLE_FIELD = getDeclaredField("COMPARABLE");
    private static final Field STRING_LIST_FIELD = getDeclaredField("STRING_LIST");
    private static final Field STRING_LIST_MAP_FIELD = getDeclaredField("STRING_LIST_MAP");
    private static final Typedef COMPARABLE_TYPE = TYPE.typeOf(COMPARABLE_FIELD);
    private static final Typedef STRING_TYPE = Typedef.by(String.class);
    private static final Typedef LIST_TYPE = TYPE.typeOf(STRING_LIST_FIELD);
    private static final Typedef MAP_TYPE = TYPE.typeOf(STRING_LIST_MAP_FIELD);

    private static Field getDeclaredField(final String name) {
        try {
            return CLASS.getDeclaredField(name);
        } catch (final NoSuchFieldException e) {
            throw new IllegalStateException("should not happen!", e);
        }
    }

    @ParameterizedTest
    @EnumSource
    final void asClass(final Case testCase) {
        assertSame(testCase.asClass, testCase.type.asClass());
    }

    @ParameterizedTest
    @EnumSource
    final void getFormalParameters(final Case testCase) {
        assertEquals(testCase.formalParameters, testCase.type.getFormalParameters());
    }

    @Test
    final void getActualParameters() {
        assertEquals(emptyList(), STRING_TYPE.getActualParameters());
        assertEquals(Arrays.asList(STRING_TYPE, LIST_TYPE), MAP_TYPE.getActualParameters());
    }

    @Test
    final void getSuperType() {
        //noinspection OptionalGetWithoutIsPresent
        assertEquals(Typedef.by(Object.class), STRING_TYPE.getSuperType().get());
        assertEquals(Optional.empty(), MAP_TYPE.getSuperType());
    }

    @Test
    final void getSuperTypes() {
        assertEquals(new HashSet<>(Arrays.asList(
                             Typedef.by(Object.class),
                             Typedef.by(Serializable.class),
                             COMPARABLE_TYPE,
                             Typedef.by(CharSequence.class))),
                     new HashSet<>(STRING_TYPE.getSuperTypes()));
        assertEquals(emptySet(), new HashSet<>(MAP_TYPE.getSuperTypes()));
    }

    @Test
    final void getInterfaces() {
        assertEquals(new HashSet<>(Arrays.asList(
                             Typedef.by(Serializable.class),
                             COMPARABLE_TYPE,
                             Typedef.by(CharSequence.class))),
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

        //noinspection EmptyClass
        final Class<?> c1 = new TypeOf<String>(null) {
        }.getClass();
        //noinspection OptionalGetWithoutIsPresent
        final Typedef typeOfStringType = Typedef.by(c1).getSuperType().get();
        assertEquals(STRING_TYPE, typeOfStringType.typeOf(field));

        //noinspection EmptyClass
        final Class<?> c2 = new TypeOf<List<String>>(null) {
        }.getClass();
        //noinspection OptionalGetWithoutIsPresent
        final Typedef typeOfListType = Typedef.by(c2).getSuperType().get();
        assertEquals(LIST_TYPE, typeOfListType.typeOf(field));
    }

    @Test
    final void returnTypeOf() throws NoSuchMethodException {
        final Method method = SuperTypeOf.class.getDeclaredMethod("getField");

        //noinspection EmptyClass
        final Class<?> c1 = new TypeOf<String>(null) {
        }.getClass();
        //noinspection OptionalGetWithoutIsPresent
        final Typedef typeOfStringType = Typedef.by(c1).getSuperType().get();
        assertEquals(STRING_TYPE, typeOfStringType.returnTypeOf(method));

        //noinspection EmptyClass
        final Class<?> c2 = new TypeOf<List<String>>(null) {
        }.getClass();
        //noinspection OptionalGetWithoutIsPresent
        final Typedef typeOfListType = Typedef.by(c2).getSuperType().get();
        assertEquals(LIST_TYPE, typeOfListType.returnTypeOf(method));
    }

    @Test
    final void parameterTypesOf() throws NoSuchMethodException {
        final Method method = SuperTypeOf.class.getDeclaredMethod("getField");
        //noinspection EmptyClass
        final Class<?> c = new TypeOf<String>(null) {
        }.getClass();
        //noinspection OptionalGetWithoutIsPresent
        final Typedef typeOfStringType = Typedef.by(c).getSuperType().get();
        assertEquals(emptyList(), typeOfStringType.parameterTypesOf(method));
    }

    @Test
    final void exceptionTypesOf() throws NoSuchMethodException {
        final Method method = SuperTypeOf.class.getDeclaredMethod("getField");
        //noinspection EmptyClass
        final Class<?> c2 = new TypeOf<List<String>>(null) {
        }.getClass();
        //noinspection OptionalGetWithoutIsPresent
        final Typedef typeOfListType = Typedef.by(c2).getSuperType().get();
        assertEquals(emptyList(), typeOfListType.exceptionTypesOf(method));
    }

    @SuppressWarnings("AnonymousInnerClassMayBeStatic")
    @Test
    final void testEquals() {
        assertEquals(STRING_TYPE, Typedef.by(String.class));
        // TODO: assertEquals(MAP_TYPE, new Typedef<Map<String, List<String>>>() {});
    }

    @SuppressWarnings("AnonymousInnerClassMayBeStatic")
    @Test
    final void testHashCode() {
        assertEquals(STRING_TYPE.hashCode(), Typedef.by(String.class).hashCode());
        // TODO: assertEquals(MAP_TYPE.hashCode(), new Typedef<Map<String, List<String>>>() {}.hashCode());
    }

    @ParameterizedTest
    @EnumSource
    final void testToString(final Case testCase) {
        assertEquals(testCase.string, testCase.type.toString());
    }

    enum Case {
        INTEGER(Typedef.by(Integer.class), "java.lang.Integer", emptyList(), Integer.class),
        STRING(STRING_TYPE, "java.lang.String", emptyList(), String.class),
        LIST(LIST_TYPE, "java.util.List<java.lang.String>", singletonList("E"), List.class),
        MAP(MAP_TYPE,
            "java.util.Map<java.lang.String, java.util.List<java.lang.String>>",
            Arrays.asList("K", "V"),
            Map.class),

        INT_ARRAY(Typedef.by(int[].class), "int[]", singletonList("E"), int[].class),

        INTEGER_ARRAY(Typedef.by(Integer[].class), "java.lang.Integer[]", singletonList("E"), Integer[].class),

        LIST_ARRAY(Helper.stringListArrayType(),
                   "java.util.List<java.lang.String>[]",
                   singletonList("E"),
                   List[].class);

        private final Typedef type;
        private final String string;
        private final List<String> formalParameters;
        private final Class<?> asClass;

        Case(final Typedef type,
             final String string,
             final List<String> formalParameters,
             final Class<?> asClass) {
            this.type = type;
            this.string = string;
            this.formalParameters = formalParameters;
            this.asClass = asClass;
        }
    }

    @SuppressWarnings("UtilityClassWithoutPrivateConstructor")
    static final class Helper {

        @SuppressWarnings("unused")
        private static final List<String>[] STRING_LIST_ARRAY = null;

        static Typedef stringListArrayType() {
            return Typedef.by(Helper.class)
                          .typeOf(getDeclaredField("STRING_LIST_ARRAY"));
        }

        private static Field getDeclaredField(final String name) {
            try {
                return Helper.class.getDeclaredField(name);
            } catch (final NoSuchFieldException e) {
                throw new IllegalStateException("should not happen!", e);
            }
        }
    }
}
