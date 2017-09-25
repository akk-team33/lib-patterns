package net.team33.patterns.reflect.fields;

import net.team33.patterns.reflect.test.Sample;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class FilterTest {

    private static Set<String> sampleFields() {
        return ToStream.FLAT.apply(Sample.class)
                .map(ToName.SIMPLE)
                .collect(HashSet::new, Set::add, Set::addAll);
    }

    @Test
    public final void all() throws Exception {
        final Set<String> expected = ToStream.FLAT.apply(Sample.class)
                .map(ToName.SIMPLE)
                .collect(Collectors.toSet());
        final Set<String> result = ToStream.FLAT.apply(Sample.class)
                .filter(Filter.ALL)
                .map(ToName.SIMPLE)
                .collect(Collectors.toSet());
        Assert.assertEquals(expected, result);
    }

    @Test
    public final void notAll() throws Exception {
        final Set<String> expected = Collections.emptySet();
        final Set<String> result = ToStream.FLAT.apply(Sample.class)
                .filter(Filter.ALL.negate())
                .map(ToName.SIMPLE)
                .collect(Collectors.toSet());
        Assert.assertEquals(expected, result);
    }

    @Test
    public final void instance() throws Exception {
        final Set<String> expected = sampleFields();
        expected.removeAll(Arrays.asList(
                "aStaticFinalField",
                "aStaticField"
        ));
        final Set<String> result = ToStream.FLAT.apply(Sample.class)
                .filter(Filter.INSTANCE)
                .map(ToName.SIMPLE)
                .collect(Collectors.toSet());
        Assert.assertEquals(expected, result);
    }

    @Test
    public final void notInstance() throws Exception {
        final Set<String> expected = new HashSet<>(Arrays.asList(
                "aStaticFinalField",
                "aStaticField"
        ));
        final Set<String> result = ToStream.FLAT.apply(Sample.class)
                .filter(Filter.INSTANCE.negate())
                .map(ToName.SIMPLE)
                .collect(Collectors.toSet());
        Assert.assertEquals(expected, result);
    }

    @Test
    public final void significant() throws Exception {
        final Set<String> expected = sampleFields();
        expected.removeAll(Arrays.asList(
                "aStaticFinalField",
                "aStaticField",
                "aPublicFinalTransientField",
                "aPublicTransientField"
        ));
        final Set<String> result = ToStream.FLAT.apply(Sample.class)
                .filter(Filter.SIGNIFICANT)
                .map(ToName.SIMPLE)
                .collect(Collectors.toSet());
        Assert.assertEquals(expected, result);
    }

    @Test
    public final void notSignificant() throws Exception {
        final Set<String> expected = new HashSet<>(Arrays.asList(
                "aStaticFinalField",
                "aStaticField",
                "aPublicFinalTransientField",
                "aPublicTransientField"
        ));
        final Set<String> result = ToStream.FLAT.apply(Sample.class)
                .filter(Filter.SIGNIFICANT.negate())
                .map(ToName.SIMPLE)
                .collect(Collectors.toSet());
        Assert.assertEquals(expected, result);
    }
}
