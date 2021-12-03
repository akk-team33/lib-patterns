package de.team33.test.patterns.production.e1;

import de.team33.patterns.production.e1.PlainFactoryHub;
import org.junit.jupiter.api.Test;

import static de.team33.patterns.production.e1.FactoryUtil.unknownTokenMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlainFactoryHubTest {

    private static final Object TOKEN = new Object();
    private static final Object UNKNOWN = new Object();

    private final PlainFactoryHub hub = PlainFactoryHub.builder()
                                                       .setUnknownTokenListener(token -> {
                                                           throw new IllegalStateException(unknownTokenMessage(token));
                                                       })
                                                       .on(TOKEN).apply(ctx -> TOKEN)
                                                       .build();

    @Test
    final void test() {
        assertNull(hub.get(null));
        assertEquals(TOKEN, hub.get(TOKEN));
        assertThrows(IllegalStateException.class, () -> hub.get(UNKNOWN));
    }
}
