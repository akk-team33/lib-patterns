package de.team33.patterns.reflect.pandora;

import de.team33.patterns.testing.reflect.pandora.BeanClass;
import de.team33.patterns.testing.reflect.pandora.BeanInterface;
import de.team33.patterns.testing.reflect.pandora.Supply;
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
