package de.team33.test.patterns.lazy.narvi.publics;

import de.team33.patterns.exceptional.dione.XSupplier;
import de.team33.patterns.lazy.narvi.Lazy;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class LazyTest extends LazyTestBase<Lazy<Integer>> {

    LazyTest() {
        super(new Input<>(Lazy::initEx, lazy -> lazy::get));
    }

    /**
     * Ensures that {@link Lazy#initEx(XSupplier)} encapsulates code so that any checked exception
     * that is thrown is wrapped in an (unchecked) {@link Lazy.InitException}.
     */
    @Test
    final void exceptional() {
        final Lazy<?> lazy = Lazy.initEx(() -> {
            throw new SQLException("this is a test");
        });
        assertThrows(Lazy.InitException.class, lazy::get);
    }
}
