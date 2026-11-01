package de.team33.patterns.records.triton.publics;

import de.team33.patterns.records.triton.RenderOption;
import de.team33.patterns.records.triton.Triton;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExtraTest {

    private final String value = UUID.randomUUID().toString();
    private final String json = "{\"stringable\" : \"%s\"}".formatted(value);

    @Test
    final void identity() {
        Triton.setup(StringableA.class, mapping -> mapping);

        final SampleA source = new SampleA(new StringableA(value));
        assertEquals(json, Triton.toJson(source, RenderOption.INLINE_OBJECT));
        assertEquals(source, Triton.toRecord(SampleA.class, json));
    }

    @Test
    final void forward_null() {
        Triton.setup(StringableB.class, mapping -> mapping.forward(null));
        assertThrows(IllegalArgumentException.class,
                     () -> Triton.toJson(new SampleB(new StringableB(value)))); //.printStackTrace();
        assertThrows(IllegalArgumentException.class,
                     () -> Triton.toRecord(SampleB.class, json)); //.printStackTrace();
    }

    @Test
    final void backward_null() {
        Triton.setup(StringableC.class, mapping -> mapping.backward(null));
        assertThrows(IllegalArgumentException.class,
                     () -> Triton.toJson(new SampleC(new StringableC(value)))); //.printStackTrace();
        assertThrows(IllegalArgumentException.class,
                     () -> Triton.toRecord(SampleC.class, json)); //.printStackTrace();
    }

    static class StringableBase {

        private final String value;

        StringableBase(final String value) {
            this.value = value;
        }

        @Override
        public final boolean equals(final Object obj) {
            return (this == obj) || ((obj instanceof final StringableBase other) && value.equals(other.value));
        }

        @Override
        public final int hashCode() {
            return value.hashCode();
        }

        @Override
        public final String toString() {
            return value;
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static class StringableA extends StringableBase {
        public StringableA(final String value) {
            super(value);
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static class StringableB extends StringableBase {
        public StringableB(final String value) {
            super(value);
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static class StringableC extends StringableBase {
        public StringableC(final String value) {
            super(value);
        }
    }

    private record SampleA(StringableA stringable) {
    }

    private record SampleB(StringableB stringable) {
    }

    private record SampleC(StringableC stringable) {
    }
}