package net.team33.patterns.reflect;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import net.team33.patterns.reflections.test.Sample;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class FieldsTest {

    private static final EnhancedRandomBuilder RANDOM_BUILDER = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
            .overrideDefaultInitialization(true)
            .collectionSizeRange(1, 3)
            .stringLengthRange(4, 16);

    private final EnhancedRandom random = RANDOM_BUILDER.build();
    private final Fields<Sample> fields = Fields.of(Sample.class);
    private List<Sample> samples;

    @Before
    public final void setUp() {
        final List<Sample> uniques = random.objects(Sample.class, 8).collect(toList());
        samples = Stream.concat(
                uniques.stream(),
                uniques.stream().map(origin -> fields.copy(origin).to(new Sample()))
        ).collect(toList());
    }

    @Test
    public final void copyTo() {
        final Sample original = random.nextObject(Sample.class);
        final Sample result = fields.copy(original).to(new Sample());
        assertNotSame(original, result);
        assertEquals(original, result);
    }

    @Test
    public final void copyFrom() {
        final Sample original = random.nextObject(Sample.class);
        final Sample result = fields.copy(new Sample()).from(original);
        assertNotSame(original, result);
        assertEquals(original, result);
    }

    @Test
    public final void mapToFrom() {
        final Sample origin = random.nextObject(Sample.class);
        final Map<String, Object> map = fields.map(origin).to(new HashMap<>());
        final Sample result = fields.map(new Sample()).from(map);
        assertEquals(origin, result);
    }

    @Test
    public final void equalsSample() {
        final int[] count = {0, 0};
        samples.forEach(left -> samples.forEach(right -> {
            final int index = left.equals(right) ? 0 : 1;
            count[index] += 1;
            assertEquals(index, fields.equals(left, right) ? 0 : 1);
        }));
        assertEquals(32, count[0]);
        assertEquals(224, count[1]);
    }

    @Test
    public final void hashCodeSample() {
        final int[] count = {0, 0};
        samples.forEach(left -> samples.forEach(right -> {
            final boolean equalsLR = fields.equals(left, right);
            final int leftHash = fields.hashCode(left);
            final int rightHash = fields.hashCode(right);
            if (equalsLR) {
                assertEquals(leftHash, rightHash);
                count[0] += 1;
            }
            if (leftHash != rightHash) {
                assertFalse(equalsLR);
                count[1] += 1;
            }
        }));
        assertTrue("count[0] = " + count[0], 32 <= count[0]);
        assertTrue("count[1] = " + count[1], 224 <= count[1]);
    }

    @Test
    public final void toStringSample() {
        final Sample sample = random.nextObject(Sample.class);
        final String expected = fields.map(sample).to(new LinkedHashMap<>()).toString();
        final String result = fields.toString(sample);
        assertEquals(expected, result);
    }
}
