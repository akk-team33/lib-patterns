package net.team33.patterns.reflections;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import net.team33.patterns.reflections.test.Sample;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.team33.patterns.reflections.FieldUtil.ACCESSIBLE;
import static net.team33.patterns.reflections.FieldUtil.SIGNIFICANT;

public class FieldMapTest {

    private static final EnhancedRandomBuilder RANDOM_BUILDER = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
            .overrideDefaultInitialization(true)
            .collectionSizeRange(1, 3)
            .stringLengthRange(4, 16);

    private final EnhancedRandom random = RANDOM_BUILDER.build();
    private final Map<String, Field> fields = Stream.of(Sample.class.getDeclaredFields())
            .filter(SIGNIFICANT)
            .collect(Collectors.toMap(Field::getName, ACCESSIBLE, (l, r) -> r, TreeMap::new));
    private final FieldMap<Sample> fieldMap = () -> fields.entrySet().stream();

    @Test
    public final void mapToFrom() {
        final Sample origin = random.nextObject(Sample.class);
        final Map<String, Object> map = fieldMap.map(origin).to(new HashMap<>());
        Assert.assertEquals(fields.keySet(), map.keySet());

        final Sample result = fieldMap.map(new Sample()).from(map);
        Assert.assertEquals(origin, result);
    }

    @Test
    public final void toStringSample() {
        final Sample sample = random.nextObject(Sample.class);
        final String expected = fieldMap.map(sample).to(new TreeMap<>()).toString();
        final String result = fieldMap.toString(sample);
        Assert.assertEquals(expected, result);
    }
}