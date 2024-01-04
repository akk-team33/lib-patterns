package de.team33.patterns.normal.iocaste.test;

import de.team33.patterns.normal.iocaste.Normal;
import de.team33.patterns.normal.iocaste.Normalizer;
import de.team33.patterns.normal.iocaste.testing.Supply;
import de.team33.patterns.random.tarvos.Generator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class NormalizerTest extends Supply {

    private static final Normalizer NORMALIZER = Normalizer.builder()
                                                           .setToSimple(Integer.class, i -> String.format("%,d", i))
                                                           .build();

    private static Normal normal(final Object origin) {
        return NORMALIZER.normal(origin);
    }

    @Test
    final void normal_null() {
        assertNull(normal(null));
    }

    @ParameterizedTest
    @ValueSource(classes = {
            Boolean.class, Byte.class, Short.class, Integer.class, Long.class,
            Float.class, Double.class, Character.class, String.class})
    final <T> void normal_Simple(final Class<T> tClass) {
        final SimpleCase testCase = SimpleCase.of(tClass);
        final T origin = testCase.nextOrigin(this);

        final Normal normal = normal(origin);

        assertEquals(Normal.Type.SIMPLE, normal.type());
        assertTrue(normal.isSimple());
        assertEquals(origin.toString(), normal.asSimple());
    }

    private enum SimpleCase {

        BOOLEAN(Boolean.class, Generator::nextBoolean),
        BYTE(Byte.class, Generator::nextByte),
        SHORT(Short.class, Generator::nextShort),
        INT(Integer.class, Generator::nextInt),
        LONG(Long.class, Generator::nextLong),
        FLOAT(Float.class, Generator::nextFloat),
        DOUBLE(Double.class, Generator::nextDouble),
        CHAR(Character.class, Supply::nextChar),
        STRING(String.class, Supply::nextString);

        private final Class<?> tClass;
        private final Function nextMethod;

        <T> SimpleCase(final Class<T> tClass, final Function<Supply, T> nextMethod) {
            this.tClass = tClass;
            this.nextMethod = nextMethod;
        }

        static SimpleCase of(final Class<?> tClass) {
            return Stream.of(values())
                         .filter(value -> value.tClass.equals(tClass))
                         .findAny()
                         .orElseThrow(() -> new NoSuchElementException("No case found for " + tClass));
        }

        @SuppressWarnings("unchecked")
        <T> T nextOrigin(final Supply supply) {
            return (T) nextMethod.apply(supply);
        }
    }
}