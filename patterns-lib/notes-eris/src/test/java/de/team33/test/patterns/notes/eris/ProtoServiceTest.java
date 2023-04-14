package de.team33.test.patterns.notes.eris;

import de.team33.patterns.notes.eris.Audience;
import de.team33.patterns.notes.eris.ProtoService;
import de.team33.patterns.random.tarvos.Generator;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProtoServiceTest extends ProtoService<ProtoServiceTest> {

    private static final Random RANDOM = new Random();
    private static final Generator GENERATOR = numBits -> new BigInteger(numBits, RANDOM);

    ProtoServiceTest() {
        super(new Audience(), ProtoServiceTest.class);
    }

    private int intValue;
    private String stringValue;
    private Instant instantValue;

    @Test
    final void register_and_fire() {
        final Mutable<Integer> intMutable = new Mutable<>();
        final Mutable<String> stringMutable = new Mutable<>();
        final Mutable<Instant> instantMutable = new Mutable<>();

        registry().add(Channel.SET_INTEGER, intMutable::setValue);
        registry().add(Channel.SET_STRING, stringMutable::setValue);
        registry().add(Channel.SET_INSTANT, instantMutable::setValue);

        intValue = GENERATOR.nextInt();
        stringValue = GENERATOR.nextString(16, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        instantValue = Instant.now().plusSeconds(GENERATOR.nextInt());

        fire(Channel.SET_INTEGER, Channel.SET_STRING, Channel.SET_INSTANT);

        assertEquals(intValue, intMutable.getValue());
        assertEquals(stringValue, stringMutable.getValue());
        assertEquals(instantValue, instantMutable.getValue());
    }

    private static class Mutable<T> {

        private T value;

        public final T getValue() {
            return value;
        }

        public final Mutable<T> setValue(final T value) {
            this.value = value;
            return this;
        }
    }

    interface Channel<M> extends ProtoService.Channel<ProtoServiceTest, M> {

        Channel<Integer> SET_INTEGER = service -> service.intValue;
        Channel<String> SET_STRING = service -> service.stringValue;
        Channel<Instant> SET_INSTANT = service -> service.instantValue;
    }
}