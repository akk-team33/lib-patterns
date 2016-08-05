package net.team33.patterns;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MutableTest {

    @Test
    public final void testApply() {
        final String result = Mutable.of("original").apply(mutable -> {
            assertEquals("original", mutable.get());
            mutable.set("updated");
        }).get();
        assertEquals("updated", result);
    }
}