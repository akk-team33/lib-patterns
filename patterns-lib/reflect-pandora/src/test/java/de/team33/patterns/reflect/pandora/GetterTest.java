package de.team33.patterns.reflect.pandora;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class GetterTest {

    @Test
    final void apply_IllegalAccessException() throws NoSuchMethodException {
        final String name = "writeReplace";
        final Getter<Instant> getter = new Getter<>(Instant.class.getDeclaredMethod(name), name);
        final Instant target = Instant.now();

        try {
            final Object result = getter.apply(target);
            fail("expected to fail - but was " + result);
        } catch (final IllegalStateException e) {
            // as expected!
            // e.printStackTrace();
            assertInstanceOf(IllegalAccessException.class, e.getCause());
            assertTrue(e.getMessage().contains(name));
        }
    }

    @Test
    final void apply_InvocationTargetException() throws NoSuchMethodException {
        final String name = "getLeadsToInvocationTargetException";
        final Getter<GetterTest> getter = new Getter<>(getClass().getDeclaredMethod(name), name);

        try {
            final Object result = getter.apply(this);
            fail("expected to fail - but was " + result);
        } catch (final IllegalStateException e) {
            // as expected!
            // e.printStackTrace();
            assertInstanceOf(InvocationTargetException.class, e.getCause());
            assertTrue(e.getMessage().contains(name));
        }
    }

    final Object getLeadsToInvocationTargetException() {
        throw new UnsupportedOperationException("should lead to an InvocationTargetException");
    }
}
