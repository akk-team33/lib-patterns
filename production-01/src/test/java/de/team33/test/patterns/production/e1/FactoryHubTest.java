package de.team33.test.patterns.production.e1;

import de.team33.patterns.production.e1.FactoryHub;
import de.team33.test.patterns.production.shared.Complex;
import de.team33.test.patterns.production.shared.Mappable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class FactoryHubTest {

    private static final Random OUTER_RANDOM = new Random();
    private static final Object UNKNOWN = new Object();
    private static final Integer INTEGER = OUTER_RANDOM.nextInt();
    private static final Date DATE = new Date(OUTER_RANDOM.nextInt());
    private static final Double DOUBLE = OUTER_RANDOM.nextDouble();
    private static final BigInteger BIG_INTEGER = BigInteger.valueOf(OUTER_RANDOM.nextInt());
    private static final String STRING = "any:String:" + OUTER_RANDOM.nextInt();
    private static final List<Integer> INT_LIST = Collections.singletonList(INTEGER);
    private static final Set<Integer> INT_SET = Collections.singleton(INTEGER);
    private static final Map<Integer, Set<Integer>> INT_MAP = Collections.singletonMap(INTEGER, INT_SET);
    private static final Complex<Integer> COMPLEX = new Complex.Builder<Integer>()
            .collect(collector -> collector.setIntValue(INTEGER)
                                           .setStringValue(STRING))
            .setSimple(INTEGER)
            .setList(INT_LIST)
            .setMap(INT_MAP)
            .build();
    private static final Mappable<Integer> MAPPABLE = new Mappable<Integer>().setSimple(INTEGER)
                                                                             .setList(INT_LIST)
                                                                             .setMap(INT_MAP);
    private static final Map<String, Object> MAPPABLE_MAP = MAPPABLE.toMap();

    private final Random random = new Random();
    private final FactoryHub<FactoryHubTest> factoryHub;

    FactoryHubTest() {
        final FactoryHub.Collector<FactoryHubTest> collector = new FactoryHub.Collector<FactoryHubTest>()
                .on(INTEGER).apply(ctx -> INTEGER)
                .on(DATE).apply(ctx -> DATE)
                .on(STRING).apply(ctx -> STRING)
                .on(INT_LIST).apply(ctx -> new LinkedList<>(INT_LIST))
                .on(INT_MAP).apply(ctx -> new TreeMap<>(INT_MAP))
                .on(DOUBLE).apply(ctx -> ctx.random.nextDouble())
                .on(BIG_INTEGER).apply(ctx -> new BigInteger(128, ctx.random))
                .on(MAPPABLE).apply(ctx -> new Mappable<>(ctx.factoryHub.map(MAPPABLE.toMap())));
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

    @Test
    final void map() {
        final Map<String, Object> result = factoryHub.map(MAPPABLE_MAP);
        assertNotSame(MAPPABLE_MAP, result);
        assertEquals(MAPPABLE_MAP, result);
    }

    @Test
    final void map_indirect() {
        final Mappable<Integer> result = factoryHub.create(MAPPABLE);
        assertNotSame(MAPPABLE, result);
        assertEquals(MAPPABLE, result);
    }

    @Test
    final void byFieldsOf_COMPLEX() {
        final Complex<Integer> result = factoryHub.byFieldsOf(COMPLEX).init(Complex.empty());
        assertEquals(COMPLEX, result);
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
