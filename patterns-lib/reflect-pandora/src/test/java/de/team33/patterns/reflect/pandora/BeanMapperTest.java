package de.team33.patterns.reflect.pandora;

import de.team33.patterns.reflect.pandora.testing.BeanClass;
import de.team33.patterns.reflect.pandora.testing.BeanInterface;
import de.team33.patterns.reflect.pandora.testing.Supply;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BeanMapperTest {

    private static final Supply SUPPLY = new Supply();

    private final BeanMapper<BeanInterface, BeanClass> mapper = BeanMapper.mapping(BeanInterface.class,
                                                                                   BeanClass.class);

    @Test
    final void test() {
        final BeanInterface expected = SUPPLY.nextBean();
        final BeanClass result = SUPPLY.nextBean();
        assertNotEquals(expected, result);

        mapper.map(expected, result);

        assertEquals(expected, result);
    }
}
