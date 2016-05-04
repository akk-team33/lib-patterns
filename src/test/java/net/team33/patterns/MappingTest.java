package net.team33.patterns;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
public class MappingTest {

    public static final String STRAIGHT_NOT_FAILED = "straight call did not fail -> test is not significant";
    public static final String NULL_ELEMENT = null;
    public static final int NOT_A_STRING = 278;

    private String sample1, sample2, sample3;
    private Map<String, String> samples;

    @Before
    public final void before() {
        sample1 = UUID.randomUUID().toString();
        sample2 = UUID.randomUUID().toString();
        sample3 = UUID.randomUUID().toString();
        samples = Mapper.map(new HashMap<String, String>())
                .put(sample1, sample2)
                .put(sample2, sample3)
                .put(sample3, sample1)
                .unmodifiable();
    }

    @Test
    public final void put() {
        assertEquals(
                Collections.singletonMap(sample1, sample2),
                Mapping.put(new HashMap<CharSequence, Comparable<? extends CharSequence>>(), sample1, sample2)
        );
    }

    @Test
    public final void putAll() {
        assertEquals(
                samples,
                Mapping.putAll(new HashMap<CharSequence, Comparable<? extends CharSequence>>(), samples)
        );
    }

    @Test
    public final void clear() {
        assertEquals(
                Collections.emptyMap(),
                Mapping.clear(new HashMap<>(samples))
        );
    }

    @Test
    public final void remove() {
        assertFalse(Mapping.remove(new HashMap<>(samples), sample2).containsKey(sample2));
    }

    @Test
    public final void removeNull() {
        final TreeMap<String, String> subject = new TreeMap<>(samples);
        try {
            subject.remove(null);
            fail(STRAIGHT_NOT_FAILED);
        } catch (final NullPointerException ignored) {
            assertFalse(
                    Mapping.containsKey(
                            Mapping.remove(subject, NULL_ELEMENT),
                            NULL_ELEMENT
                    )
            );
        }
    }

    @Test
    public final void removeIncompatibleType() {
        final TreeMap<String, String> subject = new TreeMap<>(samples);
        try {
            subject.remove(NOT_A_STRING);
            fail(STRAIGHT_NOT_FAILED);
        } catch (final ClassCastException ignored) {
            assertFalse(
                    Mapping.containsKey(
                            Mapping.remove(subject, NOT_A_STRING),
                            NOT_A_STRING
                    )
            );
        }
    }

    @Test
    public final void containsKey() {
        assertTrue(Mapping.containsKey(samples, sample1));
        assertTrue(Mapping.containsKey(samples, sample2));
        assertTrue(Mapping.containsKey(samples, sample3));

        assertFalse(Mapping.containsKey(samples, UUID.randomUUID().toString()));
        assertFalse(Mapping.containsKey(samples, NOT_A_STRING));
        assertFalse(Mapping.containsKey(samples, NULL_ELEMENT));

        final Map<String, String> treeMap = new TreeMap<>(samples);
        try {
            treeMap.containsKey(NULL_ELEMENT);
            fail(STRAIGHT_NOT_FAILED);
        } catch (final NullPointerException ignored) {
            assertFalse(Mapping.containsKey(treeMap, NULL_ELEMENT));
        }
        try {
            treeMap.containsKey(NOT_A_STRING);
            fail(STRAIGHT_NOT_FAILED);
        } catch (final ClassCastException ignored) {
            assertFalse(Mapping.containsKey(treeMap, NOT_A_STRING));
        }
    }

    @Test
    public final void containsValue() {
        assertTrue(Mapping.containsValue(samples, sample1));
        assertTrue(Mapping.containsValue(samples, sample2));
        assertTrue(Mapping.containsValue(samples, sample3));

        assertFalse(Mapping.containsValue(samples, UUID.randomUUID().toString()));
        assertFalse(Mapping.containsValue(samples, NOT_A_STRING));
        assertFalse(Mapping.containsValue(samples, NULL_ELEMENT));

        //noinspection serial,CloneableClassWithoutClone,ClassExtendsConcreteCollection,CloneableClassInSecureContext
        final Map<String, String> treeMap = new TreeMap<String, String>(samples) {
            @Override
            public boolean containsValue(final Object value) {
                return super.containsValue(String.class.cast(Objects.requireNonNull(value)));
            }
        };
        try {
            treeMap.containsValue(NULL_ELEMENT);
            fail(STRAIGHT_NOT_FAILED);
        } catch (final NullPointerException ignored) {
            assertFalse(Mapping.containsValue(treeMap, NULL_ELEMENT));
        }
        try {
            treeMap.containsValue(NOT_A_STRING);
            fail(STRAIGHT_NOT_FAILED);
        } catch (final ClassCastException ignored) {
            assertFalse(Mapping.containsValue(treeMap, NOT_A_STRING));
        }
    }

    @Test
    public final void get() {
        assertEquals(sample2, Mapping.get(samples, sample1));
        assertEquals(sample3, Mapping.get(samples, sample2));
        assertEquals(sample1, Mapping.get(samples, sample3));

        assertNull(Mapping.get(samples, UUID.randomUUID().toString()));
        assertNull(Mapping.get(samples, NOT_A_STRING));
        assertNull(Mapping.get(samples, NULL_ELEMENT));

        final Map<String, String> treeMap = new TreeMap<>(samples);
        try {
            treeMap.get(NULL_ELEMENT);
            fail(STRAIGHT_NOT_FAILED);
        } catch (final NullPointerException ignored) {
            assertNull(Mapping.get(treeMap, NULL_ELEMENT));
        }
        try {
            treeMap.get(NOT_A_STRING);
            fail(STRAIGHT_NOT_FAILED);
        } catch (final ClassCastException ignored) {
            assertNull(Mapping.get(treeMap, NOT_A_STRING));
        }
    }
}