package de.team33.patterns.lazy.narvi.publics;

import de.team33.patterns.lazy.narvi.XReLazy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class XReLazyTest extends LazyTestBase<XReLazy<Integer, Exception>> {

    XReLazyTest() {
        super(new Input<>(XReLazy::init, lazy -> lazy));
    }

    @Test
    final void reset() throws Exception {
        final Integer first = subject.get();
        assertSame(first, subject.get(), "a subsequent access should reply the same result as the first time");

        final Integer reset = subject.reset().get();
        assertNotEquals(first, reset, "after reset(), a subsequent access should reply a new value");
    }
}
