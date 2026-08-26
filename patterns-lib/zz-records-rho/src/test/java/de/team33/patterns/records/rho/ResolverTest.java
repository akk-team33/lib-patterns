package de.team33.patterns.records.rho;

import de.team33.patterns.typing.proteus.Type;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResolverTest extends TritonTestBase {

    private static final char[] EMPTY_CHAR_ARRAY = {};
    private static final Character[] EMPTY_CHARACTER_ARRAY = {};
    private static final String A_STRING = UUID.randomUUID().toString();
    private static final Instant AN_INSTANT = Instant.now();

    static Stream<MapCase<?>> parseCases() {
        return Stream.of(mapCase(boolean.class, "true", true),
                         mapCase(Boolean.class, "false", false),
                         mapCase(byte.class, "12", (byte) 12),
                         mapCase(Byte.class, "-3", (byte) -3),
                         mapCase(short.class, "-357", (short) -357),
                         mapCase(Short.class, "5791", (short) 5791),
                         mapCase(int.class, "-1357924", -1357924),
                         mapCase(Integer.class, "246801", 246801),
                         mapCase(long.class, "1357924680", 1357924680L),
                         mapCase(Long.class, "-9753108642", -9753108642L),
                         mapCase(float.class, "-1.414", -1.414f),
                         mapCase(Float.class, "1.414", 1.414f),
                         mapCase(double.class, "3.141592654", 3.141592654),
                         mapCase(Double.class, "-3.141592654", -3.141592654),
                         mapCase(char.class, "\"a\"", 'a'),
                         mapCase(Character.class, "\"b\"", 'b'),
                         mapCase(BigInteger.class, "-9753108642", BigInteger.valueOf(-9753108642L)),
                         mapCase(BigDecimal.class, "3.141592654", new BigDecimal("3.141592654")),
                         mapCase(String.class, "\"any string\"", "any string"),
                         mapCase(char[].class, "[]", EMPTY_CHAR_ARRAY),
                         mapCase(Character[].class, "[]", EMPTY_CHARACTER_ARRAY),
                         mapCase(String[].class, "[\"any string\"]", new String[]{"any string"}),
                         mapCase(int[].class, "[1,2,3]", new int[]{1, 2, 3}),
                         mapCase(Integer[].class, "[4,2,9]", new Integer[]{4, 2, 9}),
                //new MapCase<>(new Type<>() {}, "[4,2,9]", List.of(4, 2, 9)),
                         mapCase(SampleRecord.class, "{}", new SampleRecord(null, null, null)),
                         mapCase(SampleRecord.class,
                                 "{\"lValue\" :null,\"name\": null, \"eValue\" : null}",
                                 new SampleRecord(null, null, null)),
                         mapCase(SampleRecord.class,
                                 "{\"name\":\"my name\"}",
                                 new SampleRecord("my name", null, null)),
                         mapCase(PrimeSample.class,
                                 "{\"unknown\" : 17, \"lValue\" : 9753108642, \"sValue\" : 278}",
                                 new PrimeSample(null, 9753108642L, (short) 278)),
                         mapCase(NestedSample.class, """
                                         {
                                             "plain" : {
                                                 "lValue" : 9753108642,
                                                 "name"   : "name",
                                                 "eValue" : "V3"
                                             },
                                             "lValue" : 278
                                         }""",
                                 new NestedSample(new SampleRecord("name", 9753108642L, EnumSample.V3), 278L)),
                         new MapCase<>(new Type<GenericSample<String, Instant, Class<? extends Exception>>>() {},
                                       ("{" +
                                        "    \"tValue\" : \"%s\"," +
                                        "    \"uValue\" : \"%s\"," +
                                        "    \"vValue\" : \"%s\"" +
                                        "}").formatted(A_STRING,
                                                       AN_INSTANT,
                                                       IllegalArgumentException.class.getName()),
                                       new GenericSample<>(A_STRING, AN_INSTANT, IllegalArgumentException.class)));
    }

    private static <T> MapCase<T> mapCase(final Class<T> type, final String source, final T expected) {
        return new MapCase<>(Type.of(type), source, expected);
    }

    static Stream<FailCase> failCases() {
        return Stream.of(failCase(boolean.class, "tru", IllegalArgumentException.class),
                         failCase(byte.class, "250", ArithmeticException.class), // TODO: IllegalArgumentException
                         failCase(short.class, "65000", ArithmeticException.class), // TODO: IllegalArgumentException
                         failCase(PrimeSample.class, "{}", IllegalArgumentException.class),
                         failCase(List.class, "{}", IllegalArgumentException.class),
                         failCase(char.class, "\"\"", IllegalArgumentException.class),
                         failCase(char.class, "\"ab\"", IllegalArgumentException.class),
                         failCase(EnumSample.class, "\"V0\"", IllegalStateException.class), // TODO: IllegalArgumentException
                         failCase(PrimeSample.class,
                                  "{\"lValue\" :null,\"name\": null, \"sValue\" : null}",
                                  IllegalArgumentException.class),
                         failCase(SampleRecord.class,
                                  "{\"name\":25}",
                                  IllegalArgumentException.class),
                         failCase(SampleRecord.class,
                                  "{\"lValue\" : \"9753108642\", \"sValue\" : \"V2\"}",
                                  IllegalArgumentException.class));
    }

    private static FailCase failCase(final Class<?> type, final String source, final Class<? extends Exception> expected) {
        return new FailCase(Type.of(type), source, expected);
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
    final <T> void parse(final MapCase<T> given) {
        final Object result = Resolver.resolve(given.targetType, given.value());
        if (given.targetType.core().isArray()) {
            assertEquals(toList(given.expected), toList(result));
        } else {
            assertEquals(given.expected, result);
        }
    }

    @ParameterizedTest
    @MethodSource("failCases")
    final void parse_fail(final FailCase given) {
        // final Exception e =
        assertThrows(given.expected, () -> Resolver.resolve(given.type, given.value()));
        // e.printStackTrace();
    }

    record MapCase<T>(Type<T> targetType, String source, T expected) {

        final JsonValue value() {
            return Parser.parse(source);
        }

        @Override
        public String toString() {
            return "%s: %s".formatted(targetType.toString(), expected);
        }
    }

    record FailCase(Type<?> type, String source, Class<? extends Exception> expected) {

        final JsonValue value() {
            return Parser.parse(source);
        }
    }

    private record PrimeSample(String name, long lValue, short sValue) {
    }

    private record NestedSample(SampleRecord plain, Long lValue) {
    }

    private record GenericSample<T, U, V>(T tValue, U uValue, V vValue) {
    }
}