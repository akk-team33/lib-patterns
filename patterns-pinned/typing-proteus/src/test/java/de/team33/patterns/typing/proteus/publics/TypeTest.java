package de.team33.patterns.typing.proteus.publics;

import de.team33.patterns.typing.proteus.Type;
import de.team33.patterns.typing.proteus.sample.BuilderTypeTestSubject;
import de.team33.patterns.typing.proteus.sample.TypeTestRecord;
import de.team33.patterns.typing.proteus.sample.TypeTestSubject;
import de.team33.patterns.typing.proteus.testing.ListType;
import de.team33.patterns.typing.proteus.testing.MapType;
import de.team33.patterns.typing.proteus.testing.StringListType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.Serializable;
import java.lang.constant.Constable;
import java.lang.constant.ConstantDesc;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ClassWithTooManyMethods")
class TypeTest {

    private static final Type<String> STRING = Type.of(String.class);
    private static final Type<List<String>> LIST = new Type<>() {};
    private static final Type<Map<String, List<String>>> MAP = new Type<>() {};
    private static final Type<?> EXTENDS_OBJECT_TYPE =
            nextReturnType(new Type<>() {});
    private static final Type<? extends CharSequence> EXTENDS_CS_TYPE =
            nextReturnType(new Type<Iterator<? extends CharSequence>>() {});
    private static final Type<? super CharSequence> SUPER_CS_TYPE =
            nextReturnType(new Type<Iterator<? super CharSequence>>() {});

    static Stream<AssignableCase> assignableCases() {
        return Stream.of(
                // Identical types
                new AssignableCase(
                        Type.of(String.class),
                        Type.of(String.class),
                        true),

                // Ordinary class hierarchy
                assignableCase(long.class, int.class, false),
                assignableCase(int.class, long.class, false),
                assignableCase(Integer.class, int.class, false),
                assignableCase(Object.class, int.class, false),
                assignableCase(Object.class, double.class, false),
                assignableCase(Object.class, Integer.class, true),
                assignableCase(Object.class, Double.class, true),
                assignableCase(Object.class, Number.class, true),
                assignableCase(Object.class, Comparable.class, true),
                assignableCase(Number.class, Double.class, true),
                assignableCase(Comparable.class, Double.class, true),
                assignableCase(Number.class, Comparable.class, false),
                assignableCase(Comparable.class, Number.class, false),
                assignableCase(Object.class, void.class, false),
                assignableCase(Object.class, Void.class, true),
                assignableCase(Void.class, void.class, false),
                assignableCase(void.class, Void.class, false),
                assignableCase(Void.class, Void.class, true),
                assignableCase(void.class, void.class, true),
                assignableCase(Integer.class, Object.class, false),

                // Interfaces
                new AssignableCase(Type.of(Iterable.class),
                                   new Type<ArrayList<String>>() {},
                                   true),
                new AssignableCase(Type.of(java.io.Serializable.class),
                                   Type.of(String.class),
                                   true),

                // Object accepts every reference type
                new AssignableCase(Type.of(Object.class),
                                   new Type<List<String>>() {},
                                   true),

                // Parameterized types
                new AssignableCase(new Type<List<String>>() {},
                                   new Type<List<String>>() {},
                                   true),
                new AssignableCase(new Type<List<String>>() {},
                                   new Type<List<Object>>() {},
                                   false),

                // Parameterized supertypes
                new AssignableCase(new Type<Iterable<String>>() {},
                                   new Type<ArrayList<String>>() {},
                                   true),
                new AssignableCase(new Type<ArrayList<String>>() {},
                                   new Type<Iterable<String>>() {},
                                   false),
                new AssignableCase(new Type<ArrayList<String>>() {},
                                   new Type<StringList>() {},
                                   true),
                new AssignableCase(new Type<Iterable<CharSequence>>() {},
                                   new Type<ArrayList<String>>() {},
                                   false),

                // Unbounded wildcard
                new AssignableCase(new Type<List<?>>() {},
                                   new Type<List<String>>() {},
                                   true),
                new AssignableCase(new Type<List<?>>() {},
                                   new Type<List<List<String>>>() {},
                                   true),
                new AssignableCase(new Type<List<?>>() {},
                                   new Type<List<Object>>() {},
                                   true),

                // Upper-bounded wildcard
                new AssignableCase(new Type<List<? extends CharSequence>>() {},
                                   new Type<List<String>>() {},
                                   true),
                new AssignableCase(new Type<List<? extends CharSequence>>() {},
                                   new Type<List<Object>>() {},
                                   false),

                // Lower-bounded wildcard
                new AssignableCase(new Type<List<? super String>>() {},
                                   new Type<List<String>>() {},
                                   true),
                new AssignableCase(new Type<List<? super String>>() {},
                                   new Type<List<Object>>() {},
                                   true),
                new AssignableCase(new Type<List<? super String>>() {},
                                   new Type<List<Integer>>() {},
                                   false),

                // Nested parameterized types
                new AssignableCase(new Type<List<List<?>>>() {},
                                   new Type<List<List<String>>>() {},
                                   false),
                new AssignableCase(new Type<List<? extends List<?>>>() {},
                                   new Type<List<List<String>>>() {},
                                   true),
                new AssignableCase(new Type<List<List<String>>>() {},
                                   new Type<List<List<Object>>>() {},
                                   false),

                // Arrays
                new AssignableCase(Type.of(Object.class),
                                   Type.of(String[].class),
                                   true),
                new AssignableCase(Type.of(Object[].class),
                                   Type.of(String[].class),
                                   true),
                new AssignableCase(Type.of(CharSequence[].class),
                                   Type.of(String[].class),
                                   true),
                new AssignableCase(Type.of(String[].class),
                                   Type.of(Object[].class),
                                   false),
                new AssignableCase(Type.of(Object[].class),
                                   Type.of(int[].class),
                                   false),

                // Raw type and parameterized type
                new AssignableCase(Type.of(List.class),
                                   new Type<List<String>>() {},
                                   true),
                new AssignableCase(new Type<List<String>>() {},
                                   Type.of(List.class),
                                   false),

                // Wildcards - 1. Wildcard <- Non-Wildcard
                new AssignableCase(EXTENDS_OBJECT_TYPE,
                                   Type.of(String.class),
                                   true),
                new AssignableCase(EXTENDS_CS_TYPE,
                                   Type.of(CharSequence.class),
                                   true),
                new AssignableCase(EXTENDS_CS_TYPE,
                                   Type.of(String.class),
                                   true),
                new AssignableCase(EXTENDS_CS_TYPE,
                                   Type.of(Object.class),
                                   false),
                new AssignableCase(SUPER_CS_TYPE,
                                   Type.of(Object.class),
                                   true),
                new AssignableCase(SUPER_CS_TYPE,
                                   Type.of(CharSequence.class),
                                   true),
                new AssignableCase(SUPER_CS_TYPE,
                                   Type.of(String.class),
                                   false),

                // Wildcards - 2. Non-Wildcard <- Wildcard
                new AssignableCase(Type.of(String.class),
                                   EXTENDS_OBJECT_TYPE,
                                   false),
                new AssignableCase(Type.of(CharSequence.class),
                                   EXTENDS_CS_TYPE,
                                   true),
                new AssignableCase(Type.of(String.class),
                                   EXTENDS_CS_TYPE,
                                   false),
                new AssignableCase(Type.of(Object.class),
                                   EXTENDS_CS_TYPE,
                                   true),
                new AssignableCase(Type.of(Object.class),
                                   SUPER_CS_TYPE,
                                   true),
                new AssignableCase(Type.of(CharSequence.class),
                                   SUPER_CS_TYPE,
                                   false),
                new AssignableCase(Type.of(String.class),
                                   SUPER_CS_TYPE,
                                   false),

                // Wildcards - 3. Wildcard <- Wildcard
                new AssignableCase(EXTENDS_OBJECT_TYPE,
                                   EXTENDS_OBJECT_TYPE,
                                   true),
                new AssignableCase(EXTENDS_OBJECT_TYPE,
                                   EXTENDS_CS_TYPE,
                                   true),
                new AssignableCase(EXTENDS_OBJECT_TYPE,
                                   SUPER_CS_TYPE,
                                   true),
                new AssignableCase(EXTENDS_CS_TYPE,
                                   EXTENDS_OBJECT_TYPE,
                                   false),
                new AssignableCase(EXTENDS_CS_TYPE,
                                   EXTENDS_CS_TYPE,
                                   true),
                new AssignableCase(EXTENDS_CS_TYPE,
                                   SUPER_CS_TYPE,
                                   false),
                new AssignableCase(SUPER_CS_TYPE,
                                   EXTENDS_OBJECT_TYPE,
                                   false),
                new AssignableCase(SUPER_CS_TYPE,
                                   EXTENDS_CS_TYPE,
                                   false),
                new AssignableCase(SUPER_CS_TYPE,
                                   SUPER_CS_TYPE,
                                   true));
    }

    @SuppressWarnings("unchecked")
    private static <T> Type<T> nextReturnType(final Type<? extends Iterator<?>> type) {
        try {
            final Method method = Iterator.class.getDeclaredMethod("next");
            return (Type<T>) type.returnTypeOf(method);
        } catch (final NoSuchMethodException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    static Stream<Case<?>> cases() {
        return Stream.of(new Case<>(STRING, Set.of(Type.of(Object.class),
                                                   Type.of(Serializable.class),
                                                   new Type<Comparable<String>>() {},
                                                   Type.of(CharSequence.class),
                                                   Type.of(Constable.class),
                                                   Type.of(ConstantDesc.class))),
                         new Case<>(LIST, Set.of(new Type<Collection<String>>() {})),
                         new Case<>(Type.of(List.class), Set.of(Type.of(Collection.class))),
                         new Case<>(MAP, Set.of()));
    }

    static Stream<TORC_Case> torc_Cases() {
        return Stream.of(new TORC_Case(0, "java.lang.String"),
                         new TORC_Case(1, "java.lang.String"),
                         new TORC_Case(2, "java.util.List<java.lang.Double>"),
                         new TORC_Case(3, "java.util.Map<java.lang.String, java.util.List<java.time.Instant>>"));
    }

    private static AssignableCase assignableCase(final Class<?> left, final Class<?> right, final boolean expected) {
        return new AssignableCase(Type.of(left), Type.of(right), expected);
    }

    @SuppressWarnings({"rawtypes", "TypeParameterExtendsFinalClass"})
    static Stream<FieldCase> fieldCases() {
        final Type<TypeTestSubject<String>> baseContext = new Type<>() {};
        final Type<BuilderTypeTestSubject> builderContext = new Type<>() {};
        final Type<TypeTestSubject> rawContext = new Type<>() {};
        return Stream.of(
                new FieldCase("string", baseContext, Type.of(String.class)),
                new FieldCase("primitive", baseContext, Type.of(int.class)),
                new FieldCase("array", baseContext, new Type<String[]>() {}),
                new FieldCase("variable", baseContext, Type.of(String.class)),
                new FieldCase("variableArray", baseContext, new Type<String[]>() {}),
                new FieldCase("parameterized", baseContext, new Type<List<String>>() {}),
                new FieldCase("parameterizedMultiple", baseContext, new Type<Map<String, String>>() {}),
                new FieldCase("wildcard", baseContext, new Type<List<?>>() {}),
                new FieldCase("upperBounded", baseContext, new Type<List<? extends String>>() {}),
                new FieldCase("lowerBounded", baseContext, new Type<List<? super String>>() {}),
                new FieldCase("nested", baseContext, new Type<List<List<String>>>() {}),
                new FieldCase("nestedWildcard", baseContext, new Type<Map<String, List<? extends String>>>() {}),

                new FieldCase("string", builderContext, Type.of(String.class)),
                new FieldCase("primitive", builderContext, Type.of(int.class)),
                new FieldCase("array", builderContext, new Type<String[]>() {}),
                new FieldCase("variable", builderContext, Type.of(StringBuilder.class)),
                new FieldCase("variableArray", builderContext, new Type<StringBuilder[]>() {}),
                new FieldCase("parameterized", builderContext, new Type<List<StringBuilder>>() {}),
                new FieldCase("parameterizedMultiple", builderContext, new Type<Map<String, StringBuilder>>() {}),
                new FieldCase("wildcard", builderContext, new Type<List<?>>() {}),
                new FieldCase("upperBounded", builderContext, new Type<List<? extends StringBuilder>>() {}),
                new FieldCase("lowerBounded", builderContext, new Type<List<? super StringBuilder>>() {}),
                new FieldCase("nested", builderContext, new Type<List<List<StringBuilder>>>() {}),
                new FieldCase("nestedWildcard", builderContext, new Type<Map<String, List<? extends StringBuilder>>>() {}),

                new FieldCase("string", rawContext, Type.of(String.class)),
                new FieldCase("primitive", rawContext, Type.of(int.class)),
                new FieldCase("array", rawContext, new Type<String[]>() {}),
                new FieldCase("variable", rawContext, Type.of(CharSequence.class)),
                new FieldCase("variableArray", rawContext, new Type<CharSequence[]>() {}),
                new FieldCase("parameterized", rawContext, new Type<List>() {}),
                new FieldCase("parameterizedMultiple", rawContext, new Type<Map>() {}),
                new FieldCase("wildcard", rawContext, new Type<List>() {}),
                new FieldCase("upperBounded", rawContext, new Type<List>() {}),
                new FieldCase("lowerBounded", rawContext, new Type<List>() {}),
                new FieldCase("nested", rawContext, new Type<List>() {}),
                new FieldCase("nestedWildcard", rawContext, new Type<Map>() {}));
    }

    @SuppressWarnings({"rawtypes", "TypeParameterExtendsFinalClass"})
    static Stream<FieldCase> recordComponentCases() {
        final Type<TypeTestRecord<String>> baseContext = new Type<>() {};
        final Type<TypeTestRecord<StringBuilder>> builderContext = new Type<>() {};
        final Type<TypeTestRecord> rawContext = new Type<>() {};
        return Stream.of(
                new FieldCase("string", baseContext, Type.of(String.class)),
                new FieldCase("primitive", baseContext, Type.of(int.class)),
                new FieldCase("array", baseContext, new Type<String[]>() {}),
                new FieldCase("variable", baseContext, Type.of(String.class)),
                new FieldCase("variableArray", baseContext, new Type<String[]>() {}),
                new FieldCase("parameterized", baseContext, new Type<List<String>>() {}),
                new FieldCase("parameterizedMultiple", baseContext, new Type<Map<String, String>>() {}),
                new FieldCase("wildcard", baseContext, new Type<List<?>>() {}),
                new FieldCase("upperBounded", baseContext, new Type<List<? extends String>>() {}),
                new FieldCase("lowerBounded", baseContext, new Type<List<? super String>>() {}),
                new FieldCase("nested", baseContext, new Type<List<List<String>>>() {}),
                new FieldCase("nestedWildcard", baseContext, new Type<Map<String, List<? extends String>>>() {}),

                new FieldCase("string", builderContext, Type.of(String.class)),
                new FieldCase("primitive", builderContext, Type.of(int.class)),
                new FieldCase("array", builderContext, new Type<String[]>() {}),
                new FieldCase("variable", builderContext, Type.of(StringBuilder.class)),
                new FieldCase("variableArray", builderContext, new Type<StringBuilder[]>() {}),
                new FieldCase("parameterized", builderContext, new Type<List<StringBuilder>>() {}),
                new FieldCase("parameterizedMultiple", builderContext, new Type<Map<String, StringBuilder>>() {}),
                new FieldCase("wildcard", builderContext, new Type<List<?>>() {}),
                new FieldCase("upperBounded", builderContext, new Type<List<? extends StringBuilder>>() {}),
                new FieldCase("lowerBounded", builderContext, new Type<List<? super StringBuilder>>() {}),
                new FieldCase("nested", builderContext, new Type<List<List<StringBuilder>>>() {}),
                new FieldCase("nestedWildcard", builderContext, new Type<Map<String, List<? extends StringBuilder>>>() {}),

                new FieldCase("string", rawContext, Type.of(String.class)),
                new FieldCase("primitive", rawContext, Type.of(int.class)),
                new FieldCase("array", rawContext, new Type<String[]>() {}),
                new FieldCase("variable", rawContext, Type.of(CharSequence.class)),
                new FieldCase("variableArray", rawContext, new Type<CharSequence[]>() {}),
                new FieldCase("parameterized", rawContext, new Type<List>() {}),
                new FieldCase("parameterizedMultiple", rawContext, new Type<Map>() {}),
                new FieldCase("wildcard", rawContext, new Type<List>() {}),
                new FieldCase("upperBounded", rawContext, new Type<List>() {}),
                new FieldCase("lowerBounded", rawContext, new Type<List>() {}),
                new FieldCase("nested", rawContext, new Type<List>() {}),
                new FieldCase("nestedWildcard", rawContext, new Type<Map>() {}));
    }

    @Test
    final void wildcardTypes() {
        assertEquals("?", EXTENDS_OBJECT_TYPE.toString());
        assertEquals("? extends java.lang.CharSequence", EXTENDS_CS_TYPE.toString());
        assertEquals("? super java.lang.CharSequence", SUPER_CS_TYPE.toString());
    }

    @Test
    final void boxed() {
        assertEquals(Type.of(Float.class), Type.of(float.class).boxed());
        assertEquals(Type.of(Void.class), Type.of(void.class).boxed());
        assertEquals(Type.of(Integer.class), Type.of(Integer.class).boxed());
        assertEquals(new Type<List<String>>() {}, new Type<List<String>>() {}.boxed());
    }

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
        final Type<List<String>> type = new ListType<>() {};
        assertEquals(LIST, type);
    }

    @Test
    final void indirectDerivative() {
        final Type<List<String>> type = new StringListType();
        assertEquals(LIST, type);
    }

    @Test
    final void multipleDerivation() {
        //noinspection EmptyClass
        final Type<Map<String, List<String>>> mapType = new MapType<>() {};
        assertEquals(MAP, mapType);
    }

    @Test
    final void superType() {
        assertEquals(Optional.of(Type.of(Object.class)), STRING.superType());
        assertEquals(Optional.empty(), MAP.superType());
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

    @ParameterizedTest
    @EnumSource
    final void core(final ToStringCase testCase) {
        assertSame(testCase.asClass, testCase.type.core());
    }

    @ParameterizedTest
    @MethodSource("torc_Cases")
    final void typeOf_RecordComponent(final TORC_Case given) {
        final var result = given.type().typeOf(given.component());
        assertEquals(given.expected, result.toString());
    }

    @Test
    final void typeOf_foreign_RecordComponent() {
        final var type = new Type<TORC_Case>() {};
        final RecordComponent component = new TORC_Case(2, null).component();
        assertThrows(IllegalArgumentException.class,
                     () -> type.typeOf(component)); //.printStackTrace();
    }

    @Test
    final void returnTypeOf() throws NoSuchMethodException {
        final Method method = SuperTypeOf.class.getDeclaredMethod("getField");

        final Type<TypeOf<String>> typeOfStringType = new Type<>() {};
        assertEquals(STRING, typeOfStringType.returnTypeOf(method));

        final Type<TypeOf<List<String>>> typeOfListType = new Type<>() {};
        assertEquals(LIST, typeOfListType.returnTypeOf(method));
    }

    @Test
    final void parameterTypesOf() throws NoSuchMethodException {
        final Method method = SuperTypeOf.class.getDeclaredMethod("getField");
        final Type<TypeOf<String>> typeOfStringType = new Type<>() {};
        assertEquals(emptyList(), typeOfStringType.parameterTypesOf(method));
    }

    @Test
    final void exceptionTypesOf() throws NoSuchMethodException {
        final Method method = SuperTypeOf.class.getDeclaredMethod("getField");
        final Type<TypeOf<List<String>>> typeOfListType = new Type<>() {};
        assertEquals(emptyList(), typeOfListType.exceptionTypesOf(method));
    }

    @Test
    final void actualParameters() {
        assertEquals(emptyList(), Type.of(Map.class).actualParameters());
        assertEquals(emptyList(), STRING.actualParameters());
        assertEquals(Arrays.asList(STRING, LIST), MAP.actualParameters());
    }

    @ParameterizedTest
    @MethodSource("assignableCases")
    final void isAssignableFrom(final AssignableCase given) {
        assertEquals(given.expected, given.left.isAssignableFrom(given.right));
    }

    @ParameterizedTest
    @MethodSource("recordComponentCases")
    final void typeOfRecordComponent(final FieldCase given) {
        final RecordComponent[] components = TypeTestRecord.class.getRecordComponents();
        final RecordComponent component = Stream.of(components)
                                                .filter(c -> c.getName().equals(given.name()))
                                                .findFirst()
                                                .orElseThrow();
        assertEquals(given.expected(), given.context().typeOf(component));
    }

    @SuppressWarnings("AnonymousInnerClassMayBeStatic")
    @Test
    final void testEquals() {
        assertEquals(STRING, new Type<String>() {});
        assertEquals(MAP, new Type<Map<String, List<String>>>() {});
    }

    @SuppressWarnings("AnonymousInnerClassMayBeStatic")
    @Test
    final void testHashCode() {
        assertEquals(STRING.hashCode(), new Type<String>() {}.hashCode());
        assertEquals(MAP.hashCode(), new Type<Map<String, List<String>>>() {}.hashCode());
    }

    @ParameterizedTest
    @EnumSource
    final void testToString(final ToStringCase testCase) {
        assertEquals(testCase.string, testCase.type.toString());
    }

    @SuppressWarnings("InnerClassFieldHidesOuterClassField")
    enum ToStringCase {
        INTEGER(Type.of(Integer.class), "java.lang.Integer", emptyList(), Integer.class),
        STRING(TypeTest.STRING, "java.lang.String", emptyList(), String.class),
        LIST(TypeTest.LIST, "java.util.List<java.lang.String>", singletonList("E"), List.class),
        MAP(TypeTest.MAP,
            "java.util.Map<java.lang.String, java.util.List<java.lang.String>>",
            Arrays.asList("K", "V"),
            Map.class),

        INT_ARRAY(Type.of(int[].class), "int[]", singletonList("E"), int[].class),

        INTEGER_ARRAY(Type.of(Integer[].class), "java.lang.Integer[]", singletonList("E"), Integer[].class),

        LIST_ARRAY(new Type<List<String>[]>() {},
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

    @ParameterizedTest
    @EnumSource
    final void formalParameters(final ToStringCase testCase) {
        assertEquals(testCase.formalParameters, testCase.type.formalParameters());
    }

    @SuppressWarnings({"AssignmentOrReturnOfFieldWithMutableType", "WeakerAccess"})
    record SampleRecord<E, F, G>(String name, E element, List<F> list, Map<E, List<G>> map) {}

    @SuppressWarnings("MethodMayBeStatic")
    record TORC_Case(int index, String expected) {

        final Type<?> type() {
            return new Type<SampleRecord<String, Double, Instant>>() {};
        }

        final RecordComponent component() {
            return SampleRecord.class.getRecordComponents()[index];
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
    final void typeOf_Field() throws NoSuchFieldException {
        final Field field = SuperTypeOf.class.getDeclaredField("field");

        final Type<TypeOf<String>> typeOfStringType = new Type<>() {};
        assertEquals(STRING, typeOfStringType.typeOf(field));

        final Type<TypeOf<List<String>>> typeOfListType = new Type<>() {};
        assertEquals(LIST, typeOfListType.typeOf(field));

        //noinspection rawtypes
        final Type<TypeOf> typeOfRawType = new Type<>() {};
        assertEquals(Type.of(Object.class), typeOfRawType.typeOf(field));
    }

    @ParameterizedTest
    @MethodSource("fieldCases")
    final void typeOfField(final FieldCase given) throws NoSuchFieldException {
        final Field field = TypeTestSubject.class.getDeclaredField(given.name());
        assertEquals(given.expected(), given.context().typeOf(field));
    }

    static class StringList extends ArrayList<String> {}

    record AssignableCase(Type<?> left, Type<?> right, boolean expected) {

        @Override
        public String toString() {
            return "%s <- %s (%s)".formatted(left, right, expected);
        }
    }

    record FieldCase(String name, Type<?> context, Type<?> expected) {}
}
