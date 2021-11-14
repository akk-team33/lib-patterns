package de.team33.test.patterns.random.e1;

import de.team33.patterns.random.e1.RandomHub;
import de.team33.test.patterns.random.shared.Buildable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;
import java.util.IntSummaryStatistics;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RandomHubTest {

    private static final Buildable BUILDABLE = Buildable.builder()
                                                        .setIntValue(RandomHub.INTEGER)
                                                        .setLongValue(RandomHub.LONG)
                                                        .setStringValue(RandomHub.STRING)
                                                        .build();

    private final RandomHub defaultHub = RandomHub.builder()
                                                  .on(BUILDABLE).apply(rnd -> Buildable.builder()
                                                                                       .setIntValue(rnd.any(BUILDABLE.getIntValue()))
                                                                                       .setLongValue(rnd.any(BUILDABLE.getLongValue()))
                                                                                       .setStringValue(rnd.any(BUILDABLE.getStringValue()))
                                                                                       .build())
                                                  .build();

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
}