package de.team33.patterns.building.elara.publics;

import de.team33.patterns.building.elara.Lifecycle;
import de.team33.patterns.building.elara.ProtoBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProtoBuilderTest {

    @Test
    final void legal() {
        assertInstanceOf(LegalBuilder.class, new LegalBuilder());
    }

    @Test
    final void illegal() {
        assertThrows(IllegalArgumentException.class, () -> new IllegalBuilder());
    }

    private static final Lifecycle LIFECYCLE = new Lifecycle() {
        @Override
        public void check() {
        }

        @Override
        public void increment() {
        }
    };

    static class LegalBuilder extends ProtoBuilder<StringBuilder, LegalBuilder> {

        protected LegalBuilder() {
            super(new StringBuilder(), LIFECYCLE, LegalBuilder.class);
        }
    }

    static class IllegalBuilder extends ProtoBuilder<StringBuilder, LegalBuilder> {

        protected IllegalBuilder() {
            super(new StringBuilder(), LIFECYCLE, LegalBuilder.class);
        }
    }
}
