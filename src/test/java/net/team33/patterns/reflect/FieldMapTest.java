package net.team33.patterns.reflect;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import net.team33.patterns.reflect.test.Sample;
import org.junit.Test;

import java.util.Arrays;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class FieldMapTest {

    private static final EnhancedRandomBuilder RANDOM_BUILDER = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
            .overrideDefaultInitialization(true)
            .collectionSizeRange(1, 3)
            .stringLengthRange(4, 16);

    private final EnhancedRandom random = RANDOM_BUILDER
            .build();
    private final Function<Object, FieldMap> objectToMap = FieldMap.builder(Object.class)
            .build();
    private final Function<Sample, FieldMap> sampleToMap = FieldMap.builder(Sample.class)
            .build();
    private final Function<Sample, FieldMap> sampleToMapAll = FieldMap.builder(Sample.class)
            .setFilter(field -> true)
            .build();

    @Test
    public final void size() {
        assertEquals(0, objectToMap.apply(new Object()).size());
        assertEquals(8, sampleToMap.apply(new Sample()).size());
        assertEquals(12, sampleToMapAll.apply(new Sample()).size());
    }

    @Test
    public final void isEmpty() {
        assertTrue(objectToMap.apply(new Object()).isEmpty());
        assertFalse(sampleToMap.apply(new Sample()).isEmpty());
        assertFalse(sampleToMapAll.apply(new Sample()).isEmpty());
    }

    @Test
    public final void containsValue() {
        final Sample sample = random.nextObject(Sample.class);
        final FieldMap result = sampleToMap.apply(sample);
        for (final Object value : Arrays.asList(
                sample.getAPrivateField(),
                sample.getAPrivateFinalField(),
                sample.getAPackageField(),
                sample.getAPackageFinalField(),
                sample.getAProtectedField(),
                sample.getAProtectedFinalField(),
                sample.getAPublicField(),
                sample.getAPublicFinalField()
        )) {
            assertTrue(result.containsValue(value));
        }
        for (final String value : Arrays.asList(
                sample.getAPublicTransientField(),
                sample.getAPublicFinalTransientField(),
                Sample.getAStaticField(),
                Sample.getAStaticFinalField(),
                "any other value"
        )) {
            assertFalse(result.containsKey(value));
        }

    }

    @Test
    public final void containsKey() {
        final FieldMap result = sampleToMap.apply(new Sample());
        for (final String key : Arrays.asList(
                "aPrivateField",
                "aPrivateFinalField",
                "aPackageField",
                "aPackageFinalField",
                "aProtectedField",
                "aProtectedFinalField",
                "aPublicField",
                "aPublicFinalField"
        )) {
            assertTrue(result.containsKey(key));
        }
        for (final String key : Arrays.asList(
                "aPublicTransientField",
                "aPublicFinalTransientField",
                "aStaticField",
                "aStaticFinalField",
                "any other key"
        )) {
            assertFalse(result.containsKey(key));
        }
    }

    @Test
    public final void get() {
        final Sample sample = random.nextObject(Sample.class);
        final FieldMap result = sampleToMap.apply(sample);

        assertSame(sample.getAPrivateField(), result.get("aPrivateField"));
        assertSame(sample.getAPrivateFinalField(), result.get("aPrivateFinalField"));
        assertSame(sample.getAPackageField(), result.get("aPackageField"));
        assertSame(sample.getAPackageFinalField(), result.get("aPackageFinalField"));
        assertSame(sample.getAProtectedField(), result.get("aProtectedField"));
        assertSame(sample.getAProtectedFinalField(), result.get("aProtectedFinalField"));
        assertSame(sample.getAPublicField(), result.get("aPublicField"));
        assertSame(sample.getAPublicFinalField(), result.get("aPublicFinalField"));

        assertNull(result.get("aPublicTransientField"));
        assertNull(result.get("aPublicFinalTransientField"));
        assertNull(result.get("aStaticField"));
        assertNull(result.get("aStaticFinalField"));
        assertNull(result.get("aNotExistingField"));
    }

    @Test
    public void put() {
    }

    @Test
    public void remove() {
    }

    @Test
    public void putAll() {
        final Sample sample = random.nextObject(Sample.class);
        final Sample other = random.nextObject(Sample.class);
        assertNotEquals(sample, other);

        sampleToMap.apply(other).putAll(sampleToMap.apply(sample));
        assertEquals(sample, other);
    }

    @Test
    public void clear() {
    }

    @Test
    public void keySet() {
    }

    @Test
    public void values() {
    }

    @Test
    public void entrySet() {
    }
}