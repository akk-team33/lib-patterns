package net.team33.patterns.reflections.alt.alt;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import net.team33.patterns.reflections.alt.alt.Fields.Filter;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FieldsTest {

    private static final EnhancedRandomBuilder RANDOM_BUILDER = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
            .collectionSizeRange(1, 3)
            .stringLengthRange(4, 16);
    private static final BinaryOperator<Field> OVERRIDE = (l, r) -> r;

    private final EnhancedRandom random = RANDOM_BUILDER.build();
    private List<Other> duplicated;

    @Before
    public final void setUp() {
        final List<Other> uniques = random.objects(Other.class, 8).collect(toList());
        duplicated = Stream.concat(
                uniques.stream(),
                uniques.stream().map(origin -> Other.FIELDS.copy(origin, new Other()))
        ).collect(toList());
    }

    @Test
    public final void stream() {
        assertEquals(0, Fields.stream(Object.class).count());
        assertEquals(12, Fields.stream(Any.class).count());
        assertEquals(24, Fields.stream(Other.class).count());
        assertEquals(12, Fields.stream(Other.class)
                .collect(Collectors.toMap(Field::getName, f -> f, OVERRIDE, TreeMap::new))
                .size());
    }

    @Test
    public final void copyAny() {
        final Any original = random.nextObject(Any.class);
        final Any result = Any.FIELDS.copy(original, new Any());
        assertEquals(original, result);
    }

    @Test
    public final void copyOther() {
        final Other original = random.nextObject(Other.class);
        final Other result = Other.FIELDS.copy(original, new Other());
        assertEquals(original, result);
    }

    @Test
    public final void copyDate() {
        final Date original = new Date();
        final Fields<Date> fields = Fields.builder(Date.class)
                .setFilter(Filter.NON_STATIC)
                .build();
        final Date result = fields.copy(original, new Date(0));
        assertEquals(original, result);
    }

//    @Test
//    public final void mapAny() {
//        final Any original = random.nextObject(Any.class);
//        final Map<String, Object> map = Any.FIELDS.map(original).to(new TreeMap<>());
//        final Any result = Any.FIELDS.copy(original, new Any());
//        assertEquals(original, result);
//    }

    @Test
    public final void equalsOther() {
        final int[] count = {0, 0};
        duplicated.forEach(left -> duplicated.forEach(right -> {
            final int index = left.equals(right) ? 0 : 1;
            count[index] += 1;
            assertEquals(index, Other.FIELDS.equals(left, right) ? 0 : 1);
        }));
        assertEquals(32, count[0]);
        assertEquals(224, count[1]);
    }

    @Test
    public final void hashOther() {
        final int[] count = {0, 0};
        duplicated.forEach(left -> duplicated.forEach(right -> {
            final boolean equalsLR = Other.FIELDS.equals(left, right);
            final int leftHash = Other.FIELDS.hashCode(left);
            final int rightHash = Other.FIELDS.hashCode(right);
            if (equalsLR) {
                assertEquals(leftHash, rightHash);
                count[0] += 1;
            }
            if (leftHash != rightHash) {
                assertFalse(equalsLR);
                count[1] += 1;
            }
        }));
        assertTrue("count[0] = " + count[0], 32 >= count[0]);
        assertTrue("count[1] = " + count[1], 224 >= count[1]);
    }

    @SuppressWarnings({"unused", "ConstantNamingConvention", "StaticVariableNamingConvention", "FieldMayBeFinal",
            "FieldMayBeStatic", "ProtectedField", "PackageVisibleField", "ClassWithTooManyFields", "PublicField",
            "TypeMayBeWeakened"})
    private static class Any {
        private static final Fields<Any> FIELDS = Fields.of(Any.class);
        private static String aStaticField = "aStaticField";

        private final String aPrivateFinalField = "Any.aPrivateFinalField";
        protected final String aProtectedFinalField = "Any.aProtectedFinalField";
        final String aPackageFinalField = "Any.aPackageFinalField";
        public final String aPublicFinalField = "Any.aPublicFinalField";
        public final transient String aPublicFinalTransientField = "Any.aPublicFinalTransientField";

        private String aPrivateField = "Any.aPrivateField";
        protected String aProtectedField = "Any.aProtectedField";
        String aPackageField = "Any.aPackageField";
        public String aPublicField = "Any.aPublicField";
        public transient String aPublicTransientField = "Any.aPublicTransientField";

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

        @SuppressWarnings("DesignForExtension")
        @Override
        public int hashCode() {
            return toList(this).hashCode();
        }

        @SuppressWarnings("DesignForExtension")
        @Override
        public boolean equals(final Object obj) {
            return (this == obj) || ((Any.class == obj.getClass()) && toList(this).equals(toList((Any) obj)));
        }

        @SuppressWarnings("DesignForExtension")
        @Override
        public String toString() {
            return toList(this).toString();
        }
    }

    @SuppressWarnings({"unused", "ConstantNamingConvention", "StaticVariableNamingConvention", "FieldMayBeFinal",
            "FieldMayBeStatic", "ProtectedField", "PackageVisibleField", "ClassWithTooManyFields", "PublicField",
            "TypeMayBeWeakened", "FieldNameHidesFieldInSuperclass"})
    private static class Other extends Any {
        private static final Fields<Other> FIELDS = Fields.of(Other.class);
        private static String aStaticField = "aStaticField";

        private final String aPrivateFinalField = "Other.aPrivateFinalField";
        protected final String aProtectedFinalField = "Other.aProtectedFinalField";
        final String aPackageFinalField = "Other.aPackageFinalField";
        public final String aPublicFinalField = "Other.aPublicFinalField";
        public final transient String aPublicFinalTransientField = "Other.aPublicFinalTransientField";

        private String aPrivateField = "Other.aPrivateField";
        protected String aProtectedField = "Other.aProtectedField";
        String aPackageField = "Other.aPackageField";
        public String aPublicField = "Other.aPublicField";
        public transient String aPublicTransientField = "Other.aPublicTransientField";

        private static List<?> toList(final Other other) {
            return Arrays.asList(
                    other.aPrivateFinalField,
                    other.aProtectedFinalField,
                    other.aPackageFinalField,
                    other.aPublicFinalField,
                    other.aPrivateField,
                    other.aProtectedField,
                    other.aPackageField,
                    other.aPublicField
            );
        }

        @SuppressWarnings("DesignForExtension")
        @Override
        public int hashCode() {
            return toList(this).hashCode();
        }

        @SuppressWarnings("DesignForExtension")
        @Override
        public boolean equals(final Object obj) {
            return (this == obj) || ((Other.class == obj.getClass()) && toList(this).equals(toList((Other) obj)));
        }

        @SuppressWarnings("DesignForExtension")
        @Override
        public String toString() {
            return toList(this).toString();
        }
    }
}