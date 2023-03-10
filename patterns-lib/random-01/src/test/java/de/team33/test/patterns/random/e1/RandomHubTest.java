package de.team33.test.patterns.random.e1;

import de.team33.patterns.production.e1.FactoryUtil;
import de.team33.patterns.random.e1.RandomHub;
import de.team33.test.patterns.random.shared.Buildable;
import org.junit.jupiter.api.Test;

import java.util.IntSummaryStatistics;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class RandomHubTest {

    private static final Buildable BUILDABLE = Buildable.builder()
                                                        .setIntValue(RandomHub.INTEGER)
                                                        .setLongValue(RandomHub.LONG)
                                                        .setStringValue(RandomHub.STRING)
                                                        .build();
    private static final String KEY_TOKEN = "key token";
    private static final Double VALUE_TOKEN = 3.141592654;

    private final RandomHub defaultHub;

    RandomHubTest() {
        defaultHub = RandomHub.builder()
                              .on(KEY_TOKEN).apply(rnd -> KEY_TOKEN + rnd.any(RandomHub.INTEGER))
                              .on(VALUE_TOKEN).apply(rnd -> 3.0 + rnd.anyDouble())
                              .on(BUILDABLE).apply(rnd -> Buildable.builder()
                                                                   .setIntValue(rnd.any(BUILDABLE.getIntValue()))
                                                                   .setLongValue(rnd.any(BUILDABLE.getLongValue()))
                                                                   .setStringValue(rnd.any(BUILDABLE.getStringValue()))
                                                                   .build())
                              .setUnknownTokenListener(FactoryUtil.LOG_UNKNOWN_TOKEN)
                              .build();
    }

    @Test
    final void any_Buildable() {
        final Buildable result = defaultHub.any(BUILDABLE);
        assertInstanceOf(Buildable.class, result);
        assertInstanceOf(Integer.class, result.getIntValue());
        assertInstanceOf(Long.class, result.getLongValue());
        assertInstanceOf(String.class, result.getStringValue());
    }

    @Test
    final void any() {
        assertInstanceOf(Boolean.class, defaultHub.any(false));
        assertInstanceOf(Boolean.class, defaultHub.any(true));
        assertInstanceOf(Byte.class, defaultHub.any(RandomHub.BYTE));
        assertInstanceOf(Short.class, defaultHub.any(RandomHub.SHORT));
        assertInstanceOf(Integer.class, defaultHub.any(RandomHub.INTEGER));
        assertInstanceOf(Long.class, defaultHub.any(RandomHub.LONG));
        assertInstanceOf(Float.class, defaultHub.any(RandomHub.FLOAT));
        assertInstanceOf(Double.class, defaultHub.any(RandomHub.DOUBLE));
        assertInstanceOf(Character.class, defaultHub.any(RandomHub.CHARACTER));
        assertInstanceOf(String.class, defaultHub.any(RandomHub.STRING));
    }

    @Test
    final void stream() {
        defaultHub.stream(RandomHub.INTEGER)
                  .limit(10000)
                  .forEach(value -> assertInstanceOf(Integer.class, value));
    }

    @Test
    final void any_STRING() {
        final int[] counts = new int[24];
        Stream.generate(() -> defaultHub.any(RandomHub.STRING)).limit(1000)
              .peek(System.out::println)
              .forEach(str -> counts[str.length()] += 1);
        final IntSummaryStatistics stats = IntStream.of(counts).summaryStatistics();
        assertEquals(counts[0], stats.getMax());
    }

    @Test
    final void map() {
        final Map<String, Double> result = defaultHub.map(KEY_TOKEN, VALUE_TOKEN, 5);
        assertEquals(5, result.size());
        result.forEach((key, value) -> {
            assertEquals(KEY_TOKEN, key.substring(0, KEY_TOKEN.length()));
            assertEquals(VALUE_TOKEN.intValue(), value.intValue());
        });
    }

    @Test
    void testMap() {
    }

    @Test
    void testMap1() {
    }

    @Test
    void anyBits() {
    }
}
