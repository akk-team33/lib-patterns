package de.team33.patterns.reflect.pandora;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SetterTest {

    @Test
    void accept_IllegalAccessException() throws NoSuchMethodException {
        final String name = "ensureCapacityInternal";
        final Setter<ArrayList<?>> setter = new Setter<>(ArrayList.class.getDeclaredMethod(name, int.class));
        final ArrayList<Object> target = new ArrayList<>();

        try {
            setter.accept(target, 25);
            fail("expected to fail - but was " + target);
        } catch (final IllegalStateException e) {
            // as expected!
            assertInstanceOf(IllegalAccessException.class, e.getCause());
            assertTrue(e.getMessage().contains(name));
        }
    }

    @Test
    void accept_InvocationTargetException() throws NoSuchMethodException {
        final String name = "setLeadsToInvocationTargetException";
        final Setter<SetterTest> setter = new Setter<>(getClass().getDeclaredMethod(name, int.class));

        try {
            setter.accept(this, 25);
            fail("expected to fail - but was " + this);
        } catch (final IllegalStateException e) {
            // as expected!
            assertInstanceOf(InvocationTargetException.class, e.getCause());
            assertTrue(e.getMessage().contains(name));
        }
    }

    final void setLeadsToInvocationTargetException(final int value) {
        throw new UnsupportedOperationException("should lead to an InvocationTargetException");
    }
}
