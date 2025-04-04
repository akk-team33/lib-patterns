package de.team33.patterns.building.elara.publics;

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
        assertThrows(IllegalArgumentException.class, IllegalBuilder::new);
    }

    private static final ProtoBuilder.Lifecycle LIFECYCLE = new ProtoBuilder.Lifecycle() {
        @Override
        public void check() {
            // nothing to do here
        }

        @Override
        public void increment() {
            // nothing to do here
        }
    };

    static class LegalBuilder extends ProtoBuilder<StringBuilder, LegalBuilder> {

        LegalBuilder() {
            super(new StringBuilder(0), LIFECYCLE, LegalBuilder.class);
        }
    }

    static class IllegalBuilder extends ProtoBuilder<StringBuilder, LegalBuilder> {

        IllegalBuilder() {
            super(new StringBuilder(0), LIFECYCLE, LegalBuilder.class);
        }
    }
}
