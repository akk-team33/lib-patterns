package de.team33.patterns.json.jota.publics;

import de.team33.patterns.json.jota.Json;
import de.team33.patterns.typing.proteus.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonTest {

    public static final Type<?> NULL_TYPE = null;

    @Test
    final void setup_null() {
        assertThrows(NullPointerException.class,
                     () -> Json.setup(NULL_TYPE, JsonType.STRING, mapping -> mapping)); // .printStackTrace();
        assertThrows(NullPointerException.class,
                     () -> Json.setup(Type.of(Sample00.class), null, mapping -> mapping)); // .printStackTrace();
        assertThrows(NullPointerException.class,
                     () -> Json.setup(Type.of(Sample01.class), JsonType.STRING, mapping -> null)); // .printStackTrace();
        assertThrows(NullPointerException.class,
                     () -> Json.setup(Type.of(Sample02.class), JsonType.STRING, null)); // .printStackTrace();
    }

    @Test
    final void setup_duplicate() {
        Json.setup(Type.of(Sample03.class), JsonType.STRING, mapping -> mapping);
        assertThrows(IllegalStateException.class,
                     () -> Json.setup(Type.of(Sample03.class), JsonType.STRING, mapping -> mapping));
        // .printStackTrace();
    }

    @Test
    final void roundTrip() {
        final var origin = 278;
        final var stage = Json.map(origin);
        final var result = Json.parse(Integer.class);
    }

    @SuppressWarnings("EmptyClass")
    private static final class Sample00 {}

    @SuppressWarnings("EmptyClass")
    private static final class Sample01 {}

    @SuppressWarnings("EmptyClass")
    private static final class Sample02 {}

    @SuppressWarnings("EmptyClass")
    private static final class Sample03 {}
}