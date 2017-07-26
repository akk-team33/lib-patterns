package net.team33.patterns.reflections;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class FieldsTest {

    private static final EnhancedRandomBuilder RANDOM_BUILDER = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
            .collectionSizeRange(1, 3)
            .stringLengthRange(4, 16);

    private final EnhancedRandom random = RANDOM_BUILDER.build();
    private List<Any> uniques;

    @Before
    public void setUp() throws Exception {
        uniques = Arrays.asList(new Any(random), new Any(random), new Any(random));
    }

    @Test
    public final void copy() throws Exception {
        final Any original = new Any(random);
        final Any target = new Any();
        assertNotEquals(original, target);

        assertSame(target, Any.FIELDS.copy(original, target));
        assertNotSame(target, original);

        assertEquals(original, target);
        assertNotEquals(original.aPublicFinalTransientField, target.aPublicFinalTransientField);
        assertNotEquals(original.aPublicTransientField, target.aPublicTransientField);
    }

    @Test
    public final void map() throws Exception {
        final Any original = new Any(random);
        final Map<String, Object> map = Any.FIELDS.map(original).to(new TreeMap<>());
        final Any result = Any.FIELDS.map(new Any()).from(map);

        assertEquals(original, result);
        assertNotEquals(original.aPublicFinalTransientField, result.aPublicFinalTransientField);
        assertNotEquals(original.aPublicTransientField, result.aPublicTransientField);
    }

    @Test
    public final void matching() throws Exception {
        final Any first = new Any(random);
        final Any second = new Any(random);
        final Any third = Any.FIELDS.copy(first, new Any());
        assertEquals(first.equals(second), Any.FIELDS.matching(first, second));
        assertEquals(first.equals(third), Any.FIELDS.matching(first, third));
    }

    @Test
    public final void hash() throws Exception {
        final List<Any> originals = Stream.concat(
                uniques.stream(),
                uniques.stream().map(any -> Any.FIELDS.copy(any, new Any(random)))
        ).collect(Collectors.toList());
        final int[] count = {0, 0};
        for (final Any left : originals) {
            for (final Any right : originals) {
                if (Any.FIELDS.matching(left, right)) {
                    assertEquals(Any.FIELDS.hash(left), Any.FIELDS.hash(right));
                    count[0] += 1;
                }
                if (Any.FIELDS.hash(left) != Any.FIELDS.hash(right)) {
                    assertFalse(Any.FIELDS.matching(left, right));
                    count[1] += 1;
                }
            }
        }
        assertEquals(12, count[0]);
        assertTrue(24 >= count[1]);
    }

    @Test
    public final void string() throws Exception {
        for (final Any original : uniques) {
            assertEquals(
                    Any.FIELDS.map(original).to(new TreeMap<>()).toString(),
                    Any.FIELDS.toString(original)
            );
        }
    }

    @SuppressWarnings({
            "PublicField", "ProtectedField", "PackageVisibleField", "AssignmentToNull", "ClassWithTooManyFields"})
    private static class Any {
        private static final Fields FIELDS = Fields.of(Any.class);

        private final String aPrivateFinalField;
        protected final String aProtectedFinalField;
        final String aPackageFinalField;
        public final String aPublicFinalField;
        public final transient String aPublicFinalTransientField;

        @SuppressWarnings("FieldMayBeFinal")
        private String aPrivateField;
        @SuppressWarnings("FieldMayBeFinal")
        protected String aProtectedField;
        @SuppressWarnings("FieldMayBeFinal")
        String aPackageField;
        @SuppressWarnings("FieldMayBeFinal")
        public String aPublicField;
        @SuppressWarnings("FieldMayBeFinal")
        public transient String aPublicTransientField;

        private Any() {
            this.aPrivateFinalField = null;
            this.aProtectedFinalField = null;
            this.aPackageFinalField = null;
            this.aPublicFinalField = null;
            this.aPublicFinalTransientField = null;
        }

        private Any(final EnhancedRandom random) {
            this.aPrivateFinalField = random.nextObject(String.class);
            this.aProtectedFinalField = random.nextObject(String.class);
            this.aPackageFinalField = random.nextObject(String.class);
            this.aPublicFinalField = random.nextObject(String.class);
            this.aPublicFinalTransientField = random.nextObject(String.class);

            this.aPrivateField = random.nextObject(String.class);
            this.aProtectedField = random.nextObject(String.class);
            this.aPackageField = random.nextObject(String.class);
            this.aPublicField = random.nextObject(String.class);
            this.aPublicTransientField = random.nextObject(String.class);
        }

        @SuppressWarnings("TypeMayBeWeakened")
        private static List<?> toList(final Any any) {
            return Arrays.asList(
                    any.aPrivateFinalField,
                    any.aProtectedFinalField,
                    any.aPackageFinalField,
                    any.aPublicFinalField,
                    any.aPrivateField,
                    any.aProtectedField,
                    any.aPackageField,
                    any.aPublicField
            );
        }

        @Override
        public final int hashCode() {
            return toList(this).hashCode();
        }

        @Override
        public final boolean equals(final Object obj) {
            return (this == obj) || ((obj instanceof Any) && toList(this).equals(toList((Any) obj)));
        }

        @Override
        public final String toString() {
            return toList(this).toString();
        }
    }
}