package de.team33.test.patterns.properties.e4;

import de.team33.patterns.properties.e4.Uniformer;
import de.team33.test.patterns.properties.shared.AnyClass;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.TreeMap;

class UniformerTest {

    private Uniformer<AnyClass> uniformer;

    @Test
    final void test() {
        final AnyClass origin = new AnyClass(new Random());
        final TreeMap<String, Object> result = uniformer.map(origin).to(new TreeMap<>());
    }

}
