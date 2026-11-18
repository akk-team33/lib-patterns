package de.team33.patterns.records.triton;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MappingTest {

    private final Mapping<Class<?>, String> mapping = new Mapping<>(Class::getName, Class::forName);

    @Test
    final void map_illegal() {
        assertThrows(IllegalStateException.class,
                     () -> mapping.reverse().map(UUID.randomUUID().toString())); //.printStackTrace();
    }

    @Test
    final void map_roundTrip() {
        final String stage = mapping.map(List.class);
        final Class<?> result = mapping.reverse().map(stage);
        assertEquals(List.class, result);
    }

    @Test
    final void isFeatured() {
        assertTrue(mapping.isFeatured());
        assertTrue(mapping.reverse().isFeatured());
        assertFalse(mapping.forward(null).isFeatured());
        assertFalse(mapping.backward(null).isFeatured());
        assertFalse(mapping.forward(null).backward(null).isFeatured());
    }

    @Test
    final void forward() {
        final Mapping<Class<?>, String> limited = mapping.forward(null);
        assertThrows(IllegalStateException.class, () -> limited.map(List.class)); //.printStackTrace();

        final String expected = List.class.getName();
        final Class<?> stage = limited.reverse().map(expected);
        assertEquals(List.class, stage);

        final Mapping<Class<?>, String> result = limited.forward(Class::getName);
        assertEquals(expected, result.map(stage));
    }

    @Test
    final void backward() {
        final Mapping<Class<?>, String> limited = mapping.backward(null);
        final String stage = limited.map(List.class);
        assertThrows(IllegalStateException.class, () -> limited.reverse().map(stage)); //.printStackTrace();

        final Mapping<Class<?>, String> result = limited.backward(Class::forName);
        assertEquals(List.class, result.reverse().map(stage));
    }
}