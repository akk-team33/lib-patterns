package de.team33.test.patterns.random.tarvos;

import de.team33.patterns.random.tarvos.Charger;
import de.team33.samples.patterns.production.narvi.Buildable;
import de.team33.samples.patterns.production.narvi.Sample;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChargerTest {

    private static final List<String> STRINGS = Collections.singletonList("xyz");
    private static final List<Long> LONGS = Collections.singletonList(0xDEADFACEDEADFACEL);

    @Test
    final void initBean() {
        final Sample expected = new Sample().setBooleanValue(false)
                                            .setStringValue("ABC")
                                            .setIntValue(278)
                                            .setLongValue(Long.MAX_VALUE)
                                            .setStringList(Arrays.asList("abc", "def", "ghi"))
                                            .setLongList(Arrays.asList(4L, 69L, 345L));
        final Charger charger = Charger.builder()
                                       .on(true).apply(expected::isBooleanValue)
                                       .on(int.class).apply(expected::getIntValue)
                                       .on(String.class).apply(expected::getStringValue)
                                       .on(Long.class).apply(expected::getLongValue)
                                       .on(STRINGS).apply(expected::getStringList)
                                       .on(LONGS).apply(expected::getLongList)
                                       .build();
        final Sample result = charger.initBean(new Sample().setBooleanValue(true)
                                                           .setStringList(STRINGS)
                                                           .setLongList(LONGS));
        assertEquals(expected, result);
    }

    @Test
    final void initBean_Buildable() {
        final Buildable expected = Buildable.builder()
                                            .setStringValue("ABC")
                                            .setIntValue(278)
                                            .setLongValue(Long.MAX_VALUE)
                                            .setStringList(Arrays.asList("abc", "def", "ghi"))
                                            .setLongList(Arrays.asList(4L, 69L, 345L))
                                            .build();
        final Sample template = new Sample().setStringList(STRINGS)
                                            .setLongList(LONGS);
        final Charger charger = Charger.builder()
                                       .on(int.class).apply(expected::getIntValue)
                                       .on(String.class).apply(expected::getStringValue)
                                       .on(Long.class).apply(expected::getLongValue)
                                       .on(STRINGS).apply(expected::getStringList)
                                       .on(LONGS).apply(expected::getLongList)
                                       .build();
        final Buildable result = charger.initBean(Buildable.builder(), template)
                                        .build();
        assertEquals(expected, result);
    }

    @Test
    final void toBuilder() {
        final Charger charger1 = Charger.builder()
                                        .on(int.class).apply(() -> 5)
                                        .on(char.class).apply(() -> '5')
                                        .build();
        assertEquals(5, charger1.get(int.class));
        assertEquals('5', charger1.get(char.class));
        assertThrows(NoSuchElementException.class, () -> charger1.get(String.class));

        final Charger charger2 = charger1.toBuilder()
                                         .on(int.class).apply(() -> 7)
                                         .on(String.class).apply(() -> "11")
                                         .build();
        assertEquals(7, charger2.get(int.class));
        assertEquals('5', charger2.get(char.class));
        assertEquals("11", charger2.get(String.class));

        final Charger charger3 = charger2.toBuilder()
                                         .on(char.class).apply(() -> '9')
                                         .build();
        assertEquals(7, charger3.get(int.class));
        assertEquals('9', charger3.get(char.class));
        assertEquals("11", charger3.get(String.class));

        final Charger charger4 = charger1.toBuilder()
                                         .on(char.class).apply(() -> '9')
                                         .build();
        assertEquals(5, charger4.get(int.class));
        assertEquals('9', charger4.get(char.class));
        assertThrows(NoSuchElementException.class, () -> charger4.get(String.class));
    }
}