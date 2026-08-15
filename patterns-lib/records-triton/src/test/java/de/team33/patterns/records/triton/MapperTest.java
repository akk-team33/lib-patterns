package de.team33.patterns.records.triton;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MapperTest {

    private final Mapper<Class<?>, String> mapper = new Mapper<>(Class::getName, Class::forName);

    @Test
    final void map_illegal() {
        assertThrows(IllegalArgumentException.class,
                     () -> mapper.reverse().map(UUID.randomUUID().toString())); //.printStackTrace();
    }

    @Test
    final void map_roundTrip() {
        final String stage = mapper.map(List.class);
        final Class<?> result = mapper.reverse().map(stage);
        assertEquals(List.class, result);
    }

    @Test
    final void isFullFeatured() {
        assertTrue(mapper.isFullFeatured());
        assertTrue(mapper.reverse().isFullFeatured());
        assertFalse(mapper.forward(null).isFullFeatured());
        assertFalse(mapper.backward(null).isFullFeatured());
        assertFalse(mapper.forward(null).backward(null).isFullFeatured());
    }

    @Test
    final void forward() {
        final Mapper<Class<?>, String> limited = mapper.forward(null);
        assertThrows(IllegalStateException.class, () -> limited.map(List.class)); //.printStackTrace();

        final String expected = List.class.getName();
        final Class<?> stage = limited.reverse().map(expected);
        assertEquals(List.class, stage);

        final Mapper<Class<?>, String> result = limited.forward(Class::getName);
        assertEquals(expected, result.map(stage));
    }

    @Test
    final void backward() {
        final Mapper<Class<?>, String> limited = mapper.backward(null);
        final String stage = limited.map(List.class);
        assertThrows(IllegalStateException.class, () -> limited.reverse().map(stage)); //.printStackTrace();

        final Mapper<Class<?>, String> result = limited.backward(Class::forName);
        assertEquals(List.class, result.reverse().map(stage));
    }
}