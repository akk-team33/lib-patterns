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

    static Stream<Case<?>> cases() {
        return Stream.of(new Case<>(STRING, Set.of(Type.of(Object.class),
                                                   Type.of(Serializable.class),
                                                   new Type<Comparable<String>>() {},
                                                   Type.of(CharSequence.class),
                                                   Type.of(Constable.class),
                                                   Type.of(ConstantDesc.class))),
                         new Case<>(MAP, Set.of()));
    }

    static Stream<TORC_Case> torc_Cases() {
        return Stream.of(new TORC_Case(0, "java.lang.String"),
                         new TORC_Case(1, "java.lang.String"),
                         new TORC_Case(2, "java.util.List<java.lang.Double>"),
                         new TORC_Case(3, "java.util.Map<java.lang.String, java.util.List<java.time.Instant>>"));
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
        assertEquals(emptyList(), STRING.actualParameters());
        assertEquals(Arrays.asList(STRING, LIST), MAP.actualParameters());
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

    @Test
    final void typeOf_Field() throws NoSuchFieldException {
        final Field field = SuperTypeOf.class.getDeclaredField("field");

        final Type<TypeOf<String>> typeOfStringType = new Type<>() {};
        assertEquals(STRING, typeOfStringType.typeOf(field));

        final Type<TypeOf<List<String>>> typeOfListType = new Type<>() {};
        assertEquals(LIST, typeOfListType.typeOf(field));
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
                   List[].class),

        LIST_OF_CLASS_BYTE(new Type<List<Class<Byte>>>() {},
                           "java.util.List<java.lang.Class<java.lang.Byte>>",
                           List.of("E"), List.class),
        LIST_OF_CLASS_RAW_(new Type<List<Class>>() {},
                           "java.util.List<java.lang.Class>",
                           List.of("E"), List.class),
        LIST_OF_CLASS_ANY_(new Type<List<Class<?>>>() {},
                           "java.util.List<java.lang.Class<?>>",
                           List.of("E"), List.class),
        LIST_OF_CLASS_EXT_(new Type<List<Class<? extends IllegalStateException>>>() {},
                           "java.util.List<java.lang.Class<?>>",
                           List.of("E"), List.class),
        LIST_OF_CLASS_SUP_(new Type<List<Class<? super IllegalStateException>>>() {},
                           "java.util.List<java.lang.Class<?>>",
                           List.of("E"), List.class);

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
}
