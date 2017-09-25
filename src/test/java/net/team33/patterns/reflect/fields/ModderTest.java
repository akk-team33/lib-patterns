package net.team33.patterns.reflect.fields;

import net.team33.patterns.reflect.test.Sample;
import org.junit.Test;

import static org.junit.Assert.fail;

public class ModderTest {

    @Test
    public void setAccessible() {
        final Sample sample = new Sample();
        ToStream.FLAT.apply(Sample.class).filter(Filter.PUBLIC.negate()).forEach(field -> {
            try {
                field.get(sample);
                fail("should fail");
            } catch (final IllegalAccessException ignored) {
                Modder.SET_ACCESSIBLE.accept(field);
                try {
                    field.get(sample);
                } catch (final IllegalAccessException e1) {
                    throw new AssertionError(e1);
                }
            }
        });
    }
}