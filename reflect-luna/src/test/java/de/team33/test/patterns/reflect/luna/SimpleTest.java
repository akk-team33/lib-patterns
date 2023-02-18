package de.team33.test.patterns.reflect.luna;

import de.team33.patterns.exceptional.e1.XConsumer;
import de.team33.patterns.exceptional.e1.XFunction;
import de.team33.patterns.reflect.luna.Fields;
import de.team33.sample.patterns.reflect.luna.Simple;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SimpleTest {

    private static final Supply SUPPLY = new Supply();

    private static final Fields FOREIGN_FIELDS = Fields.of(Simple.class);

    /**
     * Ensures that accessing foreign, private fields via {@link Fields#forEach(XConsumer)} causes a
     * {@link Fields.AccessException}.
     */
    @Test
    final void forEach_failing() {
        final Simple source = SUPPLY.nextSimple();
        final Simple result = new Simple();
        assertThrows(Fields.AccessException.class,
                     () -> FOREIGN_FIELDS.forEach(field -> field.set(result, field.get(source))));
    }

    /**
     * Ensures that accessing foreign, private fields via {@link Fields#map(XFunction)} causes a
     * {@link Fields.AccessException}.
     */
    @Test
    final void readAccess_failing() {
        final Simple origin = SUPPLY.nextSimple();
        //noinspection ResultOfMethodCallIgnored
        assertThrows(Fields.AccessException.class, () -> FOREIGN_FIELDS.map(field -> field.get(origin)).count());
    }

    @Test
    final void copy() {
        final Simple origin = SUPPLY.nextSimple();
        final Simple result = new Simple(origin);
        assertEquals(origin, result);
    }

    @Test
    final void toList() {
        final Simple origin = SUPPLY.nextSimple();
        final List<Object> expected = Arrays.asList(origin.getIntValue(),
                                                    origin.getDoubleValue(),
                                                    origin.getInstantValue(),
                                                    origin.getStringValue());

        final List<Object> result = new Simple(origin).toList();
        assertEquals(expected, result);
    }

    @Test
    final void toMap() {
        final Simple origin = SUPPLY.nextSimple();
        final Map<String, Object> expected = new TreeMap<>();
        expected.put("intValue", origin.getIntValue());
        expected.put("doubleValue", origin.getDoubleValue());
        expected.put("instantValue", origin.getInstantValue());
        expected.put("stringValue", origin.getStringValue());

        final Map<String, Object> result = new Simple(origin).toMap();
        assertEquals(expected, result);
    }
}
