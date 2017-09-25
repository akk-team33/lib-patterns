package net.team33.patterns.reflections;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import net.team33.patterns.reflect.test.Sample;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class FieldsTest {

    private static final EnhancedRandomBuilder RANDOM_BUILDER = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
            .overrideDefaultInitialization(true)
            .collectionSizeRange(1, 3)
            .stringLengthRange(4, 16);

    private final EnhancedRandom random = RANDOM_BUILDER.build();
    private final Fields<Sample> fields = Fields.of(Sample.class);

    @Test
    public final void toMapReMap() throws Exception {
        final Sample original = random.nextObject(Sample.class);
        final Map<String, Object> map = fields.toMap(original);
        Assert.assertEquals(
                original,
                fields.toMap(new Sample()).adopt(map)
        );
    }
}