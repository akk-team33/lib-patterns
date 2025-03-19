package de.team33.test.patterns.lazy.narvi.publics;

import de.team33.patterns.exceptional.dione.XSupplier;
import de.team33.patterns.lazy.narvi.InitException;
import de.team33.patterns.lazy.narvi.Lazy;
import de.team33.patterns.lazy.narvi.ReLazy;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReLazyTest extends LazyTestBase<ReLazy<Integer>> {

    ReLazyTest() {
        super(new Input<>(ReLazy::initEx, lazy -> lazy::get));
    }

    /**
     * Ensures that {@link Lazy#initEx(XSupplier)} encapsulates code so that any checked exception
     * that is thrown is wrapped in an (unchecked) {@link Lazy.InitException}.
     */
    @Test
    final void exceptional() {
        final ReLazy<?> lazy = ReLazy.initEx(() -> {
            throw new SQLException("this is a test");
        });
        assertThrows(InitException.class, lazy::get);
    }

    @Test
    final void reset() throws Exception {
        final Integer first = subject.get();
        assertSame(first, subject.get(), "a subsequent access should reply the same result as the first time");

        final Integer reset = subject.reset().get();
        assertNotEquals(first, reset, "after reset(), a subsequent access should reply a new value");
    }
}
