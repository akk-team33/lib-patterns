package net.team33.patterns.reflect.fields;

import net.team33.patterns.reflect.test.Sample;
import net.team33.patterns.reflect.test.SampleEx;
import org.junit.Assert;
import org.junit.Test;

public class ToNameTest {

    @Test
    public final void simple() {
        ToStream.FLAT.apply(Sample.class).forEach(field -> {
            Assert.assertEquals(
                    field.getName(),
                    ToName.SIMPLE.apply(field)
            );
        });
    }

    @Test
    public final void qualified() {
        ToStream.FLAT.apply(Sample.class).forEach(field -> {
            Assert.assertEquals(
                    "net.team33.patterns.reflect.test.Sample." + field.getName(),
                    ToName.QUALIFIED.apply(field)
            );
            Assert.assertEquals(
                    "net.team33.patterns.reflect.test.Sample." + field.getName(),
                    ToName.QUALIFIED.apply(field)
            );
            Assert.assertEquals(
                    "net.team33.patterns.reflect.test.Sample." + field.getName(),
                    ToName.QUALIFIED.apply(field)
            );
        });
    }

    @Test
    public final void prefixed() {
        ToStream.FLAT.apply(Sample.class).forEach(field -> {
            Assert.assertEquals(
                    field.getName(),
                    ToName.PREFIXED.apply(Sample.class).apply(field)
            );
            Assert.assertEquals(
                    "." + field.getName(),
                    ToName.PREFIXED.apply(SampleEx.class).apply(field)
            );
            Assert.assertEquals(
                    ".." + field.getName(),
                    ToName.PREFIXED.apply(SampleXX.class).apply(field)
            );
        });
    }

    @SuppressWarnings({"ClassTooDeepInInheritanceTree", "EmptyClass"})
    private static class SampleXX extends SampleEx {
    }
}