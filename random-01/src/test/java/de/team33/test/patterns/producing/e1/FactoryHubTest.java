package de.team33.test.patterns.producing.e1;

import de.team33.patterns.producing.e1.FactoryHub;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;
import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class FactoryHubTest {

    private static final Random OUTER_RANDOM = new Random();
    private static final Object UNKNOWN = new Object();
    private static final Integer INTEGER = OUTER_RANDOM.nextInt();
    private static final Date DATE = new Date(OUTER_RANDOM.nextInt());
    private static final Double DOUBLE = OUTER_RANDOM.nextDouble();
    private static final BigInteger BIG_INTEGER = BigInteger.valueOf(OUTER_RANDOM.nextInt());

    private final Random random = new Random();
    private final FactoryHub<FactoryHubTest> factoryHub;

    FactoryHubTest() {
        final FactoryHub.Collector<FactoryHubTest> collector = new FactoryHub.Collector<FactoryHubTest>()
                .on(INTEGER).apply(ctx -> INTEGER)
                .on(DATE).apply(ctx -> DATE)
                .on(DOUBLE).apply(ctx -> ctx.random.nextDouble())
                .on(BIG_INTEGER).apply(ctx -> new BigInteger(128, ctx.random));
        factoryHub = new FactoryHub<>(collector, () -> this);
    }

    @ParameterizedTest
    @ValueSource(classes = {StringHub.class, EmptyHubHub.class})
    final void illegalDerivation(final Class<?> hubClass) {
        assertThrows(IllegalArgumentException.class, hubClass::newInstance);
    }

    @Test
    final void illegalTemplate() {
        assertThrows(IllegalArgumentException.class, () -> new EmptyHub().create(UNKNOWN));
    }

    @Test
    final void nullTemplate() {
        assertNull(new EmptyHub().create(null));
    }

    @Test
    final void create_fixed() {
        assertEquals(INTEGER, factoryHub.create(INTEGER));
        assertEquals(DATE, factoryHub.create(DATE));
    }

    @Test
    final void create_random() {
        final Double aDouble = factoryHub.create(DOUBLE);
        assertInstanceOf(Double.class, aDouble);
        assertNotEquals(DOUBLE, aDouble);

        final BigInteger bigInteger = factoryHub.create(BIG_INTEGER);
        assertInstanceOf(BigInteger.class, bigInteger);
        assertNotEquals(BIG_INTEGER, bigInteger);
    }

    static class EmptyHub extends FactoryHub<EmptyHub> {

        EmptyHub() {
            super(new FactoryHub.Collector<>(), EmptyHub.class);
        }
    }

    static class StringHub extends FactoryHub<String> {

        StringHub() {
            super(new FactoryHub.Collector<>(), String.class); // <- illegal!
        }
    }

    static class EmptyHubHub extends FactoryHub<EmptyHub> {

        EmptyHubHub() {
            super(new FactoryHub.Collector<>(), EmptyHub.class); // <- illegal!
        }
    }
}
