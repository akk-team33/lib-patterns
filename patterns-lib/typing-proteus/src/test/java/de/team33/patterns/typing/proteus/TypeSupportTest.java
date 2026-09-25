package de.team33.patterns.typing.proteus;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TypeSupportTest {

    private static final TypeSupport INT =
            ClassCase.support(int.class);
    private static final TypeSupport INT_ARRAY =
            ClassCase.support(int[].class);
    private static final TypeSupport INT_BI_ARRAY =
            ClassCase.support(int[][].class);
    private static final TypeSupport STRING =
            ClassCase.support(String.class);
    private static final TypeSupport STRING_ARRAY =
            ClassCase.support(String[].class);
    private static final TypeSupport RAW_LIST =
            ClassCase.support(List.class);
    private static final TypeSupport LIST_OF_STRING =
            ClassCase.support(StringList.class)
                     .memberSupport(StringList.class.getGenericInterfaces()[0]);
    private static final TypeSupport MAP_OF_STRING_TO_LIST_OF_STRING =
            ClassCase.support(StringListMap.class)
                     .memberSupport(StringListMap.class.getGenericInterfaces()[0]);

    static Stream<ActualParameterCase> actualParameterCases() {
        return Stream.of(new ActualParameterCase(INT, "E", null),
                         new ActualParameterCase(INT_ARRAY, "E", INT),
                         new ActualParameterCase(INT_ARRAY, "X", null),
                         new ActualParameterCase(INT_BI_ARRAY, "E", INT_ARRAY),
                         new ActualParameterCase(STRING, "E", null),
                         new ActualParameterCase(STRING_ARRAY, "E", STRING),
                         new ActualParameterCase(STRING_ARRAY, "Y", null),
                         new ActualParameterCase(RAW_LIST, "E", null),
                         new ActualParameterCase(LIST_OF_STRING, "E", STRING),
                         new ActualParameterCase(MAP_OF_STRING_TO_LIST_OF_STRING, "K", STRING),
                         new ActualParameterCase(MAP_OF_STRING_TO_LIST_OF_STRING, "V", LIST_OF_STRING),
                         new ActualParameterCase(MAP_OF_STRING_TO_LIST_OF_STRING, "E", null));
    }

    static Stream<EqualsCase> equalsCases() {
        return Stream.of(new EqualsCase(STRING, STRING, true),
                         new EqualsCase(STRING, LIST_OF_STRING, false),
                         new EqualsCase(LIST_OF_STRING,
                                        MAP_OF_STRING_TO_LIST_OF_STRING.actualParameter("V"), true),
                         new EqualsCase(INT, INT_ARRAY, false),
                         new EqualsCase(INT, INT_ARRAY.actualParameter("E"), true));
    }

    @ParameterizedTest
    @MethodSource("actualParameterCases")
    final void actualParameter(final ActualParameterCase given) {
        try {
            final TypeSupport result = given.support.actualParameter(given.parameter);
            assertEquals(given.expected, result);
        } catch (final IllegalArgumentException e) {
            // e.printStackTrace();
            assertNull(given.expected);
            assertEquals("formal parameter <%s> not found in %s".formatted(given.parameter,
                                                                           given.support.formalParameters()),
                         e.getMessage());
        } catch (final IllegalStateException e) {
            // e.printStackTrace();
            assertNull(given.expected);
            assertEquals("actual parameter for <%s> not found in %s".formatted(given.parameter,
                                                                               given.support.actualParameters()),
                         e.getMessage());
        }
    }

    @SuppressWarnings({"MethodOverloadsMethodOfSuperclass", "CovariantEquals"})
    @ParameterizedTest
    @MethodSource("equalsCases")
    final void equals(final EqualsCase given) {
        if (given.expected) {
            assertEquals(given.left, given.right);
        } else {
            assertNotEquals(given.left, given.right);
        }
    }

    @ParameterizedTest
    @MethodSource("equalsCases")
    final void hashCode(final EqualsCase given) {
        if (given.left.equals(given.right)) {
            assertEquals(given.left.hashCode(), given.right.hashCode());
        }
        if (given.left.hashCode() != given.right.hashCode()) {
            assertNotEquals(given.left, given.right);
        }
    }

    @SuppressWarnings("InterfaceNeverImplemented")
    interface StringList extends List<String> {}

    @SuppressWarnings("InterfaceNeverImplemented")
    interface StringListMap extends Map<String, List<String>> {}

    record ActualParameterCase(TypeSupport support, String parameter, TypeSupport expected) {}

    record EqualsCase(TypeSupport left, TypeSupport right, boolean expected) {}
}
