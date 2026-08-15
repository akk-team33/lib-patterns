package de.team33.patterns.records.triton;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResolverTest {

    private static final char[] EMPTY_CHAR_ARRAY = {};
    private static final Character[] EMPTY_CHARACTER_ARRAY = {};

    static Stream<MapCase> parseCases() {
        return Stream.of(new MapCase(boolean.class, "true", true),
                         new MapCase(Boolean.class, "false", false),
                         new MapCase(byte.class, "12", (byte) 12),
                         new MapCase(Byte.class, "-3", (byte) -3),
                         new MapCase(short.class, "-357", (short) -357),
                         new MapCase(Short.class, "5791", (short) 5791),
                         new MapCase(int.class, "-1357924", -1357924),
                         new MapCase(Integer.class, "246801", 246801),
                         new MapCase(long.class, "1357924680", 1357924680L),
                         new MapCase(Long.class, "-9753108642", -9753108642L),
                         new MapCase(float.class, "-1.414", -1.414f),
                         new MapCase(Float.class, "1.414", 1.414f),
                         new MapCase(double.class, "3.141592654", 3.141592654),
                         new MapCase(Double.class, "-3.141592654", -3.141592654),
                         new MapCase(char.class, "\"a\"", 'a'),
                         new MapCase(Character.class, "\"b\"", 'b'),
                         new MapCase(BigInteger.class, "-9753108642", BigInteger.valueOf(-9753108642L)),
                         new MapCase(BigDecimal.class, "3.141592654", new BigDecimal("3.141592654")),
                         new MapCase(String.class, "\"any string\"", "any string"),
                         new MapCase(char[].class, "[]", EMPTY_CHAR_ARRAY),
                         new MapCase(Character[].class, "[]", EMPTY_CHARACTER_ARRAY),
                         new MapCase(String[].class, "[\"any string\"]", new String[]{"any string"}),
                         new MapCase(int[].class, "[1,2,3]", new int[]{1, 2, 3}),
                         new MapCase(Integer[].class, "[4,2,9]", new Integer[]{4, 2, 9}),
                         new MapCase(SampleRecord.class, "{}", new SampleRecord(null, null, null)),
                         new MapCase(SampleRecord.class,
                                     "{\"lValue\" :null,\"name\": null, \"eValue\" : null}",
                                     new SampleRecord(null, null, null)),
                         new MapCase(SampleRecord.class,
                                     "{\"name\":\"my name\"}",
                                     new SampleRecord("my name", null, null)),
                         new MapCase(PrimeSample.class,
                                     "{\"unknown\" : 17, \"lValue\" : 9753108642, \"sValue\" : 278}",
                                     new PrimeSample(null, 9753108642L, (short) 278)),
                         new MapCase(NestedSample.class, """
                                 {
                                     "plain" : {
                                         "lValue" : 9753108642,
                                         "name"   : "name",
                                         "eValue" : "V3"
                                     },
                                     "lValue" : 278
                                 }""",
                                     new NestedSample(new SampleRecord("name", 9753108642L, EnumSample.V3), 278L)));
    }

    static Stream<FailCase> failCases() {
        return Stream.of(new FailCase(boolean.class, "tru", IllegalArgumentException.class),
                         new FailCase(byte.class, "250", ArithmeticException.class), // TODO: IllegalArgumentException
                         new FailCase(short.class, "65000", ArithmeticException.class), // TODO: IllegalArgumentException
                         new FailCase(PrimeSample.class, "{}", IllegalArgumentException.class),
                         new FailCase(List.class, "{}", IllegalArgumentException.class),
                         new FailCase(char.class, "\"\"", IllegalArgumentException.class),
                         new FailCase(char.class, "\"ab\"", IllegalArgumentException.class),
                         new FailCase(EnumSample.class, "\"V0\"", IllegalStateException.class), // TODO: IllegalArgumentException
                         new FailCase(PrimeSample.class,
                                      "{\"lValue\" :null,\"name\": null, \"sValue\" : null}",
                                      IllegalArgumentException.class),
                         new FailCase(SampleRecord.class,
                                      "{\"name\":25}",
                                      IllegalArgumentException.class),
                         new FailCase(SampleRecord.class,
                                      "{\"lValue\" : \"9753108642\", \"sValue\" : \"V2\"}",
                                      IllegalArgumentException.class));
    }

    private static List<Object> toList(final Object array) {
        final int length = Array.getLength(array);
        final List<Object> result = new ArrayList<>(length);
        for (int index = 0; index < length; ++index) {
            result.add(Array.get(array, index));
        }
        return result;
    }

    @ParameterizedTest
    @MethodSource("parseCases")
    final void parse(final MapCase given) {
        final Object result = Resolver.resolve(given.targetClass, given.value());
        if (given.targetClass.isArray()) {
            assertEquals(toList(given.expected), toList(result));
        } else {
            assertEquals(given.expected, result);
        }
    }

    @ParameterizedTest
    @MethodSource("failCases")
    final void parse_fail(final FailCase given) {
        // final Exception e =
        assertThrows(given.expected, () -> Resolver.resolve(given.rClass, given.value()));
        // e.printStackTrace();
    }

    record MapCase(Class<?> targetClass, String source, Object expected) {

        final JsonValue value() {
            return Parser.parse(source);
        }
    }

    record FailCase(Class<?> rClass, String source, Class<? extends Exception> expected) {

        final JsonValue value() {
            return Parser.parse(source);
        }
    }

    private record PrimeSample(String name, long lValue, short sValue) {
    }

    private record NestedSample(SampleRecord plain, Long lValue) {
    }
}