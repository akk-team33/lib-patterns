package de.team33.patterns.notes.eris.publics;

import de.team33.patterns.notes.eris.Audience;
import de.team33.patterns.reflect.luna.Fields;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AudienceTest {

    private static final Random RANDOM = new Random();

    private final Data data = new Data(RANDOM.nextInt(),
                                       RANDOM.nextDouble(),
                                       Instant.now().plusMillis(RANDOM.nextInt()));
    private final Audience audience = new Audience();

    @Test
    final void addListener_and_fire() {
        final Data listened = new Data();
        audience.add(Channel.SET_INTEGER, value -> listened.intValue = value);
        audience.add(Channel.SET_DOUBLE, value -> listened.doubleValue = value);
        audience.add(Channel.SET_INSTANT, value -> listened.instantValue = value);

        audience.send(Channel.SET_INTEGER, data.intValue);
        audience.send(Channel.SET_DOUBLE, data.doubleValue);
        audience.send(Channel.SET_INSTANT, data.instantValue);

        assertEquals(data, listened);
    }

    @Test
    final void noListener_and_fire() {
        final Data listened = new Data();

        audience.send(Channel.SET_INTEGER, data.intValue);
        audience.send(Channel.SET_DOUBLE, data.doubleValue);
        audience.send(Channel.SET_INSTANT, data.instantValue);

        assertNotEquals(data, listened);
    }

    static class Data {

        private static final Fields FIELDS = Fields.of(Data.class);

        int intValue;
        double doubleValue;
        Instant instantValue;

        Data() {
        }

        Data(final int intValue, final double doubleValue, final Instant instantValue) {
            this.intValue = intValue;
            this.doubleValue = doubleValue;
            this.instantValue = instantValue;
        }

        private Map<String, Object> toMap() {
            return FIELDS.toMap(field -> field.get(this));
        }

        @Override
        public final boolean equals(final Object obj) {
            return (this == obj) || ((obj instanceof Data) && toMap().equals(((Data) obj).toMap()));
        }

        @Override
        public final int hashCode() {
            return toMap().hashCode();
        }

        @Override
        public final String toString() {
            return toMap().toString();
        }
    }

    @SuppressWarnings("ClassNameSameAsAncestorName")
    @FunctionalInterface
    interface Channel<M> extends de.team33.patterns.notes.eris.Channel<M> {

        Channel<Integer> SET_INTEGER = test -> test.data.intValue;
        Channel<Double> SET_DOUBLE = test -> test.data.doubleValue;
        Channel<Instant> SET_INSTANT = test -> test.data.instantValue;

        M getMessage(AudienceTest source);
    }
}
