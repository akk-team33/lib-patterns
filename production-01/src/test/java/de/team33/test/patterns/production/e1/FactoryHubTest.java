package de.team33.test.patterns.production.e1;

import de.team33.patterns.production.e1.FactoryHub;
import de.team33.patterns.production.e1.FactoryUtil;
import de.team33.test.patterns.production.shared.Mappable;
import org.junit.jupiter.api.Test;

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

public class FactoryHubTest {

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
    private static final Mappable<Integer> MAPPABLE = new Mappable<Integer>().setSimple(INTEGER)
                                                                             .setList(INT_LIST)
                                                                             .setMap(INT_MAP);

    private final Random random = new Random();
    private final FactoryHub<FactoryHubTest> factoryHub;

    FactoryHubTest() {
        factoryHub = FactoryHub.builder(() -> this)
                               .on(INTEGER).apply(ctx -> INTEGER)
                               .on(DATE).apply(ctx -> DATE)
                               .on(STRING).apply(ctx -> STRING)
                               .on(INT_LIST).apply(ctx -> new LinkedList<>(INT_LIST))
                               .on(INT_MAP).apply(ctx -> new TreeMap<>(INT_MAP))
                               .on(DOUBLE).apply(ctx -> ctx.random.nextDouble())
                               .on(BIG_INTEGER).apply(ctx -> new BigInteger(128, ctx.random))
                               .on(MAPPABLE).apply(ctx -> ctx.factoryHub.map(MAPPABLE, Mappable::toMap,
                                                                             Mappable::new))
                               .setUnknownTokenListener(FactoryUtil.DENY_UNKNOWN_TOKEN)
                               .build();
    }

    @Test
    final void unknownTemplate_denied() {
        assertThrows(IllegalArgumentException.class, () -> factoryHub.get(UNKNOWN));
    }

    @Test
    final void unknownTemplate_accepted() {
        assertEquals(UNKNOWN, new EmptyHub().get(UNKNOWN));
    }

    @Test
    final void nullTemplate() {
        assertNull(new EmptyHub().get(null));
    }

    @Test
    final void another_fixed() {
        assertEquals(INTEGER, factoryHub.get(INTEGER));
        assertEquals(DATE, factoryHub.get(DATE));
        assertEquals(MAPPABLE, factoryHub.get(MAPPABLE));
    }

    @Test
    final void another_random() {
        final Double aDouble = factoryHub.get(DOUBLE);
        assertInstanceOf(Double.class, aDouble);
        assertNotEquals(DOUBLE, aDouble);

        final BigInteger bigInteger = factoryHub.get(BIG_INTEGER);
        assertInstanceOf(BigInteger.class, bigInteger);
        assertNotEquals(BIG_INTEGER, bigInteger);
    }

    @Test
    final void map_k_v_sized() {
        final Map<String, Date> result = factoryHub.map(STRING, DATE, 1);
        assertEquals(Collections.singletonMap(STRING, DATE), result);
    }

    @Test
    final void map_templateMap() {
        final Map<String, Integer> expected = new TreeMap<String, Integer>() {{
            put("a", INTEGER);
            put("b", INTEGER);
            put("c", INTEGER);
        }};
        final Map<String, Integer> result = factoryHub.map(expected);
        assertNotSame(expected, result);
        assertEquals(expected, result);
    }

    @Test
    final void map_Mappable() {
        final Mappable<Integer> result = factoryHub.map(MAPPABLE, Mappable::toMap, Mappable::new);
        assertNotSame(MAPPABLE, result);
        assertEquals(MAPPABLE, result);
    }

    static class EmptyHub extends FactoryHub<EmptyHub> {

        EmptyHub() {
            super(new Collector<EmptyHub, Void>() {
                @Override
                protected Void getBuilder() {
                    return null;
                }
            });
        }

        @Override
        protected final EmptyHub getContext() {
            return this;
        }
    }
}
