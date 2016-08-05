package net.team33.patterns;

import org.junit.Test;

import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class LazyTest {

    @SuppressWarnings("RedundantStringConstructorCall") // by intention!
    private static final Supplier<String> ORIGIN = () -> new String("This is a string");

    @Test
    public final void originGetNotTheSameTwice() {
        assertNotSame("ORIGIN.get() should NOT get the same instance, twice", ORIGIN.get(), ORIGIN.get());
    }

    @Test
    public final void lazyGetTheSameTwice() {
        final Supplier<String> sample = new Lazy<>(ORIGIN);
        assertSame("sample.get() should get the same instance, each time", sample.get(), sample.get());
    }

    @Test
    public final void get() {
        assertEquals("sample.get() should get the original value", ORIGIN.get(), new Lazy<>(ORIGIN).get());
    }
}