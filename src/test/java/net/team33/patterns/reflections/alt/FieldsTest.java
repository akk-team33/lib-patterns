package net.team33.patterns.reflections.alt;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import net.team33.patterns.reflections.test.Sample;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class FieldsTest {

    private static final EnhancedRandomBuilder RANDOM_BUILDER = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
            .overrideDefaultInitialization(true)
            .collectionSizeRange(1, 3)
            .stringLengthRange(4, 16);

    private final EnhancedRandom random = RANDOM_BUILDER.build();
    private final Function<Sample, Fields<Sample>> fields = Fields.of(Sample.class);
    private List<Sample> samples;

    @Before
    public final void setUp() {
        final List<Sample> uniques = random.objects(Sample.class, 8).collect(toList());
        samples = Stream.concat(
                uniques.stream(),
                uniques.stream().map(origin -> fields.apply(origin).copyTo(new Sample()))
        ).collect(toList());
    }

    @Test
    public final void copyTo() throws Exception {
        final Sample original = random.nextObject(Sample.class);
        final Sample result = fields.apply(original).copyTo(new Sample());
        assertEquals(original, result);
    }

    @Test
    public final void copyFrom() throws Exception {
        final Sample original = random.nextObject(Sample.class);
        final Sample result = fields.apply(new Sample()).copy(original);
        assertEquals(original, result);
    }

    @Test
    public final void equalsSample() throws Exception {
        final int[] count = {0, 0};
        samples.forEach(left -> samples.forEach(right -> {
            final int index = left.equals(right) ? 0 : 1;
            count[index] += 1;
            assertEquals(index, fields.apply(left).equalsTo(right) ? 0 : 1);
        }));
        assertEquals(32, count[0]);
        assertEquals(224, count[1]);
    }

    @Test
    public final void hashCodeSample() throws Exception {
        final int[] count = {0, 0};
        samples.forEach(left -> samples.forEach(right -> {
            final boolean equalsLR = fields.apply(left).equalsTo(right);
            final int leftHash = fields.apply(left).toHashCode();
            final int rightHash = fields.apply(right).toHashCode();
            if (equalsLR) {
                assertEquals(leftHash, rightHash);
                count[0] += 1;
            }
            if (leftHash != rightHash) {
                assertFalse(equalsLR);
                count[1] += 1;
            }
        }));
        assertEquals(32, count[0]);
        assertEquals(224, count[1]);
    }
}