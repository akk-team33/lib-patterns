package de.team33.test.patterns.reflect.luna;

import de.team33.patterns.random.tarvos.Generator;
import de.team33.patterns.reflect.luna.Fields;
import de.team33.sample.patterns.reflect.luna.Simple;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SimpleTest extends Random implements Generator {

    private static final Fields FOREIGN_FIELDS = Fields.of(Simple.class);

    @Override
    public final BigInteger nextBits(final int numBits) {
        return new BigInteger(numBits, this);
    }

    private Simple nextSimple() {
        return new Simple().setIntValue(nextInt())
                           .setDoubleValue(nextDouble())
                           .setInstantValue(
                                   Instant.now()
                                          .plusSeconds(nextLong(-100000L, 100000L)))
                           .setStringValue(
                                   nextString(nextInt(0, 24), "abc"));
    }

    @Test
    final void forEach_failing() {
        final Simple source = nextSimple();
        final Simple result = new Simple();
        assertThrows(Fields.AccessException.class,
                     () -> FOREIGN_FIELDS.forEach(field -> field.set(result, field.get(source))));
    }

    @Test
    final void readAccess_failing() {
        final Simple origin = nextSimple();
        assertThrows(Fields.AccessException.class, () -> FOREIGN_FIELDS.map(field -> field.get(origin)).count());
    }

    @Test
    final void copy() {
        final Simple expected = nextSimple();
        final Simple result = new Simple(expected);
        assertEquals(expected, result);
    }

    @Test
    final void toList() {
        final Simple origin = nextSimple();
        final List<Object> expected = Arrays.asList(origin.getIntValue(),
                                                    origin.getDoubleValue(),
                                                    origin.getInstantValue(),
                                                    origin.getStringValue());
        final List<Object> result = new Simple(origin).toList();
        assertEquals(expected, result);
    }

    @Test
    final void toMap() {
        final Simple origin = nextSimple();
        final Map<String, Object> expected = new TreeMap<>();
        expected.put("intValue", origin.getIntValue());
        expected.put("doubleValue", origin.getDoubleValue());
        expected.put("instantValue", origin.getInstantValue());
        expected.put("stringValue", origin.getStringValue());
        final Map<String, Object> result = new Simple(origin).toMap();
        assertEquals(expected, result);
    }
}
