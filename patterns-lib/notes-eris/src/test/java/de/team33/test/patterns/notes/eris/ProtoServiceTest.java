package de.team33.test.patterns.notes.eris;

import de.team33.patterns.arbitrary.mimas.Generator;
import de.team33.patterns.notes.eris.Audience;
import de.team33.patterns.notes.eris.ProtoService;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Deprecated
@SuppressWarnings("WeakerAccess")
class ProtoServiceTest extends ProtoService<ProtoServiceTest> {

    private static final Random RANDOM = new SecureRandom();
    private static final Generator GENERATOR = numBits -> new BigInteger(numBits, RANDOM);
    private int intValue;
    private String stringValue;
    private Instant instantValue;
    ProtoServiceTest() {
        super(new Audience(), ProtoServiceTest.class);
    }

    @Test
    final void register_and_fire() {
        final Mutable<Integer> intMutable = new Mutable<>();
        final Mutable<String> stringMutable = new Mutable<>();
        final Mutable<Instant> instantMutable = new Mutable<>();

        registry().add(Channel.SET_INTEGER, intMutable::setValue);
        registry().add(Channel.SET_STRING, stringMutable::setValue);
        registry().add(Channel.SET_INSTANT, instantMutable::setValue);

        intValue = GENERATOR.anyInt();
        stringValue = GENERATOR.anyString(16, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        instantValue = Instant.now().plusSeconds(GENERATOR.anyInt());

        fire(Channel.SET_INTEGER, Channel.SET_STRING, Channel.SET_INSTANT);

        assertEquals(intValue, intMutable.getValue());
        assertEquals(stringValue, stringMutable.getValue());
        assertEquals(instantValue, instantMutable.getValue());
    }

    @FunctionalInterface
    interface Channel<M> extends ProtoService.Channel<ProtoServiceTest, M> {

        Channel<Integer> SET_INTEGER = service -> service.intValue;
        Channel<String> SET_STRING = service -> service.stringValue;
        Channel<Instant> SET_INSTANT = service -> service.instantValue;
    }

    @SuppressWarnings("UnusedReturnValue")
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
}
