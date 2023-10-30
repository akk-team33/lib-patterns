package de.team33.patterns.reflect.pandora;

import de.team33.patterns.reflect.pandora.testing.BeanClass;
import de.team33.patterns.reflect.pandora.testing.BeanInterface;
import de.team33.patterns.reflect.pandora.testing.Supply;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MapperTest extends Supply {

    private final Mapper<BeanInterface, BeanClass> mapper = Mapper.mapping(BeanInterface.class, BeanClass.class);

    @Test
    final void map() {
        final BeanClass origin = nextBean();
        final BeanClass target = nextBean();
        assertNotEquals(origin, target);

        mapper.map(origin, target);

        assertEquals(origin, target);
    }
}
